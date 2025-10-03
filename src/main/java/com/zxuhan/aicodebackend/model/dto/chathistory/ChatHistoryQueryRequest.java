package com.zxuhan.aicodebackend.model.dto.chathistory;


import com.zxuhan.aicodebackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.Instant;

/**
 * Chat history query request
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChatHistoryQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * message
     */
    private String message;

    /**
     * message type（user/ai）
     */
    private String messageType;

    /**
     * application id
     */
    private Long appId;

    /**
     * create user id
     */
    private Long userId;

    /**
     * Cursor query - creation time of the last record
     * Used for paginated queries to retrieve records earlier than this time
     */
    private Instant lastCreateTime;

    private static final long serialVersionUID = 1L;
}
