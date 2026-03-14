package com.zxuhan.aicode.core.saver;

import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicode.ai.model.MultiFileCodeResult;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;

/**
 * Multi-file code saver.
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // Save the HTML file
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // Save the CSS file
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        // Save the JS file
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // HTML code is required; CSS and JS are optional
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML code must not be empty");
        }
    }
}