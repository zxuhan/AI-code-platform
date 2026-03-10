package com.zxuhan.aicode.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

/**
 * Cloudflare R2 (S3-compatible) configuration.
 */
@Configuration
@ConfigurationProperties(prefix = "r2.client")
@Data
public class R2ClientConfig {

    /**
     * R2 endpoint, e.g. https://{account-id}.r2.cloudflarestorage.com
     */
    private String endpoint;

    /**
     * R2 API token access key id.
     */
    private String accessKeyId;

    /**
     * R2 API token secret access key (keep confidential).
     */
    private String secretAccessKey;

    /**
     * Bucket name.
     */
    private String bucket;

    /**
     * Public base URL used to build returned object URLs
     * (custom domain or bucket public r2.dev URL).
     */
    private String publicUrl;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create(endpoint))
                // R2 ignores region but the SDK requires one
                .region(Region.of("auto"))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }
}
