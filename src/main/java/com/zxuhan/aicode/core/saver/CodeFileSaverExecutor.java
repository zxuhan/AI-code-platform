package com.zxuhan.aicode.core.saver;

import com.zxuhan.aicode.ai.model.HtmlCodeResult;
import com.zxuhan.aicode.ai.model.MultiFileCodeResult;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;

/**
 * Code file save executor.
 * Picks a saver implementation based on the code generation type.
 */
public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaver = new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaver = new MultiFileCodeFileSaverTemplate();

    /**
     * Execute the save operation.
     *
     * @param codeResult  code result object
     * @param codeGenType code generation type
     * @param appId       application id
     * @return the directory the code was saved into
     */
    public static File executeSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId) {
        return switch (codeGenType) {
            case HTML -> htmlCodeFileSaver.saveCode((HtmlCodeResult) codeResult, appId);
            case MULTI_FILE -> multiFileCodeFileSaver.saveCode((MultiFileCodeResult) codeResult, appId);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Unsupported code generation type: " + codeGenType);
        };
    }
}
