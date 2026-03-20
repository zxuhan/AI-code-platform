package com.zxuhan.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * Admin update-application request.
 */
@Data
public class AppAdminUpdateRequest implements Serializable {

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
     * Priority.
     */
    private Integer priority;

    private static final long serialVersionUID = 1L;
}