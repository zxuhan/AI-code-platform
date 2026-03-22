package com.zxuhan.aicode.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.zxuhan.aicode.constant.UserConstant;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.exception.ThrowUtils;
import com.zxuhan.aicode.model.dto.chathistory.ChatHistoryQueryRequest;
import com.zxuhan.aicode.model.entity.App;
import com.zxuhan.aicode.model.entity.ChatHistory;
import com.zxuhan.aicode.mapper.ChatHistoryMapper;
import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.enums.ChatHistoryMessageTypeEnum;
import com.zxuhan.aicode.service.AppService;
import com.zxuhan.aicode.service.ChatHistoryService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Chat history service implementation.
 */
@Service
@Slf4j
public class ChatHistoryServiceImpl extends ServiceImpl<ChatHistoryMapper, ChatHistory> implements ChatHistoryService {

    @Resource
    @Lazy
    private AppService appService;

    @Override
    public boolean addChatMessage(Long appId, String message, String messageType, Long userId) {
        // Basic validation
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "Application ID must not be empty");
        ThrowUtils.throwIf(StrUtil.isBlank(message), ErrorCode.PARAMS_ERROR, "Message body must not be empty");
        ThrowUtils.throwIf(StrUtil.isBlank(messageType), ErrorCode.PARAMS_ERROR, "Message type must not be empty");
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "User ID must not be empty");
        // Validate the message type
        ChatHistoryMessageTypeEnum messageTypeEnum = ChatHistoryMessageTypeEnum.getEnumByValue(messageType);
        ThrowUtils.throwIf(messageTypeEnum == null, ErrorCode.PARAMS_ERROR, "Unsupported message type");
        // Persist
        ChatHistory chatHistory = ChatHistory.builder()
                .appId(appId)
                .message(message)
                .messageType(messageType)
                .userId(userId)
                .build();
        return this.save(chatHistory);
    }

    @Override
    public boolean deleteByAppId(Long appId) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "Application ID must not be empty");
        QueryWrapper queryWrapper = QueryWrapper.create()
                .eq("appId", appId);
        return this.remove(queryWrapper);
    }

    @Override
    public Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                                      LocalDateTime lastCreateTime,
                                                      User loginUser) {
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR, "Application ID must not be empty");
        ThrowUtils.throwIf(pageSize <= 0 || pageSize > 50, ErrorCode.PARAMS_ERROR, "Page size must be between 1 and 50");
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        // Authorization: only the creator or an admin can view chat history
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "Application not found");
        boolean isAdmin = UserConstant.ADMIN_ROLE.equals(loginUser.getUserRole());
        boolean isCreator = app.getUserId().equals(loginUser.getId());
        ThrowUtils.throwIf(!isAdmin && !isCreator, ErrorCode.NO_AUTH_ERROR, "Not authorized to view this application's chat history");
        // Build the query
        ChatHistoryQueryRequest queryRequest = new ChatHistoryQueryRequest();
        queryRequest.setAppId(appId);
        queryRequest.setLastCreateTime(lastCreateTime);
        QueryWrapper queryWrapper = this.getQueryWrapper(queryRequest);
        // Execute the query
        return this.page(Page.of(1, pageSize), queryWrapper);
    }

    @Override
    public int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount) {
        try {
            QueryWrapper queryWrapper = QueryWrapper.create()
                    .eq(ChatHistory::getAppId, appId)
                    .orderBy(ChatHistory::getCreateTime, false)
                    .limit(1, maxCount);
            List<ChatHistory> historyList = this.list(queryWrapper);
            if (CollUtil.isEmpty(historyList)) {
                return 0;
            }
            // Reverse to chronological order (oldest first)
            historyList = historyList.reversed();
            // Replay the messages into the chat memory
            int loadedCount = 0;
            // Clear existing memory to avoid duplicates
            chatMemory.clear();
            for (ChatHistory history : historyList) {
                if (ChatHistoryMessageTypeEnum.USER.getValue().equals(history.getMessageType())) {
                    chatMemory.add(UserMessage.from(history.getMessage()));
                } else if (ChatHistoryMessageTypeEnum.AI.getValue().equals(history.getMessageType())) {
                    chatMemory.add(AiMessage.from(history.getMessage()));
                }
                loadedCount++;
            }
            log.info("Loaded {} history messages for appId: {}", loadedCount, appId);
            return loadedCount;
        } catch (Exception e) {
            log.error("Failed to load chat history, appId: {}, error: {}", appId, e.getMessage(), e);
            // Failure is non-fatal: we just lose the history context
            return 0;
        }
    }

    /**
     * Build the query wrapper.
     *
     * @param chatHistoryQueryRequest query parameters
     * @return query wrapper
     */
    @Override
    public QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        if (chatHistoryQueryRequest == null) {
            return queryWrapper;
        }
        Long id = chatHistoryQueryRequest.getId();
        String message = chatHistoryQueryRequest.getMessage();
        String messageType = chatHistoryQueryRequest.getMessageType();
        Long appId = chatHistoryQueryRequest.getAppId();
        Long userId = chatHistoryQueryRequest.getUserId();
        LocalDateTime lastCreateTime = chatHistoryQueryRequest.getLastCreateTime();
        String sortField = chatHistoryQueryRequest.getSortField();
        String sortOrder = chatHistoryQueryRequest.getSortOrder();
        // Combine conditions
        queryWrapper.eq("id", id)
                .like("message", message)
                .eq("messageType", messageType)
                .eq("appId", appId)
                .eq("userId", userId);
        // Cursor pagination uses createTime
        if (lastCreateTime != null) {
            queryWrapper.lt("createTime", lastCreateTime);
        }
        // Sort
        if (StrUtil.isNotBlank(sortField)) {
            queryWrapper.orderBy(sortField, "ascend".equals(sortOrder));
        } else {
            // Default: order by createTime descending
            queryWrapper.orderBy("createTime", false);
        }
        return queryWrapper;
    }
}
