package com.zxuhan.aicodebackend.ai;

import com.zxuhan.aicodebackend.ai.model.HtmlCodeResult;
import com.zxuhan.aicodebackend.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;
import reactor.core.publisher.Flux;

public interface AiCodeGeneratorService {
    /**
     * Generate HTML code
     *
     * @param userMessage user message
     * @return AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * Generate multi-file code
     *
     * @param userMessage user message
     * @return AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);

    /**
     * Generate HTML code Streaming
     *
     * @param userMessage user message
     * @return AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-html-system-prompt.txt")
    Flux<String> generateHtmlCodeStream(String userMessage);

    /**
     * Generate multi-file code Streaming
     *
     * @param userMessage user message
     * @return AI output
     */
    @SystemMessage(fromResource = "prompt/codegen-multi-file-system-prompt.txt")
    Flux<String> generateMultiFileCodeStream(String userMessage);
}
