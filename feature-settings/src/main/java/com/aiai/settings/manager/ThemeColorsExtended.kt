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
 * 主题色候选色板（扩展）。
 */
object ThemeColorsExtended {

    data class ThemeColor(val name: String, val hex: String)

    fun all(): List<ThemeColor> = listOf(
        ThemeColor("靛蓝", "#3D5AFE"),
        ThemeColor("玫瑰", "#E91E63"),
        ThemeColor("紫罗兰", "#9C27B0"),
        ThemeColor("深紫", "#673AB7"),
        ThemeColor("深蓝", "#3F51B5"),
        ThemeColor("蓝", "#2196F3"),
        ThemeColor("青", "#00BCD4"),
        ThemeColor("青绿", "#009688"),
        ThemeColor("绿", "#4CAF50"),
        ThemeColor("橙", "#FF9800"),
        ThemeColor("深橙", "#FF5722"),
        ThemeColor("棕", "#795548")
    )
}
