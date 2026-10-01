/*
 * Copyright (c) 2026 爱Ai (AiAi)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.chat.renderer

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import java.util.regex.Pattern

/**
 * 代码高亮器
 *
 * 支持20+编程语言的语法高亮，基于正则表达式匹配关键字、字符串、注释等。
 * 生成带颜色Span的SpannableStringBuilder。
 */
class CodeHighlighter {

    companion object {
        // 关键字颜色
        private const val COLOR_KEYWORD = 0xFFFF7B71.toInt()
        private const val COLOR_STRING = 0xFFA5D6A7.toInt()
        private const val COLOR_COMMENT = 0xFF78909C.toInt()
        private const val COLOR_NUMBER = 0xFFFFCA28.toInt()
        private const val COLOR_FUNCTION = 0xFF82B1FF.toInt()
        private const val COLOR_OPERATOR = 0xFFCE93D8.toInt()
    }

    private val languageKeywords: Map<String, Set<String>> = mapOf(
        "kotlin" to setOf(
            "fun", "val", "var", "if", "else", "for", "while", "do", "return",
            "class", "interface", "object", "package", "import", "null", "true",
            "false", "this", "super", "is", "as", "in", "when", "try", "catch",
            "finally", "throw", "data", "sealed", "override", "private", "public",
            "protected", "internal", "companion", "suspend", "lazy", "by"
        ),
        "java" to setOf(
            "public", "private", "protected", "class", "interface", "extends",
            "implements", "void", "int", "long", "double", "float", "boolean",
            "string", "if", "else", "for", "while", "do", "return", "new",
            "this", "super", "null", "true", "false", "try", "catch", "finally",
            "throw", "throws", "static", "final", "abstract", "enum", "import",
            "package", "byte", "short", "char"
        ),
        "python" to setOf(
            "def", "class", "if", "elif", "else", "for", "while", "return",
            "import", "from", "as", "with", "try", "except", "finally", "raise",
            "lambda", "pass", "break", "continue", "and", "or", "not", "in",
            "is", "None", "True", "False", "print", "self", "yield", "global"
        ),
        "javascript" to setOf(
            "function", "var", "let", "const", "if", "else", "for", "while",
            "return", "class", "extends", "import", "export", "from", "new",
            "this", "null", "undefined", "true", "false", "try", "catch",
            "finally", "throw", "typeof", "instanceof", "async", "await", "of"
        ),
        "typescript" to setOf(
            "function", "var", "let", "const", "if", "else", "for", "while",
            "return", "class", "extends", "import", "export", "from", "new",
            "this", "null", "undefined", "true", "false", "try", "catch",
            "finally", "throw", "typeof", "interface", "type", "enum", "implements",
            "public", "private", "protected", "readonly", "abstract"
        ),
        "go" to setOf(
            "func", "var", "const", "package", "import", "if", "else", "for",
            "range", "return", "go", "defer", "chan", "select", "switch",
            "case", "default", "break", "continue", "struct", "interface",
            "map", "slice", "make", "new", "nil", "true", "false"
        ),
        "rust" to setOf(
            "fn", "let", "mut", "const", "static", "if", "else", "for", "while",
            "loop", "return", "impl", "trait", "struct", "enum", "match",
            "mod", "use", "pub", "crate", "self", "Self", "ref", "move",
            "box", "vec", "String", "Some", "None", "Ok", "Err"
        ),
        "c" to setOf(
            "int", "char", "float", "double", "void", "if", "else", "for",
            "while", "do", "return", "struct", "union", "typedef", "define",
            "include", "static", "const", "extern", "sizeof", "switch", "case",
            "break", "continue", "goto", "unsigned", "signed", "long", "short"
        ),
        "cpp" to setOf(
            "int", "char", "float", "double", "void", "if", "else", "for",
            "while", "do", "return", "class", "public", "private", "protected",
            "virtual", "override", "template", "typename", "namespace", "using",
            "include", "define", "typedef", "struct", "enum", "try", "catch",
            "throw", "new", "delete", "this", "nullptr", "auto"
        ),
        "sql" to setOf(
            "SELECT", "FROM", "WHERE", "INSERT", "INTO", "VALUES", "UPDATE",
            "SET", "DELETE", "CREATE", "TABLE", "DROP", "ALTER", "ADD", "COLUMN",
            "JOIN", "LEFT", "RIGHT", "INNER", "OUTER", "ON", "GROUP", "BY",
            "ORDER", "HAVING", "LIMIT", "OFFSET", "DISTINCT", "AS", "AND", "OR",
            "NOT", "NULL", "IS", "IN", "BETWEEN", "LIKE", "CASE", "WHEN", "THEN",
            "ELSE", "END", "UNION", "ALL", "INDEX", "VIEW"
        ),
        "html" to setOf(
            "html", "head", "body", "div", "span", "p", "a", "img", "script",
            "style", "link", "meta", "title", "h1", "h2", "h3", "ul", "ol",
            "li", "table", "tr", "td", "th", "form", "input", "button", "label"
        ),
        "css" to setOf(
            "color", "background", "margin", "padding", "border", "display",
            "flex", "grid", "position", "absolute", "relative", "fixed", "static",
            "width", "height", "max-width", "min-height", "font-size", "font-weight",
            "text-align", "justify-content", "align-items", "transition", "animation"
        ),
        "json" to setOf(
            "true", "false", "null"
        ),
        "xml" to setOf(
            "xml", "version", "encoding", "manifest", "application", "activity",
            "service", "receiver", "provider", "permission", "uses-permission"
        ),
        "shell" to setOf(
            "if", "then", "else", "fi", "for", "while", "do", "done", "case",
            "esac", "function", "return", "export", "local", "echo", "cd", "ls",
            "pwd", "mkdir", "rm", "cp", "mv", "chmod", "chown", "grep", "sed",
            "awk", "cat", "sudo"
        ),
        "yaml" to setOf(
            "true", "false", "null", "yes", "no", "on", "off"
        ),
        "swift" to setOf(
            "func", "var", "let", "if", "else", "for", "while", "return",
            "class", "struct", "enum", "protocol", "extension", "import",
            "public", "private", "internal", "fileprivate", "open", "guard",
            "defer", "switch", "case", "default", "break", "continue", "inout",
            "nil", "self", "Self", "throws", "catch", "try", "async", "await"
        ),
        "php" to setOf(
            "function", "public", "private", "protected", "class", "extends",
            "implements", "if", "else", "elseif", "for", "foreach", "while",
            "return", "echo", "print", "new", "this", "null", "true", "false",
            "array", "string", "int", "float", "bool", "var", "const", "use",
            "namespace", "require", "include", "as"
        ),
        "ruby" to setOf(
            "def", "end", "if", "elsif", "else", "unless", "for", "while",
            "until", "do", "return", "class", "module", "include", "extend",
            "attr_accessor", "attr_reader", "attr_writer", "nil", "true",
            "false", "self", "yield", "begin", "rescue", "ensure", "raise"
        ),
        "dart" to setOf(
            "void", "var", "final", "const", "dynamic", "Object", "String",
            "int", "double", "bool", "List", "Map", "Set", "if", "else",
            "for", "while", "do", "return", "class", "extends", "implements",
            "with", "mixin", "abstract", "factory", "async", "await", "yield"
        )
    )

