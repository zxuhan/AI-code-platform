package com.zxuhan.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * Create-application request.
 */
@Data
public class AppAddRequest implements Serializable {

    /**
     * Initial prompt for the application.
     */
    private String initPrompt;

    private static final long serialVersionUID = 1L;
} 