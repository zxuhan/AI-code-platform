package com.zxuhan.aicodebackend.core.saver;

import com.zxuhan.aicodebackend.ai.model.HtmlCodeResult;
import com.zxuhan.aicodebackend.ai.model.MultiFileCodeResult;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * Code file saver executor
 * Executes corresponding save logic based on code generation type
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * Execute code saver
     *
     * @param codeResult  code result
     * @param codeGenType code generation type
     * @param appId app ID
     * @return saved file
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) throws BusinessException {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Unsupported code generation type: " + codeGenType);
        };
    }
}