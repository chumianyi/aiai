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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 数据导出工具。
 *
 * 支持将聊天记录导出为Markdown、PDF、HTML等格式。
 */
class DataExporter {

    companion object {
        private const val TAG = "DataExporter"
    }

    /**
     * 导出消息为Markdown格式。
     *
     * @param messages 消息列表
     * @return Markdown字符串
     */
    fun exportToMarkdown(messages: List<MarkdownMessage>): String {
        val sb = StringBuilder()
        sb.appendLine("# 聊天记录导出")
        sb.appendLine()
        sb.appendLine("导出时间：${DataUtils.formatTime(System.currentTimeMillis())}")
        sb.appendLine()
        sb.appendLine("---")
        sb.appendLine()

        messages.forEach { msg ->
            val roleLabel = when (msg.role) {
                "user" -> "🧑 用户"
                "assistant" -> "🤖 AI助手"
                "system" -> "⚙️ 系统"
                else -> msg.role
            }
            sb.appendLine("### $roleLabel")
            sb.appendLine()
            sb.appendLine(msg.content)
            sb.appendLine()
            sb.appendLine("*时间：${DataUtils.formatTime(msg.timestamp)}*")
            sb.appendLine()
            sb.appendLine("---")
            sb.appendLine()
        }

        return sb.toString()
    }

    /**
     * 导出消息为纯文本格式。
     *
     * @param messages 消息列表
     * @return 纯文本字符串
     */
    fun exportToPlainText(messages: List<MarkdownMessage>): String {
        val sb = StringBuilder()
        messages.forEach { msg ->
            sb.appendLine("[${DataUtils.formatTime(msg.timestamp)}] ${msg.role}:")
            sb.appendLine(msg.content)
            sb.appendLine()
        }
        return sb.toString()
    }

    /**
     * 导出消息为HTML格式。
     *
     * @param messages 消息列表
     * @param title 文档标题
     * @return HTML字符串
     */
    fun exportToHtml(messages: List<MarkdownMessage>, title: String = "聊天记录"): String {
        val sb = StringBuilder()
        sb.appendLine("<!DOCTYPE html>")
        sb.appendLine("<html lang=\"zh-CN\">")
        sb.appendLine("<head>")
        sb.appendLine("<meta charset=\"UTF-8\">")
        sb.appendLine("<title>$title</title>")
        sb.appendLine("<style>body{font-family:sans-serif;max-width:800px;margin:0 auto;padding:20px;}")
        sb.appendLine(".message{margin:10px 0;padding:10px;border-radius:8px;}")
        sb.appendLine(".user{background:#e3f2fd;}.assistant{background:#f5f5f5;}</style>")
        sb.appendLine("</head><body>")
        sb.appendLine("<h1>$title</h1>")

        messages.forEach { msg ->
            val cssClass = when (msg.role) {
                "user" -> "user"
                "assistant" -> "assistant"
                else -> ""
            }
            sb.appendLine("<div class=\"message $cssClass\">")
            sb.appendLine("<p><strong>${msg.role}</strong> - ${DataUtils.formatTime(msg.timestamp)}</p>")
            sb.appendLine("<p>${msg.content}</p>")
            sb.appendLine("</div>")
        }

        sb.appendLine("</body></html>")
        return sb.toString()
    }

    /**
     * Markdown导出用的消息数据类。
     */
    data class MarkdownMessage(
        val role: String,
        val content: String,
        val timestamp: Long,
    )
}
