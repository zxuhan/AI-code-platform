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
 * Application entity.
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
     * Application name.
     */
    @Column("appName")
    private String appName;

    /**
     * Application cover image.
     */
    private String cover;

    /**
     * Initial prompt for the application.
     */
    @Column("initPrompt")
    private String initPrompt;

    /**
     * Code generation type (enum).
     */
    @Column("codeGenType")
    private String codeGenType;

    /**
     * Deployment key.
     */
    @Column("deployKey")
    private String deployKey;

    /**
     * Deployment time.
     */
    @Column("deployedTime")
    private LocalDateTime deployedTime;

    /**
     * Priority.
     */
    private Integer priority;

    /**
     * ID of the user who created the application.
     */
    @Column("userId")
    private Long userId;

    /**
     * Edit time.
     */
    @Column("editTime")
    private LocalDateTime editTime;

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
