package com.zxuhan.aicode.utils;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.zxuhan.aicode.exception.BusinessException;
import com.zxuhan.aicode.exception.ErrorCode;
import io.github.bonigarcia.wdm.WebDriverManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.UUID;

/**
 * Web screenshot utilities.
 */
@Slf4j
public class WebScreenshotUtils {

    private static final WebDriver webDriver;

    // Initialize the driver once at class load time to avoid repeated startups
    static {
        final int DEFAULT_WIDTH = 1600;
        final int DEFAULT_HEIGHT = 900;
        webDriver = initChromeDriver(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Destroy the driver on shutdown.
     */
    @PreDestroy
    public void destroy() {
        webDriver.quit();
    }

    /**
     * Take a screenshot of a web page.
     *
     * @param webUrl URL to capture
     * @return path to the compressed screenshot file, or null on failure
     */
    public static String saveWebPageScreenshot(String webUrl) {
        // Validate input
        if (StrUtil.isBlank(webUrl)) {
            log.error("Web screenshot failed: url is empty");
            return null;
        }
        // Create temporary directory
        try {
            String rootPath = System.getProperty("user.dir") + "/tmp/screenshots/" + UUID.randomUUID().toString().substring(0, 8);
            FileUtil.mkdir(rootPath);
            // Image suffix
            final String IMAGE_SUFFIX = ".png";
            // Original image save path
            String imageSavePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + IMAGE_SUFFIX;
            // Visit the page
            webDriver.get(webUrl);
            // Wait for the page to load
            waitForPageLoad(webDriver);
            // Take screenshot
            byte[] screenshotBytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            // Save the raw screenshot
            saveImage(screenshotBytes, imageSavePath);
            log.info("Original screenshot saved: {}", imageSavePath);
            // Compress the image
            final String COMPRESS_SUFFIX = "_compressed.jpg";
            String compressedImagePath = rootPath + File.separator + RandomUtil.randomNumbers(5) + COMPRESS_SUFFIX;
            compressImage(imageSavePath, compressedImagePath);
            log.info("Compressed image saved: {}", compressedImagePath);
            // Delete the original file
            FileUtil.del(imageSavePath);
            return compressedImagePath;
        } catch (Exception e) {
            log.error("Web screenshot failed: {}", webUrl, e);
            return null;
        }
    }

    /**
     * Initialize the Chrome WebDriver.
     *
     * In Docker (Alpine), the image bundles chromium + chromedriver via apk;
     * CHROME_BIN and CHROMEDRIVER_PATH point at them. WebDriverManager would
     * otherwise download a glibc-linked chromedriver that won't run on Alpine.
     * In native dev, both vars are unset and WebDriverManager auto-downloads.
     */
    private static WebDriver initChromeDriver(int width, int height) {
        try {
            String chromeBin = System.getenv("CHROME_BIN");
            String chromedriverPath = System.getenv("CHROMEDRIVER_PATH");

            if (chromedriverPath != null && !chromedriverPath.isBlank()) {
                System.setProperty("webdriver.chrome.driver", chromedriverPath);
            } else {
                WebDriverManager.chromedriver().setup();
            }

            ChromeOptions options = new ChromeOptions();
            if (chromeBin != null && !chromeBin.isBlank()) {
                options.setBinary(chromeBin);
            }
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments(String.format("--window-size=%d,%d", width, height));
            options.addArguments("--disable-extensions");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            WebDriver driver = new ChromeDriver(options);
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            return driver;
        } catch (Exception e) {
            log.error("Failed to initialize Chrome browser", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to initialize Chrome browser");
        }
    }

    /**
     * Save image bytes to a file.
     *
     * @param imageBytes raw image bytes
     * @param imagePath  destination path
     */
    private static void saveImage(byte[] imageBytes, String imagePath) {
        try {
            FileUtil.writeBytes(imageBytes, imagePath);
        } catch (Exception e) {
            log.error("Failed to save image: {}", imagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to save image");
        }
    }

    /**
     * Compress an image.
     *
     * @param originImagePath     source image path
     * @param compressedImagePath destination compressed image path
     */
    private static void compressImage(String originImagePath, String compressedImagePath) {
        // Compression quality (0.1 = 10%)
        final float COMPRESSION_QUALITY = 0.3f;
        try {
            ImgUtil.compress(
                    FileUtil.file(originImagePath),
                    FileUtil.file(compressedImagePath),
                    COMPRESSION_QUALITY
            );
        } catch (Exception e) {
            log.error("Failed to compress image: {} -> {}", originImagePath, compressedImagePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to compress image");
        }
    }

    /**
     * Wait for the page to finish loading.
     *
     * @param webDriver the WebDriver instance
     */
    private static void waitForPageLoad(WebDriver webDriver) {
        try {
            // Create wait object
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            // Wait for document.readyState to be "complete"
            wait.until(driver -> ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState").
                    equals("complete")
            );
            // Wait a bit more so dynamic content can finish loading
            Thread.sleep(2000);
            log.info("Page load complete");
        } catch (Exception e) {
            log.error("Exception while waiting for page load; continuing with screenshot", e);
        }
    }
}
