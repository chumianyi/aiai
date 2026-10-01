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
 * 默认参数预设：不同场景的参数组合。
 */
object DefaultPresets {

    data class Preset(
        val name: String,
        val temperature: Float,
        val maxTokens: Int,
        val topP: Float,
        val systemPrompt: String
    )

    fun all(): List<Preset> = listOf(
        Preset("创意写作", 0.9f, 2048, 0.9f, "你是一位富有创意的作家。"),
        Preset("精确问答", 0.2f, 1024, 0.5f, "请简洁准确地回答。"),
        Preset("代码生成", 0.3f, 4096, 0.8f, "你是一位资深工程师。"),
        Preset("翻译", 0.1f, 2048, 0.3f, "你是专业翻译。"),
        Preset("通用对话", 0.7f, 2048, 0.9f, "你是友好的助手。")
    )
}
