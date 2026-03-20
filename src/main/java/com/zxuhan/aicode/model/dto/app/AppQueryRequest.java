package com.zxuhan.aicode.model.dto.app;

import com.zxuhan.aicode.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppQueryRequest extends PageRequest implements Serializable {

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
     * Priority.
     */
    private Integer priority;

    /**
     * Creator user id.
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
} 