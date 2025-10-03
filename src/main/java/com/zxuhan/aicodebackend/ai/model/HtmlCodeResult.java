package com.zxuhan.aicodebackend.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * HTML code result
 */
@Description("Generated HTML code result")
@Data
public class HtmlCodeResult {

    /**
     * HTML code
     */
    @Description("HTML code")
    private String htmlCode;

    /**
     * Description
     */
    @Description("description of generated code")
    private String description;
}
