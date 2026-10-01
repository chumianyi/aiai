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
 * 气泡样式预设。
 */
object BubbleStylePresets {

    data class Preset(val name: String, val desc: String)

    fun all(): List<Preset> = listOf(
        Preset("圆润", "大圆角气泡"),
        Preset("扁平", "直角气泡"),
        Preset("紧凑", "小间距气泡"),
        Preset("宽松", "大间距气泡"),
        Preset("无气泡", "纯文本流式")
    )
}
