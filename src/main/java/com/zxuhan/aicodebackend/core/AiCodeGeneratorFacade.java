package com.zxuhan.aicodebackend.core;

import com.zxuhan.aicodebackend.ai.AiCodeGeneratorService;
import com.zxuhan.aicodebackend.ai.AiCodeGeneratorServiceFactory;
import com.zxuhan.aicodebackend.ai.model.HtmlCodeResult;
import com.zxuhan.aicodebackend.ai.model.MultiFileCodeResult;
import com.zxuhan.aicodebackend.core.parser.CodeParserExecutor;
import com.zxuhan.aicodebackend.core.saver.CodeFileSaverExecutor;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI code generation facade class that combines code generation and saving functionality
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiCodeGeneratorFacade {


    private final AiCodeGeneratorServiceFactory aiCodeGeneratorServiceFactory;

    /**
     * Unified entry point: generate and save code based on type
     *
     * @param userMessage     User message
     * @param codeGenTypeEnum Generation type
     * @param appId app ID
     * @return Saved directory
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Generation type cannot be empty");
        }
        // Get according AI service based on appId
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executeSaver(result, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "Unsupported generation type：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * Unified entry point: generate and save code based on type (streaming)
     *
     * @param userMessage     User message
     * @param codeGenTypeEnum Generation type
     * @param appId app ID
     * @return Streaming response
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum, Long appId) {
        if (codeGenTypeEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Generation type cannot be empty");
        }
        // Get according AI service based on appId
        AiCodeGeneratorService aiCodeGeneratorService = aiCodeGeneratorServiceFactory.getAiCodeGeneratorService(appId);
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML, appId);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE, appId);
            }
            default -> {
                String errorMessage = "Unsupported generation type：" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }


    /**
     * Generic streaming code processing method
     *
     * @param codeStream  code stream
     * @param codeGenType code generation type
     * @param appId app ID
     * @return streaming response
     */
    private Flux<String> processCodeStream(Flux<String> codeStream, CodeGenTypeEnum codeGenType, Long appId) {
        // String builder for concatenating code after all streaming responses are returned
        StringBuilder codeBuilder = new StringBuilder();
        return codeStream
                // Collect code fragments in real-time
                .doOnNext(chunk -> {codeBuilder.append(chunk);})
                .doOnComplete(() -> {
                    // Save code after streaming response is complete
                    try {
                        String completeCode = codeBuilder.toString();
                        // Parse code into object
                        Object parsedResult = CodeParserExecutor.executeParser(completeCode, codeGenType);
                        // Save code to file
                        File savedDir = CodeFileSaverExecutor.executeSaver(parsedResult, codeGenType, appId);
                        log.info("Save successful, directory：" + savedDir.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("Save failed: {}", e.getMessage());
                    }
                });
    }

}