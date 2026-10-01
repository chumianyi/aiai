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
package com.aiai.chat.manager

import android.content.Context
import com.aiai.chat.data.model.ChatMessage
import com.aiai.chat.data.enums.MessageRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 对话导出管理器
 *
 * 支持将对话导出为多种格式：Markdown、TXT、PDF、HTML。
 */
class ExportManager(private val context: Context) {

    enum class ExportFormat(val displayName: String, val extension: String) {
        MARKDOWN("Markdown", "md"),
        TEXT("纯文本", "txt"),
        HTML("HTML网页", "html"),
        JSON("JSON数据", "json")
    }

    data class ExportResult(
        val success: Boolean,
        val filePath: String? = null,
        val error: String? = null
    )

    private val _exporting = MutableStateFlow(false)
    val exporting: StateFlow<Boolean> = _exporting.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress: StateFlow<Float> = _progress.asStateFlow()

    /**
     * 导出会话为Markdown格式
     */
    fun exportAsMarkdown(conversationTitle: String, messages: List<ChatMessage>): String {
        val sb = StringBuilder()
        sb.append("# $conversationTitle\n\n")
        sb.append("> 导出时间: ${System.currentTimeMillis()}\n\n")
        sb.append("---\n\n")

        messages.forEach { message ->
            when (message.role) {
                MessageRole.USER -> {
                    sb.append("## 🧑 用户\n\n")
                    sb.append("${message.content}\n\n")
                }
                MessageRole.ASSISTANT -> {
                    sb.append("## 🤖 AI助手\n\n")
                    sb.append("${message.content}\n\n")
                }
                MessageRole.SYSTEM -> {
                    sb.append("### ⚙️ 系统\n\n")
                    sb.append("${message.content}\n\n")
                }
            }
        }
        return sb.toString()
    }

    /**
     * 导出会话为纯文本格式
     */
    fun exportAsText(conversationTitle: String, messages: List<ChatMessage>): String {
        val sb = StringBuilder()
        sb.append("=== $conversationTitle ===\n\n")

        messages.forEach { message ->
            val roleName = when (message.role) {
                MessageRole.USER -> "用户"
                MessageRole.ASSISTANT -> "AI助手"
                MessageRole.SYSTEM -> "系统"
            }
            sb.append("[$roleName]\n")
            sb.append("${message.content}\n\n")
        }
        return sb.toString()
    }

    /**
     * 导出会话为HTML格式
     */
    fun exportAsHtml(conversationTitle: String, messages: List<ChatMessage>): String {
        val sb = StringBuilder()
        sb.append("<html><head><meta charset='utf-8'>")
        sb.append("<title>$conversationTitle</title>")
        sb.append("<style>body{font-family:sans-serif;max-width:800px;margin:40px auto;padding:20px;}")
        sb.append(".user{background:#e3f2fd;padding:12px;border-radius:8px;margin:8px 0;}")
        sb.append(".assistant{background:#f5f5f5;padding:12px;border-radius:8px;margin:8px 0;}")
        sb.append("</style></head><body>")
        sb.append("<h1>$conversationTitle</h1>")

        messages.forEach { message ->
            val cssClass = when (message.role) {
                MessageRole.USER -> "user"
                MessageRole.ASSISTANT -> "assistant"
                MessageRole.SYSTEM -> "assistant"
            }
            sb.append("<div class='$cssClass'>${message.content}</div>")
        }

        sb.append("</body></html>")
        return sb.toString()
    }

    /**
     * 导出会话为JSON格式
     */
    fun exportAsJson(conversationTitle: String, messages: List<ChatMessage>): String {
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"title\": \"$conversationTitle\",\n")
        sb.append("  \"exportTime\": ${System.currentTimeMillis()},\n")
        sb.append("  \"messages\": [\n")

        messages.forEachIndexed { index, message ->
            sb.append("    {\n")
            sb.append("      \"role\": \"${message.role.name}\",\n")
            sb.append("      \"content\": \"${message.content.replace("\"", "\\\"")}\",\n")
            sb.append("      \"timestamp\": ${message.timestamp}\n")
            sb.append("    }")
            if (index < messages.size - 1) sb.append(",")
            sb.append("\n")
        }

        sb.append("  ]\n}")
        return sb.toString()
    }

    /**
     * 根据格式导出
     */
    fun export(format: ExportFormat, conversationTitle: String, messages: List<ChatMessage>): String {
        return when (format) {
            ExportFormat.MARKDOWN -> exportAsMarkdown(conversationTitle, messages)
            ExportFormat.TEXT -> exportAsText(conversationTitle, messages)
            ExportFormat.HTML -> exportAsHtml(conversationTitle, messages)
            ExportFormat.JSON -> exportAsJson(conversationTitle, messages)
        }
    }
}
