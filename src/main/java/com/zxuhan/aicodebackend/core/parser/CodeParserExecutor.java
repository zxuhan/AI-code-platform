package com.zxuhan.aicodebackend.core.parser;

import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;

/**
 * Code parser executor
 * Executes corresponding parsing logic based on code generation type
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    /**
     * Execute code parsing
     *
     * @param codeContent     code content
     * @param codeGenTypeEnum code generation type
     * @return parsing result（HtmlCodeResult or MultiFileCodeResult）
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Unsupported code generation type");
        };
    }
}