package com.zxuhan.aicodebackend.core.parser;

/**
 * Code parser interface
 *
 */
public interface CodeParser<T> {

    /**
     * Parse code content
     *
     * @param codeContent original code content
     * @return parsed result object
     */
    T parseCode(String codeContent);
}