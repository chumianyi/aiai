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
package com.aiai.common.util.file

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.reflect.Type

/**
 * JSON 文件工具类。
 *
 * 使用 Gson 将对象序列化到文件 / 从文件反序列化。
 */
object JSONFileUtil {

    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    /** 将对象写入 JSON 文件。 */
    fun <T> write(file: File, obj: T): Boolean {
        return try {
            file.parentFile?.mkdirs()
            FileWriter(file).use { gson.toJson(obj, it) }
            true
        } catch (e: Exception) {
            false
        }
    }

    /** 从 JSON 文件读取对象。 */
    fun <T> read(file: File, type: Type): T? {
        return try {
            if (!file.exists()) return null
            FileReader(file).use { gson.fromJson(it, type) }
        } catch (e: Exception) {
            null
        }
    }
}
