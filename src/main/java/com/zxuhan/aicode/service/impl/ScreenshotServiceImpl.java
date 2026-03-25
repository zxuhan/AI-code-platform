package com.zxuhan.aicode.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.exception.ThrowUtils;
import com.zxuhan.aicode.manager.R2Manager;
import com.zxuhan.aicode.service.ScreenshotService;
import com.zxuhan.aicode.utils.WebScreenshotUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Slf4j
public class ScreenshotServiceImpl implements ScreenshotService {

    @Resource
    private R2Manager r2Manager;

    @Override
    public String generateAndUploadScreenshot(String webUrl) {
        ThrowUtils.throwIf(StrUtil.isBlank(webUrl), ErrorCode.PARAMS_ERROR, "Screenshot URL must not be empty");
        log.info("Generating web screenshot, URL: {}", webUrl);
        String localScreenshotPath = WebScreenshotUtils.saveWebPageScreenshot(webUrl);
        ThrowUtils.throwIf(StrUtil.isBlank(localScreenshotPath), ErrorCode.OPERATION_ERROR, "Failed to generate web screenshot");
        try {
            String screenshotUrl = uploadScreenshotToR2(localScreenshotPath);
            ThrowUtils.throwIf(StrUtil.isBlank(screenshotUrl), ErrorCode.OPERATION_ERROR, "Failed to upload screenshot to object storage");
            log.info("Screenshot uploaded, URL: {}", screenshotUrl);
            return screenshotUrl;
        } finally {
            cleanupLocalFile(localScreenshotPath);
        }
    }

    /**
     * Upload the screenshot to R2 object storage.
     *
     * @param localScreenshotPath path to the local screenshot
     * @return public URL of the uploaded screenshot, or null on failure
     */
    private String uploadScreenshotToR2(String localScreenshotPath) {
        if (StrUtil.isBlank(localScreenshotPath)) {
            return null;
        }
        File screenshotFile = new File(localScreenshotPath);
        if (!screenshotFile.exists()) {
            log.error("Screenshot file does not exist: {}", localScreenshotPath);
            return null;
        }
        String fileName = UUID.randomUUID().toString().substring(0, 8) + "_compressed.jpg";
        String objectKey = generateScreenshotKey(fileName);
        return r2Manager.uploadFile(objectKey, screenshotFile);
    }

    /**
     * Build the screenshot's object-storage key.
     * Format: screenshots/2025/07/31/filename.jpg
     */
    private String generateScreenshotKey(String fileName) {
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("screenshots/%s/%s", datePath, fileName);
    }

    /**
     * Delete the local screenshot file.
     *
     * @param localFilePath local file path
     */
    private void cleanupLocalFile(String localFilePath) {
        File localFile = new File(localFilePath);
        if (localFile.exists()) {
            FileUtil.del(localFile);
            log.info("Removed local file: {}", localFilePath);
        }
    }
}
