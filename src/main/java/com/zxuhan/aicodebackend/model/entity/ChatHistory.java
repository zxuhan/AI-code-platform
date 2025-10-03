package com.zxuhan.aicodebackend.model.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Chat history entity
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
     * message
     */
    @Column("message")
    private String message;

    /**
     * user/ai
     */
    @Column("message_type")
    private String messageType;

    /**
     * application id
     */
    @Column("app_id")
    private Long appId;

    /**
     * create user id
     */
    @Column("user_id")
    private Long userId;

    /**
     * create time
     */
    @Column("create_time")
    private Instant createTime;

    /**
     * update time
     */
    @Column("update_time")
    private Instant updateTime;

    /**
     * is deleted
     */
    @Column(value = "is_delete", isLogicDelete = true)
    private Integer isDelete;

}