package com.zxuhan.aicodebackend.core;

import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;



@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Autowired
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("Generate a login web page, no more than 20 line code", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("Generate a login web page, no more than 20 line code", CodeGenTypeEnum.MULTI_FILE, 1L);
        // Block to wait all data chunks are collected
        List<String> result = codeStream.collectList().block();
        // Verify result
        Assertions.assertNotNull(result);
        // Concatenate strings to get the complete result
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }
}