/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.manager

import com.aiai.common.util.storage.MMKVManager

/**
 * 字体大小管理器。
 *
 * 支持四档：
 * - SMALL: 0.85x
 * - NORMAL: 1.0x
 * - LARGE: 1.15x
 * - X_LARGE: 1.3x
 */
object FontScaleManager {

    const val SMALL = 0.85f
    const val NORMAL = 1.0f
    const val LARGE = 1.15f
    const val X_LARGE = 1.3f

    private const val KEY_FONT_SCALE = "font_scale"

    /** 当前字体缩放比例。 */
    var fontScale: Float
        get() = MMKVManager.getFloat(KEY_FONT_SCALE, NORMAL)
        set(value) = MMKVManager.putFloat(KEY_FONT_SCALE, value)

    /** 缩放字体大小。 */
    fun scale(baseSize: Float): Float = baseSize * fontScale
}
