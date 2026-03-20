package com.zxuhan.aicode.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Application view object.
 */
@Data
public class AppVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Application name.
     */
    private String appName;

    /**
     * Application cover.
     */
    private String cover;

    /**
     * Initial prompt.
     */
    private String initPrompt;

    /**
     * Code generation type (enum).
     */
    private String codeGenType;

    /**
     * Deployment key.
     */
    private String deployKey;

    /**
     * Deployment time.
     */
    private LocalDateTime deployedTime;

    /**
     * Priority.
     */
    private Integer priority;

    /**
     * Creator user id.
     */
    private Long userId;

    /**
     * Creation time.
     */
    private LocalDateTime createTime;

    /**
     * Update time.
     */
    private LocalDateTime updateTime;

    /**
     * Creator user info.
     */
    private UserVO user;

    private static final long serialVersionUID = 1L;
}