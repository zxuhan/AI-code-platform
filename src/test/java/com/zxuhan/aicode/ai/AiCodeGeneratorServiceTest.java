package com.zxuhan.aicode.ai;

import com.zxuhan.aicode.ai.model.HtmlCodeResult;
import com.zxuhan.aicode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AiCodeGeneratorServiceTest {

    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    @Test
    void generateHtmlCode() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("Build a personal blog for zxuhan, no more than 20 lines");
        Assertions.assertNotNull(result);
    }

    @Test
    void generateMultiFileCode() {
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode("Build a guestbook for zxuhan");
        Assertions.assertNotNull(result);
    }

    @Test
    void testChatMemory() {
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode("Build a tools website for zxuhan, total code under 20 lines");
        Assertions.assertNotNull(result);
        result = aiCodeGeneratorService.generateHtmlCode("Do not generate a website; tell me what you just did?");
        Assertions.assertNotNull(result);
        result = aiCodeGeneratorService.generateHtmlCode("Build a tools website for zxuhan, total code under 20 lines");
        Assertions.assertNotNull(result);
        result = aiCodeGeneratorService.generateHtmlCode("Do not generate a website; tell me what you just did?");
        Assertions.assertNotNull(result);
    }
}
