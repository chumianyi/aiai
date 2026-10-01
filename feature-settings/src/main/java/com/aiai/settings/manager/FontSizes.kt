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
 * 字体大小档位。
 */
object FontSizes {

    data class Size(val label: String, val sp: Float)

    fun all(): List<Size> = listOf(
        Size("小", 13f),
        Size("标准", 15f),
        Size("大", 17f),
        Size("超大", 19f),
        Size("最大", 22f)
    )
}
