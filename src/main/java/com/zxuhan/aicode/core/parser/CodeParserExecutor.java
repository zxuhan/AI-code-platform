package com.zxuhan.aicode.core.parser;

import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;

/**
 * Code parser executor.
 * Picks a parser implementation based on the code generation type.
 */
public class CodeParserExecutor {

    private static final HtmlCodeParser htmlCodeParser = new HtmlCodeParser();

    private static final MultiFileCodeParser multiFileCodeParser = new MultiFileCodeParser();

    /**
     * Execute parsing.
     *
     * @param codeContent     raw code
     * @param codeGenTypeEnum code generation type
     * @return parsing result (HtmlCodeResult or MultiFileCodeResult)
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum) {
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Unsupported code generation type");
        };
    }
}
