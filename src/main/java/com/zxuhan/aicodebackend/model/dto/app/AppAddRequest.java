package com.zxuhan.aicodebackend.model.dto.app;


import lombok.Data;

import java.io.Serializable;

/**
 * App add request
 */
@Data
public class AppAddRequest implements Serializable {

    /**
     * app initialization prompt
     */
    private String initPrompt;

    private static final long serialVersionUID = 1L;
}