/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

/**
 * Markdown渲染工具。
 *
 * 提供Markdown文本解析和渲染辅助功能。
 */
object MarkdownHelper {

    private const val TAG = "MarkdownHelper"

    /**
     * 提取Markdown中的代码块。
     *
     * @param markdown Markdown文本
     * @return 代码块列表
     */
    fun extractCodeBlocks(markdown: String): List<CodeBlock> {
        val blocks = mutableListOf<CodeBlock>()
        val regex = Regex("```(\\w*)\\n([\\s\\S]*?)```")
        regex.findAll(markdown).forEach { match ->
            blocks.add(CodeBlock(
                language = match.groupValues[1],
                code = match.groupValues[2].trim(),
            ))
        }
        return blocks
    }

    /**
     * 提取Markdown中的链接。
     *
     * @param markdown Markdown文本
     * @return 链接列表
     */
    fun extractLinks(markdown: String): List<MarkdownLink> {
        val links = mutableListOf<MarkdownLink>()
        val regex = Regex("\\[([^]]+)\\]\\(([^)]+)\\)")
        regex.findAll(markdown).forEach { match ->
            links.add(MarkdownLink(
                text = match.groupValues[1],
                url = match.groupValues[2],
            ))
        }
        return links
    }

    /**
     * 提取Markdown中的图片。
     *
     * @param markdown Markdown文本
     * @return 图片URL列表
     */
    fun extractImages(markdown: String): List<String> {
        val images = mutableListOf<String>()
        val regex = Regex("!\\[([^]]*)\\]\\(([^)]+)\\)")
        regex.findAll(markdown).forEach { match ->
            images.add(match.groupValues[2])
        }
        return images
    }

    /**
     * 统计Markdown标题层级。
     *
     * @param markdown Markdown文本
     * @return 标题层级统计
     */
    fun countHeadings(markdown: String): Map<Int, Int> {
        val counts = mutableMapOf<Int, Int>()
        val lines = markdown.lines()
        lines.forEach { line ->
            val match = Regex("^(#{1,6}) ").find(line)
            if (match != null) {
                val level = match.groupValues[1].length
                counts[level] = (counts[level] ?: 0) + 1
            }
        }
        return counts
    }

    /**
     * 估算Markdown渲染后的文本长度。
     *
     * @param markdown Markdown文本
     * @return 纯文本长度
     */
    fun estimatePlainTextLength(markdown: String): Int {
        var text = markdown
        // 移除代码块
        text = Regex("```[\\s\\S]*?```").replace(text, "")
        // 移除行内代码
        text = Regex("`[^`]*`").replace(text, "")
        // 移除链接
        text = Regex("\\[([^]]+)\\]\\([^)]+\\)").replace(text, "$1")
        // 移除图片
        text = Regex("!\\[[^]]*\\]\\([^)]+\\)").replace(text, "")
        // 移除标题标记
        text = Regex("^#{1,6} ").replace(text, "")
        // 移除粗体/斜体标记
        text = Regex("\\*+").replace(text, "")
        return text.trim().length
    }

    /**
     * 代码块数据类。
     */
    data class CodeBlock(
        val language: String,
        val code: String,
    )

    /**
     * Markdown链接数据类。
     */
    data class MarkdownLink(
        val text: String,
        val url: String,
    )
}
