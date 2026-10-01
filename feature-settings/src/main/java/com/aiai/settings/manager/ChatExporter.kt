/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import com.aiai.settings.model.ExportConversation
import com.aiai.settings.model.ExportFormat
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 聊天记录导出器。
 *
 * 支持把单个 [ExportConversation] 渲染为 TXT / Markdown / JSON / PDF（PDF 暂用占位文本，
 * 实际项目可接入 PdfDocument）。本类不依赖 Android Framework，便于单元测试。
 */
class ChatExporter {

    private val gson = Gson()
    private val dateFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    /**
     * 导出单个会话到 [outDir]，返回生成的文件。
     */
    fun exportConversation(
        conversation: ExportConversation,
        format: ExportFormat,
        outDir: File
    ): File {
        val safeName = conversation.title.replace(Regex("[^\\w\\u4e00-\\u9fa5-]"), "_")
        val file = File(outDir, "${safeName}_${ExportManager.timestampName()}.${format.extension}")
        val content = when (format) {
            ExportFormat.TXT -> toTxt(conversation)
            ExportFormat.MD -> toMarkdown(conversation)
            ExportFormat.JSON -> toJson(conversation)
            ExportFormat.PDF -> toPdfPlaceholder(conversation)
        }
        FileWriter(file, Charsets.UTF_8).use { it.write(content) }
        return file
    }

    /** TXT：纯文本。 */
    private fun toTxt(conv: ExportConversation): String = buildString {
        appendLine("会话标题：${conv.title}")
        appendLine("导出时间：${dateFmt.format(Date())}")
        appendLine("消息条数：${conv.messageCount}")
        appendLine("=".repeat(40))
        repeat(conv.messageCount) { idx ->
            appendLine("[消息 ${idx + 1}]")
            appendLine("（此处为模拟消息内容，实际项目从数据库读取）")
            appendLine()
        }
    }

    /** Markdown：带标题与表格。 */
    private fun toMarkdown(conv: ExportConversation): String = buildString {
        appendLine("# ${conv.title}")
        appendLine()
        appendLine("> 导出时间：${dateFmt.format(Date())}  ")
        appendLine("> 消息条数：${conv.messageCount}")
        appendLine()
        appendLine("| # | 角色 | 内容 |")
        appendLine("|---|------|------|")
        repeat(conv.messageCount) { idx ->
            val role = if (idx % 2 == 0) "用户" else "AI"
            appendLine("| ${idx + 1} | $role | （消息内容） |")
        }
    }

    /** JSON：结构化。 */
    private fun toJson(conv: ExportConversation): String {
        val obj = JsonObject().apply {
            addProperty("conversationId", conv.conversationId)
            addProperty("title", conv.title)
            addProperty("messageCount", conv.messageCount)
            addProperty("exportedAt", dateFmt.format(Date()))
            add("messages", gson.toJsonTree(emptyList<JsonObject>()))
        }
        return gson.toJson(obj)
    }

    /** PDF 占位：实际项目用 android.graphics.pdf.PdfDocument。 */
    private fun toPdfPlaceholder(conv: ExportConversation): String = buildString {
        appendLine("%PDF-1.4 (placeholder)")
        appendLine("Title: ${conv.title}")
        appendLine("Exported: ${dateFmt.format(Date())}")
    }

    /** 批量导出多会话打包为 zip（占位）。 */
    fun zipFiles(files: List<File>, out: File) {
        // 简化：复制首文件作为 zip 占位，实际项目用 ZipOutputStream
        if (files.isNotEmpty()) files.first().copyTo(out, overwrite = true)
    }
}
