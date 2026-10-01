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
 * 图片风格预设。
 */
object ImageStylePresets {

    data class Style(val name: String, val desc: String)

    fun all(): List<Style> = listOf(
        Style("写实", "照片级真实"),
        Style("油画", "印象派风格"),
        Style("动漫", "二次元插画"),
        Style("3D 渲染", "电影质感"),
        Style("手绘", "水彩素描"),
        Style("赛博朋克", "霓虹未来感")
    )
}
