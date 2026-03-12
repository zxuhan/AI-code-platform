package com.zxuhan.aicode.ai;

import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AiCodeGenTypeRoutingServiceTest {

    @Resource
    private AiCodeGenTypeRoutingService aiCodeGenTypeRoutingService;

    @Test
    public void testRouteCodeGenType() {
        String userPrompt = "Build a simple personal introduction page";
        CodeGenTypeEnum result = aiCodeGenTypeRoutingService.routeCodeGenType(userPrompt);
        log.info("User request: {} -> {}", userPrompt, result.getValue());
        userPrompt = "Build a company website with three pages: home, about us, contact us";
        result = aiCodeGenTypeRoutingService.routeCodeGenType(userPrompt);
        log.info("User request: {} -> {}", userPrompt, result.getValue());
        userPrompt = "Build an e-commerce admin system with user, product, and order management; routing and state management required";
        result = aiCodeGenTypeRoutingService.routeCodeGenType(userPrompt);
        log.info("User request: {} -> {}", userPrompt, result.getValue());
    }
}