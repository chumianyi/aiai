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
 * 图片生成预设尺寸。
 */
object ImageSizePresets {

    data class Size(val label: String, val w: Int, val h: Int)

    fun all(): List<Size> = listOf(
        Size("1:1 方形", 1024, 1024),
        Size("3:4 竖版", 768, 1024),
        Size("4:3 横版", 1024, 768),
        Size("9:16 手机", 576, 1024),
        Size("16:9 宽屏", 1024, 576)
    )
}
