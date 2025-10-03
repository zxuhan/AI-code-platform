package com.zxuhan.aicodebackend.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * App admin update request
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

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
     * priority
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}