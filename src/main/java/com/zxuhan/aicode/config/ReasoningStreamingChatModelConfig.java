package com.zxuhan.aicode.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.google-ai-gemini.chat-model")
@Data
public class ReasoningStreamingChatModelConfig {

    private String apiKey;

    /**
     * Reasoning streaming model name. Override with GEMINI_REASONING_MODEL.
     * Default is gemini-2.5-pro because the Vue agent benefits from the bigger
     * reasoning model + larger output window for tool-heavy sessions.
     */
    @Value("${gemini.reasoning.model:gemini-2.5-pro}")
    private String reasoningModelName;

    /**
     * Max output tokens for the reasoning model. Override with GEMINI_REASONING_MAX_OUTPUT_TOKENS.
     * Default 32768 is safe for gemini-2.5-pro. If you switch to a flash model
     * (which caps at 8192), lower this accordingly.
     */
    @Value("${gemini.reasoning.max-output-tokens:32768}")
    private int reasoningMaxOutputTokens;

    @Bean
    public StreamingChatModel reasoningStreamingChatModel() {
        return GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(reasoningModelName)
                .maxOutputTokens(reasoningMaxOutputTokens)
                .logRequestsAndResponses(true)
                .build();
    }
}
