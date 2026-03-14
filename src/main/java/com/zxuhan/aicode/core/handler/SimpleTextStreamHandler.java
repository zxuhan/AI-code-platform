package com.zxuhan.aicode.core.handler;

import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.enums.ChatHistoryMessageTypeEnum;
import com.zxuhan.aicode.service.ChatHistoryService;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Simple text stream handler.
 * Handles streaming responses for HTML and MULTI_FILE.
 */
@Slf4j
public class SimpleTextStreamHandler {


    /**
     * Handle a plain text stream (HTML, MULTI_FILE).
     * Collects the full response text.
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
        StringBuilder aiResponseBuilder = new StringBuilder();
        return originFlux
                .map(chunk -> {
                    // Collect the AI response content
                    aiResponseBuilder.append(chunk);
                    return chunk;
                })
                .doOnComplete(() -> {
                    // After the stream completes, persist the AI message
                    String aiResponse = aiResponseBuilder.toString();
                    chatHistoryService.addChatMessage(appId, aiResponse, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                })
                .doOnError(error -> {
                    // Persist a failure message if the AI response errors out
                    String errorMessage = "AI response failed: " + error.getMessage();
                    chatHistoryService.addChatMessage(appId, errorMessage, ChatHistoryMessageTypeEnum.AI.getValue(), loginUser.getId());
                });
    }
}
