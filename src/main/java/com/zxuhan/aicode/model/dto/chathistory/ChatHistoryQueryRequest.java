package com.zxuhan.aicode.model.dto.chathistory;

import com.zxuhan.aicode.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Chat history query request.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Message body.
     */
    private String message;

    /**
     * Message type (user/ai).
     */
    private String messageType;

    /**
     * Application id.
     */
    private Long appId;

    /**
     * Creator user id.
     */
    private Long userId;

    /**
     * Cursor pagination: creation time of the last record (fetch records older than this).
     */
    private LocalDateTime lastCreateTime;

    private static final long serialVersionUID = 1L;
}