package com.zxuhan.aicode.ai.tools;

import cn.hutool.json.JSONObject;

/**
 * Base class for AI tools.
 * Defines the common interface every tool implements.
 */
public abstract class BaseTool {

    /**
     * Programmatic tool name (matches the underlying method).
     *
     * @return tool name
     */
    public abstract String getToolName();

    /**
     * Human-readable display name.
     *
     * @return display name
     */
    public abstract String getDisplayName();

    /**
     * Build the message shown to the user when the tool is requested.
     *
     * @return tool-request message
     */
    public String generateToolRequestResponse() {
        return String.format("\n\n[Selected tool] %s\n\n", getDisplayName());
    }

    /**
     * Build the formatted output stored to the database after execution.
     *
     * @param arguments tool invocation arguments
     * @return formatted execution result
     */
    public abstract String generateToolExecutedResult(JSONObject arguments);
}