package com.zxuhan.aicode.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zxuhan.aicode.ai.model.message.*;
import com.zxuhan.aicode.ai.tools.BaseTool;
import com.zxuhan.aicode.ai.tools.ToolManager;
import com.zxuhan.aicode.constant.AppConstant;
import com.zxuhan.aicode.core.builder.VueProjectBuilder;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.enums.ChatHistoryMessageTypeEnum;
import com.zxuhan.aicode.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;

/**
 * JSON message stream handler.
 * Handles the more complex streaming response for VUE_PROJECT, including tool-call info.
 */
@Slf4j
@Component
public class JsonMessageStreamHandler {

    @Resource
    private VueProjectBuilder vueProjectBuilder;

    @Resource
    private ToolManager toolManager;

    /**
     * Handle a TokenStream (VUE_PROJECT).
     * Parses JSON messages and reassembles them into the response format.
     *
     * @param originFlux         original stream
     * @param chatHistoryService chat history service
     * @param appId              application ID
     * @param loginUser          logged-in user
     * @return processed stream
     */
    public Flux<String> handle(Flux<String> originFlux,
                               ChatHistoryService chatHistoryService,
                               long appId, User loginUser) {
        // Collect data to build the persisted chat history
        StringBuilder chatHistoryStringBuilder = new StringBuilder();
        // Track tool IDs we've already seen, to detect first-time calls
        Set<String> seenToolIds = new HashSet<>();
        return originFlux
                .map(chunk -> {
                    // Parse each JSON chunk
                    return handleJsonMessageChunk(chunk, chatHistoryStringBuilder, seenToolIds);
                })
                .filter(StrUtil::isNotEmpty) // Filter empty strings
                .doOnComplete(() -> {
                    // Once streaming is done, persist the full AI response to chat history
                    String aiResponse = chatHistoryStringBuilder.toString();
                    chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                    // Asynchronously build the Vue project
                    String projectPath = AppConstant.CODE_OUTPUT_ROOT_DIR + "/vue_project_" + appId;
                    vueProjectBuilder.buildProjectAsync(projectPath);
                })
                .doOnError(error -> {
                    // Persist a failure message if the AI response errors out
                    String errorMessage = "AI response failed: " + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }

    /**
     * Parse and collect data from a TokenStream chunk.
     */
    private String handleJsonMessageChunk(String chunk, StringBuilder chatHistoryStringBuilder, Set<String> seenToolIds) {
        // Parse JSON
        StreamMessage streamMessage = JSONUtil.toBean(chunk, StreamMessage.class);
        StreamMessageTypeEnum typeEnum = StreamMessageTypeEnum.getEnumByValue(streamMessage.getType());
        switch (typeEnum) {
            case AI_RESPONSE -> {
                AiResponseMessage aiMessage = JSONUtil.toBean(chunk, AiResponseMessage.class);
                String data = aiMessage.getData();
                // Append directly
                chatHistoryStringBuilder.append(data);
                return data;
            }
            case TOOL_REQUEST -> {
                ToolRequestMessage toolRequestMessage = JSONUtil.toBean(chunk, ToolRequestMessage.class);
                String toolId = toolRequestMessage.getId();
                String toolName = toolRequestMessage.getName();
                // Check whether this is the first time we see this tool id
                if (toolId != null && !seenToolIds.contains(toolId)) {
                    // First call: record the id and emit the full tool call info
                    seenToolIds.add(toolId);
                    // Resolve the tool by name
                    BaseTool tool = toolManager.getTool(toolName);
                    // Return the formatted tool-call info
                    return tool.generateToolRequestResponse();
                } else {
                    // Not the first call: emit nothing
                    return "";
                }
            }
            case TOOL_EXECUTED -> {
                ToolExecutedMessage toolExecutedMessage = JSONUtil.toBean(chunk, ToolExecutedMessage.class);
                JSONObject jsonObject = JSONUtil.parseObj(toolExecutedMessage.getArguments());
                // Resolve the tool by name
                String toolName = toolExecutedMessage.getName();
                BaseTool tool = toolManager.getTool(toolName);
                String result = tool.generateToolExecutedResult(jsonObject);
                // Output content sent to the front end and persisted
                String output = String.format("\n\n%s\n\n", result);
                chatHistoryStringBuilder.append(output);
                return output;
            }
            default -> {
                log.error("Unsupported message type: {}", typeEnum);
                return "";
            }
        }
    }
}