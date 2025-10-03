package com.zxuhan.aicodebackend.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * App update request
 */
@Data
public class AppUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * app name
     */
    private String appName;

    private static final long serialVersionUID = 1L;
}