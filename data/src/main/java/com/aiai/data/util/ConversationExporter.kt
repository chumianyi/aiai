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
import com.aiai.data.entity.MessageEntity
import com.aiai.data.entity.ConversationEntity
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 会话导出工具。
 *
 * 将会话及其消息导出为多种格式。
 */
class ConversationExporter {

    companion object {
        private const val TAG = "ConversationExporter"
    }

    /**
     * 导出会话为Markdown格式。
     *
     * @param conversation 会话实体
     * @param messages 消息列表
     * @return Markdown字符串
     */
    fun exportToMarkdown(conversation: ConversationEntity, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

        sb.appendLine("# ${conversation.title}")
        sb.appendLine()
        sb.appendLine("> 模型：${conversation.modelName}")
        sb.appendLine("> 消息数：${conversation.messageCount}")
        sb.appendLine("> 创建时间：${dateFormat.format(Date(conversation.createdAt))}")
        sb.appendLine("> 导出时间：${dateFormat.format(Date())}")
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
            sb.appendLine("**时间：** ${dateFormat.format(Date(msg.createdAt))}")
            sb.appendLine()
            sb.appendLine("**内容：**")
            sb.appendLine()
            sb.appendLine(msg.content)
            sb.appendLine()

            if (msg.tokenCount > 0) {
                sb.appendLine("*Token消耗：${msg.tokenCount}*")
                sb.appendLine()
            }
            sb.appendLine("---")
            sb.appendLine()
        }

        return sb.toString()
    }

    /**
     * 导出会话为JSON格式。
     *
     * @param conversation 会话实体
     * @param messages 消息列表
     * @return JSON字符串
     */
    fun exportToJson(conversation: ConversationEntity, messages: List<MessageEntity>): String {
        val convJson = JSONObject().apply {
            put("id", conversation.id)
            put("title", conversation.title)
            put("modelName", conversation.modelName)
            put("systemPrompt", conversation.systemPrompt)
            put("temperature", conversation.temperature)
            put("messageCount", conversation.messageCount)
            put("createdAt", conversation.createdAt)
            put("updatedAt", conversation.updatedAt)
        }

        val messagesArray = JSONArray()
        messages.forEach { msg ->
            val msgJson = JSONObject().apply {
                put("id", msg.id)
                put("role", msg.role)
                put("content", msg.content)
                put("messageType", msg.messageType)
                put("status", msg.status)
                put("createdAt", msg.createdAt)
                put("tokenCount", msg.tokenCount)
                put("modelName", msg.modelName)
            }
            messagesArray.put(msgJson)
        }

        return JSONObject().apply {
            put("conversation", convJson)
            put("messages", messagesArray)
            put("exportedAt", System.currentTimeMillis())
        }.toString(2)
    }

    /**
     * 导出会话为纯文本格式。
     *
     * @param conversation 会话实体
     * @param messages 消息列表
     * @return 纯文本字符串
     */
    fun exportToPlainText(conversation: ConversationEntity, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

        sb.appendLine("会话：${conversation.title}")
        sb.appendLine("模型：${conversation.modelName}")
        sb.appendLine("导出时间：${dateFormat.format(Date())}")
        sb.appendLine("========================================")
        sb.appendLine()

        messages.forEach { msg ->
            sb.appendLine("[${dateFormat.format(Date(msg.createdAt))}] ${msg.role}:")
            sb.appendLine(msg.content)
            sb.appendLine()
        }

        return sb.toString()
    }

    /**
     * 导出会话为HTML格式。
     *
     * @param conversation 会话实体
     * @param messages 消息列表
     * @return HTML字符串
     */
    fun exportToHtml(conversation: ConversationEntity, messages: List<MessageEntity>): String {
        val sb = StringBuilder()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

        sb.appendLine("<!DOCTYPE html>")
        sb.appendLine("<html lang=\"zh-CN\">")
        sb.appendLine("<head>")
        sb.appendLine("<meta charset=\"UTF-8\">")
        sb.appendLine("<title>${escapeHtml(conversation.title)}</title>")
        sb.appendLine("<style>")
        sb.appendLine("body{font-family:-apple-system,sans-serif;max-width:900px;margin:0 auto;padding:20px;background:#f5f5f5;}")
        sb.appendLine(".header{background:#fff;padding:20px;border-radius:12px;margin-bottom:20px;}")
        sb.appendLine(".message{margin:10px 0;padding:15px;border-radius:12px;}")
        sb.appendLine(".user{background:#e3f2fd;margin-left:20%;}")
        sb.appendLine(".assistant{background:#fff;margin-right:20%;}")
        sb.appendLine(".role{font-weight:bold;margin-bottom:8px;}")
        sb.appendLine(".time{font-size:12px;color:#999;}")
        sb.appendLine("</style>")
        sb.appendLine("</head><body>")

        sb.appendLine("<div class=\"header\">")
        sb.appendLine("<h1>${escapeHtml(conversation.title)}</h1>")
        sb.appendLine("<p>模型：${conversation.modelName} | 消息数：${conversation.messageCount}</p>")
        sb.appendLine("</div>")

        messages.forEach { msg ->
            val cssClass = when (msg.role) {
                "user" -> "user"
                "assistant" -> "assistant"
                else -> ""
            }
            sb.appendLine("<div class=\"message $cssClass\">")
            sb.appendLine("<div class=\"role\">${escapeHtml(msg.role)}</div>")
            sb.appendLine("<div>${escapeHtml(msg.content)}</div>")
            sb.appendLine("<div class=\"time\">${dateFormat.format(Date(msg.createdAt))}</div>")
            sb.appendLine("</div>")
        }

        sb.appendLine("</body></html>")
        return sb.toString()
    }

    private fun escapeHtml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
    }
}
