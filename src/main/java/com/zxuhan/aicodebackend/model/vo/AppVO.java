package com.zxuhan.aicodebackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.stream.IntStream;

/**
 * App VO
 */
@Data
public class AppVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * app name
     */
    private String appName;

    /**
     * app cover
     */
    private String cover;

    /**
     * app initialization prompt
     */
    private String initPrompt;

    /**
     * code generation type
     */
    private String codeGenType;

    /**
     * deploy key
     */
    private String deployKey;

    /**
     * deploy time
     */
    private Instant deployedTime;

    /**
     * priority
     */
    private Integer priority;

    /**
     * create user id
     */
    private Long userId;

    /**
     * create time
     */
    private Instant createTime;

    /**
     * update time
     */
    private Instant updateTime;

    /**
     * create userVO
     */
    private UserVO user;

    private static final long serialVersionUID = 1L;
}