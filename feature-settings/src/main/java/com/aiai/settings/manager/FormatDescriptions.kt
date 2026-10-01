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
 * 导出格式说明。
 */
object FormatDescriptions {

    fun desc(format: String): String = when (format) {
        "txt" -> "纯文本，通用可读，无格式"
        "md" -> "Markdown，保留标题/列表格式"
        "json" -> "结构化数据，便于二次处理"
        "pdf" -> "正式文档，适合打印存档"
        else -> "未知格式"
    }
}
