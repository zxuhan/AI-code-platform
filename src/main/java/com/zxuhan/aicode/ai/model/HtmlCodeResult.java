package com.zxuhan.aicode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HTML code generation result.
 */
@Description("Result of generating HTML code")
@Data
public class HtmlCodeResult {

    /**
     * HTML code.
     */
    @Description("HTML code")
    private String htmlCode;

    /**
     * Description of the generated code.
     */
    @Description("Description of the generated code")
    private String description;
}