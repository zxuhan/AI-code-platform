package com.zxuhan.aicode.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

import java.io.Serial;

import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Chat history entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("chat_history")
public class ChatHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * Message body.
     */
    private String message;

    /**
     * Message type: user/ai.
     */
    @Column("messageType")
    private String messageType;

    /**
     * Application id.
     */
    @Column("appId")
    private Long appId;

    /**
     * ID of the user who created the record.
     */
    @Column("userId")
    private Long userId;

    /**
     * Creation time.
     */
    @Column("createTime")
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    @Column("updateTime")
    private LocalDateTime updateTime;

    /**
     * Logical delete flag.
     */
    @Column(value = "isDelete", isLogicDelete = true)
    private Integer isDelete;

}
