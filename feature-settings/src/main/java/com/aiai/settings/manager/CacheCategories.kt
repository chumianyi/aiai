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
 * 缓存类别统计。
 */
data class CacheCategory(
    val name: String,
    val sizeBytes: Long,
    val path: String
)

/**
 * 缓存分类详情。
 */
object CacheCategories {

    fun all(): List<CacheCategory> = listOf(
        CacheCategory("图片缓存", 12_500_000, "/cache/images"),
        CacheCategory("网络缓存", 3_200_000, "/cache/network"),
        CacheCategory("数据库", 8_700_000, "/databases"),
        CacheCategory("日志", 500_000, "/cache/logs"),
        CacheCategory("临时文件", 1_200_000, "/cache/tmp")
    )
}
