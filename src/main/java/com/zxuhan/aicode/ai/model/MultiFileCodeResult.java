package com.zxuhan.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * Multi-file code generation result.
 */
@Description("Result of generating multiple code files")
@Data
public class MultiFileCodeResult {

    /**
     * HTML code.
     */
    @Description("HTML code")
    private String htmlCode;

    /**
     * CSS code.
     */
    @Description("CSS code")
    private String cssCode;

    /**
     * JS code.
     */
    @Description("JS code")
    private String jsCode;

    /**
     * Description of the generated code.
     */
    @Description("Description of the generated code")
    private String description;
}