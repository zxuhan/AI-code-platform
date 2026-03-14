package com.zxuhan.aicode.core.parser;

/**
 * Strategy interface for code parsers.
 */
public interface CodeParser<T> {

    /**
     * Parse the given code content.
     *
     * @param codeContent raw code
     * @return parsed result
     */
    T parseCode(String codeContent);
}
