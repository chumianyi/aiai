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
 * 发送方式预设。
 */
object SendMethodPresets {

    data class Method(val name: String, val desc: String)

    fun all(): List<Method> = listOf(
        Method("回车发送", "PC 习惯"),
        Method("Ctrl+回车", "避免误触"),
        Method("按钮发送", "手动点发送")
    )
}
