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
 * 语音语速选项。
 */
object VoiceSpeedOptions {

    data class Speed(val label: String, val rate: Float)

    fun all(): List<Speed> = listOf(
        Speed("很慢", 0.6f),
        Speed("慢", 0.8f),
        Speed("正常", 1.0f),
        Speed("快", 1.2f),
        Speed("很快", 1.5f)
    )
}
