package com.zxuhan.aicode.core.saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicode.constant.AppConstant;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * Abstract code file saver - template method pattern.
 *
 * @param <T> code result type
 */
public abstract class CodeFileSaverTemplate<T> {

    /**
     * Root directory for saved files.
     */
    private static final String FILE_SAVE_ROOT_DIR = AppConstant.CODE_OUTPUT_ROOT_DIR;

    /**
     * Template method: save code following the standard flow.
     *
     * @param result code result object
     * @param appId  application id
     * @return the directory the code was saved into
     */
    public final File saveCode(T result, Long appId) {
        // 1. Validate input
        validateInput(result);
        // 2. Build the unique directory
        String baseDirPath = buildUniqueDir(appId);
        // 3. Save the files (subclass-specific)
        saveFiles(result, baseDirPath);
        // 4. Return the directory
        return new File(baseDirPath);
    }

    /**
     * Helper that writes a single file.
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
     * Validate input parameters (subclasses may override).
     *
     * @param result code result object
     */
    protected void validateInput(T result) {
        if (result == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Code result must not be null");
        }
    }

    /**
     * Build a unique directory path: tmp/code_output/bizType_appId.
     *
     * @param appId application id
     * @return directory path
     */
    protected String buildUniqueDir(Long appId) {
        if (appId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Application ID must not be null");
        }
        String codeType = getCodeType().getValue();
        String uniqueDirName = StrUtil.format("{}_{}", codeType, appId);
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * Save the files (subclass-specific).
     *
     * @param result      code result object
     * @param baseDirPath base directory
     */
    protected abstract void saveFiles(T result, String baseDirPath);

    /**
     * Get the code generation type for this saver.
     *
     * @return the code generation type
     */
    protected abstract CodeGenTypeEnum getCodeType();
}
