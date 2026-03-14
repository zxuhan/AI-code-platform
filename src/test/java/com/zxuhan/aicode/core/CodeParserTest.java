package com.zxuhan.aicode.core;

import com.zxuhan.aicode.ai.model.HtmlCodeResult;
import com.zxuhan.aicode.ai.model.MultiFileCodeResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CodeParserTest {

    @Test
    void parseHtmlCode() {
        String codeContent = """
                Some arbitrary description:
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Test page</title>
                </head>
                <body>
                    <h1>Hello World!</h1>
                </body>
                </html>
                ```
                Some arbitrary description
                """;
        HtmlCodeResult result = CodeParser.parseHtmlCode(codeContent);
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
    }

    @Test
    void parseMultiFileCode() {
        String codeContent = """
                Build a full web page:
                ```html
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Multi-file example</title>
                    <link rel="stylesheet" href="style.css">
                </head>
                <body>
                    <h1>Welcome</h1>
                    <script src="script.js"></script>
                </body>
                </html>
                ```
                ```css
                h1 {
                    color: blue;
                    text-align: center;
                }
                ```
                ```js
                console.log('Page loaded');
                ```
                Files created.
                """;
        MultiFileCodeResult result = CodeParser.parseMultiFileCode(codeContent);
        assertNotNull(result);
        assertNotNull(result.getHtmlCode());
        assertNotNull(result.getCssCode());
        assertNotNull(result.getJsCode());
    }
}