    /**
     * 高亮代码
     *
     * @param code 源代码
     * @param language 编程语言
     * @return 带颜色Span的Spannable
     */
    fun highlight(code: String, language: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(code)
        val keywords = languageKeywords[language.lowercase()] ?: emptySet()

        // 高亮注释（单行 // 或 # 或 --）
        highlightComments(builder, code, language)

        // 高亮字符串
        highlightStrings(builder, code)

        // 高亮数字
        highlightNumbers(builder, code)

        // 高亮关键字
        highlightKeywords(builder, code, keywords)

        return builder
    }

    private fun highlightComments(builder: SpannableStringBuilder, code: String, language: String) {
        val commentPattern = when (language.lowercase()) {
            "python", "ruby", "shell", "yaml" -> Pattern.compile("#.*$", Pattern.MULTILINE)
            "sql" -> Pattern.compile("--.*$", Pattern.MULTILINE)
            else -> Pattern.compile("//.*$", Pattern.MULTILINE)
        }
        val matcher = commentPattern.matcher(code)
        while (matcher.find()) {
            builder.setSpan(
                ForegroundColorSpan(COLOR_COMMENT),
                matcher.start(), matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun highlightStrings(builder: SpannableStringBuilder, code: String) {
        val stringPattern = Pattern.compile("\"([^\"\\\\]|\\\\.)*\"|'([^'\\\\]|\\\\.)*'")
        val matcher = stringPattern.matcher(code)
        while (matcher.find()) {
            builder.setSpan(
                ForegroundColorSpan(COLOR_STRING),
                matcher.start(), matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun highlightNumbers(builder: SpannableStringBuilder, code: String) {
        val numberPattern = Pattern.compile("\\b\\d+(\\.\\d+)?[fFlL]?\\b")
        val matcher = numberPattern.matcher(code)
        while (matcher.find()) {
            builder.setSpan(
                ForegroundColorSpan(COLOR_NUMBER),
                matcher.start(), matcher.end(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun highlightKeywords(builder: SpannableStringBuilder, code: String, keywords: Set<String>) {
        for (keyword in keywords) {
            val pattern = Pattern.compile("\\b${Pattern.quote(keyword)}\\b")
            val matcher = pattern.matcher(code)
            while (matcher.find()) {
                builder.setSpan(
                    ForegroundColorSpan(COLOR_KEYWORD),
                    matcher.start(), matcher.end(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    /**
     * 获取支持的语言列表
     */
    fun getSupportedLanguages(): Set<String> {
        return languageKeywords.keys
    }
}
