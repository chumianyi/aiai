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
 * 更新日志条目。
 */
data class ChangeLog(
    val version: String,
    val date: String,
    val items: List<String>
)

object ChangeLogs {
    fun recent(): List<ChangeLog> = listOf(
        ChangeLog("1.2.0", "2026-09-20", listOf(
            "新增图片生成设置",
            "优化语音识别",
            "修复若干 bug"
        )),
        ChangeLog("1.1.0", "2026-08-15", listOf(
            "新增提示词模板库",
            "支持深色模式",
            "提升导出速度"
        )),
        ChangeLog("1.0.0", "2026-07-01", listOf(
            "首次发布"
        ))
    )
}
