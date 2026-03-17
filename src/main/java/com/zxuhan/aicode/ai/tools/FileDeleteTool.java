package com.zxuhan.aicode.ai.tools;

import cn.hutool.json.JSONObject;
import com.zxuhan.aicode.constant.AppConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * File-delete tool.
 * Lets the AI delete files via tool calls.
 */
@Slf4j
@Component
public class FileDeleteTool extends BaseTool {

    @Tool("Delete the file at the given path")
    public String deleteFile(
            @P("Relative file path")
            String relativeFilePath,
            @ToolMemoryId Long appId
    ) {
        try {
            Path path = Paths.get(relativeFilePath);
            if (!path.isAbsolute()) {
                String projectDirName = "vue_project_" + appId;
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                path = projectRoot.resolve(relativeFilePath);
            }
            if (!Files.exists(path)) {
                return "Warning: file does not exist; nothing to delete - " + relativeFilePath;
            }
            if (!Files.isRegularFile(path)) {
                return "Error: path is not a regular file; cannot delete - " + relativeFilePath;
            }
            // Safety check: do not allow deleting critical files
            String fileName = path.getFileName().toString();
            if (isImportantFile(fileName)) {
                return "Error: deletion of critical file is not allowed - " + fileName;
            }
            Files.delete(path);
            log.info("Successfully deleted file: {}", path.toAbsolutePath());
            return "File deleted successfully: " + relativeFilePath;
        } catch (IOException e) {
            String errorMessage = "Delete file failed: " + relativeFilePath + ", error: " + e.getMessage();
            log.error(errorMessage, e);
            return errorMessage;
        }
    }

    /**
     * Whether the given file is considered critical and must not be deleted.
     */
    private boolean isImportantFile(String fileName) {
        String[] importantFiles = {
                "package.json", "package-lock.json", "yarn.lock", "pnpm-lock.yaml",
                "vite.config.js", "vite.config.ts", "vue.config.js",
                "tsconfig.json", "tsconfig.app.json", "tsconfig.node.json",
                "index.html", "main.js", "main.ts", "App.vue", ".gitignore", "README.md"
        };
        for (String important : importantFiles) {
            if (important.equalsIgnoreCase(fileName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getToolName() {
        return "deleteFile";
    }

    @Override
    public String getDisplayName() {
        return "Delete file";
    }

    @Override
    public String generateToolExecutedResult(JSONObject arguments) {
        String relativeFilePath = arguments.getStr("relativeFilePath");
        return String.format(" [Tool call] %s %s", getDisplayName(), relativeFilePath);
    }
}
