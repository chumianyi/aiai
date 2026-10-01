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
 * 支持小 / 标准 / 大 / 超大。
 */
object FontScaleManager {

    const val SCALE_SMALL = 0.85f
    const val SCALE_NORMAL = 1.0f
    const val SCALE_LARGE = 1.15f
    const val SCALE_XLARGE = 1.3f

    var fontScale: Float
        get() = MMKVManager.getFloat("font_scale", SCALE_NORMAL)
        set(value) = MMKVManager.putFloat("font_scale", value)

    /** 获取缩放后的字体大小。 */
    fun scaleSize(baseSize: Float): Float = baseSize * fontScale
}
