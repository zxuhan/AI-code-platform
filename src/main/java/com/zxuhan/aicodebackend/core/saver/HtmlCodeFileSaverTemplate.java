package com.zxuhan.aicodebackend.core.saver;

import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicodebackend.ai.model.HtmlCodeResult;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;

/**
 * HTML code file saver
 */
public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult> {

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult result, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", result.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult result) {
        super.validateInput(result);
        // HTML code can not be null or empty
        if (StrUtil.isBlank(result.getHtmlCode())) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "HTML code can not be null");
        }
    }
}