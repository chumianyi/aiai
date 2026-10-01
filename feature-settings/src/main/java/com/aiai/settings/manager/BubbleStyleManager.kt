/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.BubbleStyle

/**
 * 气泡样式管理。
 *
 * 维护 5 种内置气泡样式，以及用户自定义的气泡颜色。
 */
class BubbleStyleManager(private val settings: SettingsManager) {

    /** 当前样式。 */
    val current: BubbleStyle get() = settings.bubbleStyle.value

    /** 设置样式。 */
    fun setStyle(style: BubbleStyle) {
        settings.setBubbleStyle(style)
    }

    /** 我方气泡颜色。 */
    var bubbleMyColor: Int
        get() = settings.getInt("bubble_my_color", 0xFF3D5AFE.toInt())
        set(value) = settings.putInt("bubble_my_color", value)

    /** 对方气泡颜色。 */
    var bubbleOtherColor: Int
        get() = settings.getInt("bubble_other_color", 0xFFECEFF1.toInt())
        set(value) = settings.putInt("bubble_other_color", value)

    companion object {
        @Volatile
        private var instance: BubbleStyleManager? = null

        fun get(context: Context): BubbleStyleManager {
            return instance ?: synchronized(this) {
                instance ?: BubbleStyleManager(SettingsManager.get(context)).also { instance = it }
            }
        }
    }
}
