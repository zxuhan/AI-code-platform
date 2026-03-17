package com.zxuhan.aicode.core.builder;

import cn.hutool.core.util.RuntimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Vue project builder.
 */
@Slf4j
@Component
public class VueProjectBuilder {

    /**
     * Build a Vue project asynchronously.
     *
     * @param projectPath project path
     */
    public void buildProjectAsync(String projectPath) {
        Thread.ofVirtual().name("vue-builder-" + System.currentTimeMillis())
                .start(() -> {
                    try {
                        buildProject(projectPath);
                    } catch (Exception e) {
                        log.error("Exception while asynchronously building Vue project: {}", e.getMessage(), e);
                    }
                });
    }

    /**
     * Build a Vue project.
     *
     * @param projectPath project root directory
     * @return true if the build succeeded
     */
    public boolean buildProject(String projectPath) {
        File projectDir = new File(projectPath);
        if (!projectDir.exists() || !projectDir.isDirectory()) {
            log.error("Project directory does not exist: {}", projectPath);
            return false;
        }
        // Verify package.json exists
        File packageJsonFile = new File(projectDir, "package.json");
        if (!packageJsonFile.exists()) {
            log.error("No package.json file in project directory: {}", projectPath);
            return false;
        }
        log.info("Building Vue project: {}", projectPath);
        // npm install
        if (!executeNpmInstall(projectDir)) {
            log.error("npm install failed: {}", projectPath);
            return false;
        }
        // npm run build
        if (!executeNpmBuild(projectDir)) {
            log.error("npm run build failed: {}", projectPath);
            return false;
        }
        // Verify the dist directory was produced
        File distDir = new File(projectDir, "dist");
        if (!distDir.exists() || !distDir.isDirectory()) {
            log.error("Build complete but dist directory was not produced: {}", projectPath);
            return false;
        }
        log.info("Vue project built successfully, dist directory: {}", projectPath);
        return true;
    }

    /**
     * Run npm install.
     */
    private boolean executeNpmInstall(File projectDir) {
        log.info("Running npm install...");
        String command = String.format("%s install", buildCommand("npm"));
        return executeCommand(projectDir, command, 300); // 5 minute timeout
    }

    /**
     * Run npm run build.
     */
    private boolean executeNpmBuild(File projectDir) {
        log.info("Running npm run build...");
        String command = String.format("%s run build", buildCommand("npm"));
        return executeCommand(projectDir, command, 180); // 3 minute timeout
    }

    /**
     * Build the command for the current OS.
     *
     * @param baseCommand the base command name
     * @return the OS-specific command string
     */
    private String buildCommand(String baseCommand) {
        if (isWindows()) {
            return baseCommand + ".cmd";
        }
        return baseCommand;
    }

    /**
     * Whether the current OS is Windows.
     *
     * @return true on Windows
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    /**
     * Execute a command.
     *
     * @param workingDir     working directory
     * @param command        command string
     * @param timeoutSeconds timeout in seconds
     * @return true if the command succeeded
     */
    private boolean executeCommand(File workingDir, String command, int timeoutSeconds) {
        try {
            log.info("Executing command in {}: {}", workingDir.getAbsolutePath(), command);
            Process process = RuntimeUtil.exec(
                    null,
                    workingDir,
                    command.split("\\s+") // Split command into arguments
            );
            // Wait for completion with a timeout
            boolean finished = process.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                log.error("Command timed out after {}s; force-killing process", timeoutSeconds);
                process.destroyForcibly();
                return false;
            }
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("Command succeeded: {}", command);
                return true;
            } else {
                log.error("Command failed with exit code: {}", exitCode);
                return false;
            }
        } catch (Exception e) {
            log.error("Failed to execute command: {}, error: {}", command, e.getMessage());
            return false;
        }
    }

}
