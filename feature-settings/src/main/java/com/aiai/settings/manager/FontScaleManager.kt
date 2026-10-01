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
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.FontScale

/**
 * 字体大小管理。
 *
 * 提供 5 档字号，通过 [Resources] 的 fontScale 实现应用内缩放，
 * 并配合实时预览。
 */
class FontScaleManager(private val settings: SettingsManager) {

    /** 当前字体档位。 */
    val current: FontScale get() = settings.fontScale.value

    /** 设置字体档位。 */
    fun setScale(scale: FontScale) {
        settings.setFontScale(scale)
    }

    /**
     * 包装 Context，使 resources 应用当前 fontScale。
     */
    fun attach(base: Context): Context {
        val config = base.resources.configuration
        config.fontScale = current.scale
        return base.createConfigurationContext(config)
    }

    companion object {
        @Volatile
        private var instance: FontScaleManager? = null

        fun get(context: Context): FontScaleManager {
            return instance ?: synchronized(this) {
                instance ?: FontScaleManager(SettingsManager.get(context)).also { instance = it }
            }
        }
    }
}
