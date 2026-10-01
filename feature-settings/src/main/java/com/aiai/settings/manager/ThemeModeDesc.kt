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
 * 主题模式说明。
 */
object ThemeModeDesc {

    fun desc(mode: Int): String = when (mode) {
        0 -> "跟随系统自动切换"
        1 -> "始终使用浅色"
        2 -> "始终使用深色"
        3 -> "AMOLED 纯黑省电"
        else -> ""
    }
}
