package com.zxuhan.aicodebackend.core.saver;

import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicodebackend.ai.model.MultiFileCodeResult;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;

/**
 * Multi-file code file saver
 */
public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult result, String baseDirPath) {
        // save HTML file
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
        // save CSS file
        writeToFile(baseDirPath, "style.css", result.getCssCode());
        // save JavaScript file
        writeToFile(baseDirPath, "script.js", result.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult result) {
        super.validateInput(result);
        // at least HTML code，CSS and JS can be empty
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML code content can not be empty");
        }
    }
}