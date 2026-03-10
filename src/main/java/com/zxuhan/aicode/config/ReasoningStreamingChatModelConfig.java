package com.zxuhan.aicode.config;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "langchain4j.google-ai-gemini.chat-model")
@Data
public class ReasoningStreamingChatModelConfig {

    private String apiKey;

    /**
     * Reasoning streaming model (used for Vue project generation with tool calls).
     */
    @Bean
    public StreamingChatModel reasoningStreamingChatModel() {
        // Use a stronger model for reasoning + tool calls
        final String modelName = "gemini-2.5-pro";
        final int maxOutputTokens = 32768;
        return GoogleAiGeminiStreamingChatModel.builder()
                .apiKey(apiKey)
                .modelName(modelName)
                .maxOutputTokens(maxOutputTokens)
                .logRequestsAndResponses(true)
                .build();
    }
}
