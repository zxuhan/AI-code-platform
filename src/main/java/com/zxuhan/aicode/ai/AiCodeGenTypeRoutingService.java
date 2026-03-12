package com.zxuhan.aicode.ai;

import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;
import dev.langchain4j.service.SystemMessage;

/**
 * AI service that routes user requests to a code generation type.
 * Returns the enum value directly via structured output.
 */
public interface AiCodeGenTypeRoutingService {

    /**
     * Pick a code generation type based on the user's requirement.
     *
     * @param userPrompt user-provided requirement description
     * @return recommended code generation type
     */
    @SystemMessage(fromResource = "prompt/codegen-routing-system-prompt.txt")
    CodeGenTypeEnum routeCodeGenType(String userPrompt);
}
