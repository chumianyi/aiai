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
 * 气泡样式说明。
 */
object BubbleStyleDesc {

    fun desc(style: Int): String = when (style) {
        0 -> "大圆角，柔和现代"
        1 -> "直角，简洁高效"
        2 -> "小间距，紧凑节省"
        3 -> "大间距，宽松舒适"
        4 -> "无气泡，流式文本"
        else -> ""
    }
}
