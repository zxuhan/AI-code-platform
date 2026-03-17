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
import java.nio.file.StandardOpenOption;

/**
 * File-modify tool.
 * Lets the AI replace text inside a file via tool calls.
 */
@Slf4j
@Component
public class FileModifyTool extends BaseTool {

    @Tool("Modify file contents by replacing the given old content with new content")
    public String modifyFile(
            @P("Relative file path")
            String relativeFilePath,
            @P("Old content to be replaced")
            String oldContent,
            @P("New content used as the replacement")
            String newContent,
            @ToolMemoryId Long appId
    ) {
        try {
            Path path = Paths.get(relativeFilePath);
            if (!path.isAbsolute()) {
                String projectDirName = "vue_project_" + appId;
                Path projectRoot = Paths.get(AppConstant.CODE_OUTPUT_ROOT_DIR, projectDirName);
                path = projectRoot.resolve(relativeFilePath);
            }
            if (!Files.exists(path) || !Files.isRegularFile(path)) {
                return "Error: file does not exist or is not a regular file - " + relativeFilePath;
            }
            String originalContent = Files.readString(path);
            if (!originalContent.contains(oldContent)) {
                return "Warning: old content not found; file unchanged - " + relativeFilePath;
            }
            String modifiedContent = originalContent.replace(oldContent, newContent);
            if (originalContent.equals(modifiedContent)) {
                return "Info: file content unchanged after replacement - " + relativeFilePath;
            }
            Files.writeString(path, modifiedContent, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            log.info("Successfully modified file: {}", path.toAbsolutePath());
            return "File modified successfully: " + relativeFilePath;
        } catch (IOException e) {
            String errorMessage = "Modify file failed: " + relativeFilePath + ", error: " + e.getMessage();
            log.error(errorMessage, e);
            return errorMessage;
        }
    }

    @Override
    public String getToolName() {
        return "modifyFile";
    }

    @Override
    public String getDisplayName() {
        return "Modify file";
    }

    @Override
    public String generateToolExecutedResult(JSONObject arguments) {
        String relativeFilePath = arguments.getStr("relativeFilePath");
        String oldContent = arguments.getStr("oldContent");
        String newContent = arguments.getStr("newContent");
        // Show before/after content
        return String.format("""
                [Tool call] %s %s

                Before:
                ```
                %s
                ```

                After:
                ```
                %s
                ```
                """, getDisplayName(), relativeFilePath, oldContent, newContent);
    }
}
