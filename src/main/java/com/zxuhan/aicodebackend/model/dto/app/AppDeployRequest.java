package com.zxuhan.aicodebackend.model.dto.app;

import lombok.Data;

import java.io.Serializable;

/**
 * App deploy request
 */
@Data
public class AppDeployRequest implements Serializable {

    /**
     * app id
     */
    private Long appId;

    private static final long serialVersionUID = 1L;
}