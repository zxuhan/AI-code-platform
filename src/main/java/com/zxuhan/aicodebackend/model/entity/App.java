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
 * App entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("app")
public class App implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    private Long id;

    /**
     * app name
     */
    @Column("app_name")
    private String appName;

    /**
     * app cover
     */
    @Column("cover")
    private String cover;

    /**
     * app initialization prompt
     */
    @Column("init_prompt")
    private String initPrompt;

    /**
     * code generation type（enum）
     */
    @Column("code_gen_type")
    private String codeGenType;

    /**
     * deploy key
     */
    @Column("deploy_key")
    private String deployKey;

    /**
     * deploy time
     */
    @Column("deployed_time")
    private Instant deployedTime;

    /**
     * priority
     */
    @Column("priority")
    private Integer priority;

    /**
     * create user id
     */
    @Column("user_id")
    private Long userId;

    /**
     * edit time
     */
    @Column("edit_time")
    private Instant editTime;

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
     * Soft delete flag (0: not deleted, 1: deleted)
     */
    @Column(value = "is_delete", isLogicDelete = true)
    private Integer isDelete;

}