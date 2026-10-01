/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util.image

import android.graphics.Bitmap
import android.util.LruCache

/**
 * Bitmap 内存缓存池。
 *
 * 基于 LruCache，自动管理缓存大小，防止 OOM。
 */
object BitmapPool {

    private const val MAX_MEMORY = Runtime.getRuntime().maxMemory().toInt() / 8
    private val cache: LruCache<String, Bitmap> = object : LruCache<String, Bitmap>(MAX_MEMORY) {
        override fun sizeOf(key: String, value: Bitmap): Int {
            return value.byteCount
        }
    }

    /** 存入缓存。 */
    fun put(key: String, bitmap: Bitmap) {
        if (cache.get(key) == null) cache.put(key, bitmap)
    }

    /** 取出缓存。 */
    fun get(key: String): Bitmap? = cache.get(key)

    /** 移除缓存。 */
    fun remove(key: String) {
        cache.remove(key)
    }

    /** 清空缓存。 */
    fun clear() {
        cache.evictAll()
    }

    /** 当前缓存大小。 */
    fun size(): Int = cache.size()
}
