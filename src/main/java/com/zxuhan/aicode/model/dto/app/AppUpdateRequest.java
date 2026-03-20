package com.zxuhan.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * Update-application request.
 */
@Data
public class AppUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * Application name.
     */
    private String appName;

    private static final long serialVersionUID = 1L;
}