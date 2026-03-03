package com.zxuhan.aicode.common;

import lombok.Data;

import java.io.Serializable;

/**
 * Delete request DTO.
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}