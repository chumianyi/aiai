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
 * 图片数量选项。
 */
object ImageCountOptions {

    data class Count(val n: Int, val label: String)

    fun all(): List<Count> = listOf(
        Count(1, "1 张"),
        Count(2, "2 张"),
        Count(4, "4 张"),
        Count(6, "6 张")
    )
}
