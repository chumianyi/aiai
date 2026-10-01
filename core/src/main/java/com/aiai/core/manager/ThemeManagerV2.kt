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

import android.content.Context
import android.content.res.Configuration
import com.aiai.common.util.storage.MMKVManager

/**
 * 主题管理器 V2。
 */
object ThemeManagerV2 {
    const val SYSTEM = 0
    const val LIGHT = 1
    const val DARK = 2
    private const val KEY = "theme_mode"

    var mode: Int
        get() = MMKVManager.getInt(KEY, SYSTEM)
        set(v) = MMKVManager.putInt(KEY, v)

    fun isDark(ctx: Context): Boolean = when (mode) {
        LIGHT -> false
        DARK -> true
        else -> (ctx.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
    }
}
