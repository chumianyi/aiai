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
 * 语音识别引擎列表。
 */
object VoiceEngines {

    data class Engine(val id: String, val name: String, val desc: String)

    fun all(): List<Engine> = listOf(
        Engine("system", "系统语音识别", "无需联网"),
        Engine("baidu", "百度语音", "准确率高"),
        Engine("aliyun", "阿里云语音", "中文优化"),
        Engine("openai_whisper", "Whisper API", "多语言")
    )
}
