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
 * 主题色候选色板。
 */
object ThemeColorPalette {

    /** 推荐主题色。 */
    fun recommended(): List<Int> = listOf(
        0xFF3D5AFE.toInt(),
        0xFFE53935.toInt(),
        0xFFD81B60.toInt(),
        0xFF8E24AA.toInt(),
        0xFF5E35B1.toInt(),
        0xFF1E88E5.toInt(),
        0xFF00ACC1.toInt(),
        0xFF00897B.toInt(),
        0xFF43A047.toInt(),
        0xFFFB8C00.toInt(),
        0xFFF4511E.toInt(),
        0xFF6D4C41.toInt()
    )
}
