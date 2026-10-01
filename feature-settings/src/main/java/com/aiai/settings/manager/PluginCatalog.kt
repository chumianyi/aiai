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
import com.aiai.settings.model.PluginInfo
import com.aiai.settings.model.PluginStatus

/**
 * 插件目录：内置插件元数据。
 */
object PluginCatalog {

    fun builtin(): List<PluginInfo> = listOf(
        PluginInfo("p_search", "联网搜索", "为 AI 接入实时网页搜索能力", "1.0.0", "AiAi",
            downloadSize = 120L * 1024, isBuiltin = true,
            status = PluginStatus.INSTALLED,
            permissions = listOf("android.permission.INTERNET")),
        PluginInfo("p_translate", "实时翻译", "对话中自动翻译多语言内容", "1.2.0", "AiAi",
            downloadSize = 256L * 1024, isBuiltin = true,
            status = PluginStatus.INSTALLED,
            permissions = listOf("android.permission.INTERNET")),
        PluginInfo("p_code", "代码解释器", "运行沙箱代码，做数据分析", "0.9.5", "AiAi Lab",
            downloadSize = 8L * 1024 * 1024, isBuiltin = true,
            status = PluginStatus.INSTALLED,
            permissions = listOf("android.permission.INTERNET")),
        PluginInfo("p_ocr", "图片文字识别", "识别截图、照片中的文字", "1.1.0", "AiAi",
            downloadSize = 4L * 1024 * 1024,
            status = PluginStatus.NOT_INSTALLED,
            permissions = listOf("android.permission.CAMERA", "android.permission.READ_MEDIA_IMAGES")),
        PluginInfo("p_tts", "语音合成", "把回复朗读出来", "1.0.3", "Voice Team",
            downloadSize = 16L * 1024 * 1024,
            status = PluginStatus.NOT_INSTALLED,
            permissions = listOf("android.permission.INTERNET")),
        PluginInfo("p_math", "数学公式", "渲染 LaTeX 公式", "2.0.1", "OpenSource",
            downloadSize = 512L * 1024,
            status = PluginStatus.NOT_INSTALLED,
            permissions = emptyList()),
        PluginInfo("p_wolfram", "Wolfram Alpha", "接入科学计算引擎", "1.0.0", "Wolfram",
            downloadSize = 64L * 1024,
            status = PluginStatus.NOT_INSTALLED,
            permissions = listOf("android.permission.INTERNET")),
        PluginInfo("p_homework", "作业辅导", "K12 全科题目解析", "1.3.0", "AiAi Edu",
            downloadSize = 2L * 1024 * 1024,
            status = PluginStatus.NOT_INSTALLED,
            permissions = listOf("android.permission.INTERNET"))
    )
}
