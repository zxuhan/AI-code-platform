package com.zxuhan.aicodebackend.model.dto.app;

import com.zxuhan.aicodebackend.common.PageRequest;
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
     * code generation type（enum）
     */
    private String codeGenType;

    /**
     * deploy key
     */
    private String deployKey;

    /**
     * priority
     */
    private Integer priority;

    /**
     * create user id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}