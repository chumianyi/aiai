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
 * 字体大小预设。
 */
object FontSizePresets {

    data class Preset(val name: String, val scale: Float, val desc: String)

    fun all(): List<Preset> = listOf(
        Preset("小", 0.85f, "紧凑节省空间"),
        Preset("标准", 1.0f, "默认舒适"),
        Preset("大", 1.15f, "阅读更轻松"),
        Preset("超大", 1.3f, "适合视力不佳"),
        Preset("最大", 1.5f, "仅用于无障碍")
    )
}
