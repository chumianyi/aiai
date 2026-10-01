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
 * 图片保存路径选项。
 */
object ImageSavePaths {

    data class Path(val label: String, val path: String)

    fun all(): List<Path> = listOf(
        Path("默认相册", "/Pictures/AiAi"),
        Path("下载目录", "/Download/AiAi"),
        Path("应用私有", "/data/data/com.aiai/files/images")
    )
}
