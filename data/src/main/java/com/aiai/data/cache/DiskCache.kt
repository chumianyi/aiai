/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.cache

import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 磁盘缓存。
 *
 * 基于文件系统的磁盘缓存。
 */
class DiskCache(
    private val maxSize: Long,
    private val cacheDir: File = File(System.getProperty("java.io.tmpdir"), "aiai_cache"),
) {

    private val index = ConcurrentHashMap<String, Long>()

    init {
        cacheDir.mkdirs()
    }

    fun get(key: String): String? {
        val file = File(cacheDir, key.hashCode().toString())
        if (!file.exists()) return null
        return try {
            file.readText()
        } catch (e: Exception) {
            null
        }
    }

    fun put(key: String, value: String) {
        val file = File(cacheDir, key.hashCode().toString())
        file.writeText(value)
        index[key] = file.length()
    }

    fun remove(key: String) {
        val file = File(cacheDir, key.hashCode().toString())
        if (file.exists()) file.delete()
        index.remove(key)
    }

    fun clear() {
        cacheDir.listFiles()?.forEach { it.delete() }
        index.clear()
    }

    fun size(): Int = index.size
}
