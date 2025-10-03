package com.zxuhan.aicodebackend.common;

import lombok.Data;

@Data
public class PageRequest {

    /**
     * Current page number
     */
    private int pageNum = 1;

    /**
     * Page size
     */
    private int pageSize = 10;

    /**
     * Sort field
     */
    private String sortField;

    /**
     * Sort order (default descending)
     */
    private String sortOrder = "descend";
}