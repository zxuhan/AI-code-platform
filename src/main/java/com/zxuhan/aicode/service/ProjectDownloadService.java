package com.zxuhan.aicode.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ProjectDownloadService {

    /**
     * Download a project as a zip archive.
     *
     * @param projectPath      project source directory
     * @param downloadFileName desired download file name
     * @param response         HTTP response
     */
    void downloadProjectAsZip(String projectPath, String downloadFileName, HttpServletResponse response);
}
