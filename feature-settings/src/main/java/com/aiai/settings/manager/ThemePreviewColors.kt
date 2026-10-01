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
 * 主题色预览。
 */
object ThemePreviewColors {

    fun preview(primary: Int): Map<String, Int> = mapOf(
        "primary" to primary,
        "primaryDark" to (primary and 0xFEFEFE.toInt()),
        "primaryLight" to (primary or 0x22000000),
        "accent" to primary,
        "background" to 0xFFFAFAFA.toInt(),
        "surface" to 0xFFFFFFFF.toInt()
    )
}
