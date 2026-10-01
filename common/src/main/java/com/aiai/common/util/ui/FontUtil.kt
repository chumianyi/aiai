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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util.ui

import android.content.Context
import android.graphics.Typeface

/**
 * 字体工具类。
 */
object FontUtil {

    /** 加载自定义字体。 */
    fun loadTypeface(context: Context, assetPath: String): Typeface? {
        return try {
            Typeface.createFromAsset(context.assets, assetPath)
        } catch (e: Exception) {
            null
        }
    }

    /** 获取默认字体。 */
    fun defaultTypeface(): Typeface = Typeface.DEFAULT

    /** 获取等宽字体。 */
    fun monospaceTypeface(): Typeface = Typeface.MONOSPACE
}
