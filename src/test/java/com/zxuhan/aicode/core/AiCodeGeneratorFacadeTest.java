package com.zxuhan.aicode.core;

import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("Generate a login page in no more than 20 lines of code", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    void generateAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream("Generate a login page in no more than 20 lines of code", CodeGenTypeEnum.HTML, 1L);
        // Block until all data is collected
        List<String> result = codeStream.collectList().block();
        // Verify the result
        Assertions.assertNotNull(result);
        // Concatenate to obtain the full content
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }

    @Test
    void generateVueProjectCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeStream(
                "Simple to-do tracking site, total code under 200 lines",
                CodeGenTypeEnum.VUE_PROJECT, 1L);
        // Block until all data is collected
        List<String> result = codeStream.collectList().block();
        // Verify the result
        Assertions.assertNotNull(result);
        String completeContent = String.join("", result);
        Assertions.assertNotNull(completeContent);
    }
}
