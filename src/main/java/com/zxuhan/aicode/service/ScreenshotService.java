package com.zxuhan.aicode.service;

/**
 * Screenshot service.
 */
public interface ScreenshotService {


    /**
     * Take a screenshot of a web page and return the public URL of the uploaded image.
     *
     * @param webUrl URL to capture
     * @return uploaded image URL
     */
    String generateAndUploadScreenshot(String webUrl);

}
