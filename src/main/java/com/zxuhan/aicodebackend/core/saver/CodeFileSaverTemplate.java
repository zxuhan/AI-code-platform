package com.zxuhan.aicodebackend.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicodebackend.constant.AppConstant;
import com.zxuhan.aicodebackend.exception.BusinessException;
import com.zxuhan.aicodebackend.exception.ErrorCode;
import com.zxuhan.aicodebackend.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Abstract code file saver - Template method pattern
 *
 * @param <T>
 */
public abstract class CodeFileSaverTemplate<T> {

    /**
     * Root directory for file saving
     */
    private static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     * Template method: Standard process for saving code
     *
     * @param result code result object
     * @param appId app ID
     * @return saved directory
     */
    public final File saveCode(T result, Long appId) {
        // 1. Validate input
        validateInput(result);
        // 2. Build unique directory
        String baseDirPath = buildUniqueDir(appId);
        // 3. Save files (specific implementation delegated to subclasses)
        saveFiles(result, baseDirPath);
        // 4. Return file directory object
        return new File(baseDirPath);
    }

    /**
     * Utility method for writing a single file
     *
     * @param dirPath  directory path
     * @param filename file name
     * @param content  file content
     */
    public final void writeToFile(String dirPath, String filename, String content) {
        if (StrUtil.isNotBlank(content)) {
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content, filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * Validate input parameters (can be overridden by subclasses)
     *
     * @param result code result object
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Code result object cannot be null");
        }
    }

    /**
     * Build unique path for files: tmp/code_output/bizType_snowflake ID
     *
     * @param appId app ID
     * @return directory path
     */
    protected String buildUniqueDir(Long appId) {
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "app ID can not be null");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * Save files (specific implementation delegated to subclasses)
     *
     * @param result      code result object
     * @param baseDirPath base directory path
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * Get code generation type
     *
     * @return code generation type enum
     */
    protected abstract CodeGenTypeEnum getCodeType();
}