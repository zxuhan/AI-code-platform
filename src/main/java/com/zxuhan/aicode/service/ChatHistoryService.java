package com.zxuhan.aicode.service;

import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.zxuhan.aicode.model.dto.chathistory.ChatHistoryQueryRequest;
import com.zxuhan.aicode.model.entity.ChatHistory;
import com.zxuhan.aicode.model.entity.User;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;

import java.time.LocalDateTime;

/**
 * Chat history service.
 */
public interface ChatHistoryService extends IService<ChatHistory> {

    /**
     * Add a chat message.
     *
     * @param appId       application id
     * @param message     message body
     * @param messageType message type
     * @param userId      user id
     * @return whether the insert succeeded
     */
    boolean addChatMessage(Long appId, String message, String messageType, Long userId);

    /**
     * Delete chat history by application id.
     *
     * @param appId application id
     * @return whether the delete succeeded
     */
    boolean deleteByAppId(Long appId);

    /**
     * Cursor-paginate chat records for a given application.
     *
     * @param appId          application id
     * @param pageSize       page size
     * @param lastCreateTime cursor (creation time of last record)
     * @param loginUser      logged-in user
     * @return page of chat history
     */
    Page<ChatHistory> listAppChatHistoryByPage(Long appId, int pageSize,
                                               LocalDateTime lastCreateTime,
                                               User loginUser);

    /**
     * Load chat history into the in-memory chat memory.
     *
     * @param appId      application id
     * @param chatMemory chat memory window
     * @param maxCount   maximum number of records to load
     * @return number of records loaded
     */
    int loadChatHistoryToMemory(Long appId, MessageWindowChatMemory chatMemory, int maxCount);

    /**
     * Build a query wrapper from the request.
     *
     * @param chatHistoryQueryRequest query parameters
     * @return query wrapper
     */
    QueryWrapper getQueryWrapper(ChatHistoryQueryRequest chatHistoryQueryRequest);
}
