package com.zxuhan.aicode.ai;

import com.zxuhan.aicode.ai.model.HtmlCodeResult;
import com.zxuhan.aicode.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

public interface AiCodeGeneratorService {

    /**
     * Generate HTML code.
     *
     * @param userMessage user prompt
     * @return the AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * Generate multi-file code.
     *
     * @param userMessage user prompt
     * @return the AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * Generate HTML code (streaming).
     *
     * @param userMessage user prompt
     * @return the AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * Generate multi-file code (streaming).
     *
     * @param userMessage user prompt
     * @return the AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);

    /**
     * Generate Vue project code (streaming).
     *
     * @param userMessage user prompt
     * @return the AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    TokenStream generateVueProjectCodeStream(@MemoryId long appId, @UserMessage String userMessage);
}
