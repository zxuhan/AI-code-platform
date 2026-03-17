package com.zxuhan.aicode.ai.tools;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Tool manager.
 * Holds all tools and exposes lookup by name.
 */
@Slf4j
@Component
public class ToolManager {

    /**
     * Tool name to tool instance map.
     */
    private final Map<String, BaseTool> toolMap = new HashMap<>();

    /**
     * Auto-injected list of all tools.
     */
    @Resource
    private BaseTool[] tools;

    /**
     * Build the tool map at startup.
     */
    @PostConstruct
    public void initTools() {
        for (BaseTool tool : tools) {
            toolMap.put(tool.getToolName(), tool);
            log.info("Registered tool: {} -> {}", tool.getToolName(), tool.getDisplayName());
        }
        log.info("Tool manager initialized; {} tools registered", toolMap.size());
    }


    /**
     * Look up a tool by name.
     *
     * @param toolName programmatic tool name
     * @return tool instance
     */
    public BaseTool getTool(String toolName) {
        return toolMap.get(toolName);
    }

    /**
     * All registered tools.
     *
     * @return tool array
     */
    public BaseTool[] getAllTools() {
        return tools;
    }
}
