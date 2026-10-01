/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 导出内容构建器。
 */
object ExportBuilder {

    /** 构建 TXT 内容。 */
    fun toTxt(title: String, messages: List<com.aiai.settings.manager.ChatMessage>): String {
        val sb = StringBuilder()
        sb.appendLine("=== $title ===")
        sb.appendLine()
        messages.forEach {
            sb.appendLine("[${com.aiai.settings.util.TimeUtils.formatTime(it.timestamp)}] ${it.role}")
            sb.appendLine(it.content)
            sb.appendLine()
        }
        return sb.toString()
    }

    /** 构建 Markdown 内容。 */
    fun toMarkdown(title: String, messages: List<com.aiai.settings.manager.ChatMessage>): String {
        val sb = StringBuilder()
        sb.appendLine("# $title")
        sb.appendLine()
        messages.forEach {
            sb.appendLine("### ${it.role}")
            sb.appendLine(it.content)
            sb.appendLine()
        }
        return sb.toString()
    }
}
