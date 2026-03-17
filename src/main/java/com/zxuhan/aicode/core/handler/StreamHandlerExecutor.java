package com.zxuhan.aicode.core.handler;

import com.zxuhan.aicode.model.entity.User;
import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;
import com.zxuhan.aicode.service.ChatHistoryService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Stream handler executor.
 * Picks a handler based on the code generation type:
 * 1. Plain Flux<String> streams (HTML, MULTI_FILE) -> SimpleTextStreamHandler
 * 2. Complex TokenStream-style streams (VUE_PROJECT) -> JsonMessageStreamHandler
 */
@Slf4j
@Component
public class StreamHandlerExecutor {

    @Resource
    private JsonMessageStreamHandler jsonMessageStreamHandler;

    /**
     * Pick a handler and process the stream while writing chat history.
     *
     * @param originFlux         original stream
     * @param chatHistoryService chat history service
     * @param appId              application ID
     * @param loginUser          logged-in user
     * @param codeGenType        code generation type
     * @return processed stream
     */
    public Flux<String> doExecute(Flux<String> originFlux,
                                  ChatHistoryService chatHistoryService,
                                  long appId, User loginUser, CodeGenTypeEnum codeGenType) {
        return switch (codeGenType) {
            case VUE_PROJECT -> // Use the injected handler instance
                    jsonMessageStreamHandler.handle(originFlux, chatHistoryService, appId, loginUser);
            case HTML, MULTI_FILE -> // Simple text handler has no dependencies
                    new SimpleTextStreamHandler().handle(originFlux, chatHistoryService, appId, loginUser);
        };
    }
}
