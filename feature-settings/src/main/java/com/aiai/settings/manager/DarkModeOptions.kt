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
 * 深色模式选项。
 */
object DarkModeOptions {

    data class Option(val label: String, val mode: Int)

    fun all(): List<Option> = listOf(
        Option("跟随系统", 0),
        Option("浅色", 1),
        Option("深色", 2),
        Option("AMOLED 黑", 3)
    )
}
