package com.zxuhan.aicodebackend.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * Multi-file code result
 */
@Description("Generated multi-file code result")
@Data
public class MultiFileCodeResult {

    /**
     * HTML code
     */
    @Description("HTML code")
    private String htmlCode;

    /**
     * CSS code
     */
    @Description("CSS code")
    private String cssCode;

    /**
     * JS code
     */
    @Description("JavaScript code")
    private String jsCode;

    /**
     * 描述
     */
    @Description("description of generated code")
    private String description;
}
