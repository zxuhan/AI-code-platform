package com.zxuhan.aicode.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * Application deploy request.
 */
@Data
public class AppDeployRequest implements Serializable {

    /**
     * Application id.
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}