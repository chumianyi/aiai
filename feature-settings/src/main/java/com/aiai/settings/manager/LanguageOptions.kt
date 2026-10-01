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
 * 语言选项（扩展）。
 */
object LanguageOptions {

    data class Language(val name: String, val tag: String)

    fun all(): List<Language> = listOf(
        Language("跟随系统", "system"),
        Language("简体中文", "zh-CN"),
        Language("繁體中文", "zh-TW"),
        Language("English", "en"),
        Language("日本語", "ja"),
        Language("한국어", "ko"),
        Language("Français", "fr"),
        Language("Deutsch", "de"),
        Language("Español", "es")
    )
}
