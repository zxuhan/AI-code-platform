package com.zxuhan.aicodebackend.model.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicodebackend.model.dto.chathistory.ChatHistoryQueryRequest;
import com.zxuhan.aicodebackend.model.entity.ChatHistory;
import com.zxuhan.aicodebackend.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.Instant;
/**
 * Chat history service
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * Add chat history
     *
     * @param appId       app id
     * @param message     message
     * @param messageType message type
     * @param userId      user id
     * @return if successful
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * Delete chat history based on application id
     *
     * @param appId
     * @return
     */
    boolean deleteByAppId(Long appId);

    /**
     * Get chat history information of an app(paginated)
     *
     *
     * @param appId
     * @param pageSize
     * @param lastCreateTime
     * @param loginUser
     * @return
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               Instant lastCreateTime,
                                               User loginUser);

    /**
     * Load chat history to memory
     *
     * @param appId
     * @param chatMemory
     * @param maxCount maximum number
     * @return successful number
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);


    /**
     * Construct data query parameters based on query conditions
     *
     * @param chatHistoryQueryRequest
     * @return
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}