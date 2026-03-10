package com.zxuhan.aicode.manager;

import com.zxuhan.aicode.config.R2ClientConfig;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;

/**
 * Cloudflare R2 object storage manager.
 */
@Component
@Slf4j
public class R2Manager {

    @Resource
    private R2ClientConfig r2ClientConfig;

    @Resource
    private S3Client s3Client;

    /**
     * Upload a file to R2 and return its public URL.
     *
     * @param key  object key (leading slash is stripped)
     * @param file file to upload
     * @return public URL of the uploaded file, or null on failure
     */
    public String uploadFile(String key, File file) {
        String objectKey = key.startsWith("/") ? key.substring(1) : key;
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(r2ClientConfig.getBucket())
                    .key(objectKey)
                    .build();
            PutObjectResponse response = s3Client.putObject(request, RequestBody.fromFile(file));
            if (response.sdkHttpResponse().isSuccessful()) {
                String url = String.format("%s/%s", trimTrailingSlash(r2ClientConfig.getPublicUrl()), objectKey);
                log.info("File uploaded to R2: {} -> {}", file.getName(), url);
                return url;
            }
            log.error("Failed to upload file to R2: {}, status: {}", file.getName(),
                    response.sdkHttpResponse().statusCode());
            return null;
        } catch (Exception e) {
            log.error("Failed to upload file to R2: {}", file.getName(), e);
            return null;
        }
    }

    private static String trimTrailingSlash(String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
