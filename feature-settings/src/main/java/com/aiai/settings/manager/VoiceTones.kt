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
 * 语音音色列表。
 */
object VoiceTones {

    data class Tone(val id: String, val name: String, val desc: String)

    fun all(): List<Tone> = listOf(
        Tone("zh_female_1", "温柔女声", "亲和自然"),
        Tone("zh_male_1", "沉稳男声", "新闻播报"),
        Tone("zh_female_2", "活泼少女", "年轻甜美"),
        Tone("zh_male_2", "磁性大叔", "低沉有力"),
        Tone("en_female", "English Female", "Clear and bright")
    )
}
