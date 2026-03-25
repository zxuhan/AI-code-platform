package com.zxuhan.aicode.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import com.zxuhan.aicode.exception.ThrowUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Set;

@Service
@Slf4j
public class ProjectDownloadServiceImpl implements ProjectDownloadService {

    /**
     * File and directory names to exclude from the archive.
     */
    private static final Set<String> IGNORED_NAMES = Set.of(
            "node_modules",
            ".git",
            "dist",
            "build",
            ".DS_Store",
            ".env",
            "target",
            ".mvn",
            ".idea",
            ".vscode"
    );

    /**
     * File extensions to exclude from the archive.
     */
    private static final Set<String> IGNORED_EXTENSIONS = Set.of(
            ".log",
            ".tmp",
            ".cache"
    );


    @Override
    public void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response) {
        // Basic validation
        ThrowUtils.throwIf(StrUtil.isBlank(projectPath), ErrorCode.PARAMS_ERROR, "Project path must not be empty");
        ThrowUtils.throwIf(StrUtil.isBlank(downloadFileName), ErrorCode.PARAMS_ERROR, "Download file name must not be empty");
        File projectDir = new File(projectPath);
        ThrowUtils.throwIf(!projectDir.exists(), ErrorCode.PARAMS_ERROR, "Project path does not exist");
        ThrowUtils.throwIf(!projectDir.isDirectory(), ErrorCode.PARAMS_ERROR, "Project path is not a directory");
        log.info("Packaging project for download: {} -> {}.zip", projectPath, downloadFileName);
        // Set response headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/zip");
        response.addHeader("Content-Disposition",
                String.format("attachment; filename=\"%s.zip\"", downloadFileName));
        // File filter
        FileFilter filter = file -> isPathAllowed(projectDir.toPath(), file.toPath());
        // Compress
        try {
            // Use Hutool's ZipUtil to stream the filtered directory directly to the response
            ZipUtil.zip(response.getOutputStream(), StandardCharsets.UTF_8, false, filter, projectDir);
            log.info("Project packaged for download: {} -> {}.zip", projectPath, downloadFileName);
        } catch (IOException e) {
            log.error("Failed to package project for download", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to package project for download");
        }
    }

    /**
     * Whether the given path may be included in the archive.
     *
     * @param projectRoot project root directory
     * @param fullPath    absolute path
     * @return true if allowed
     */
    private boolean isPathAllowed(Path projectRoot, Path fullPath) {
        // Get the relative path
        Path relativePath = projectRoot.relativize(fullPath);
        // Check every component
        for (Path part : relativePath) {
            String partName = part.toString();
            // Check the ignored-names list
            if (IGNORED_NAMES.contains(partName)) {
                return false;
            }
            // Check ignored extensions
            if (IGNORED_EXTENSIONS.stream().anyMatch(ext -> partName.toLowerCase().endsWith(ext))) {
                return false;
            }
        }
        return true;
    }
}
