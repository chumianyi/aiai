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

import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/**
 * CSV 文件工具类。
 *
 * 支持写入和读取 CSV 文件。
 */
object CSVUtil {

    /**
     * 写入 CSV 文件（带表头）。
     *
     * @param headers 表头
     * @param rows 数据行
     */
    fun write(file: File, headers: List<String>, rows: List<List<String>>): Boolean {
        return try {
            file.parentFile?.mkdirs()
            OutputStreamWriter(FileOutputStream(file), Charsets.UTF_8).use { writer ->
                writer.write(headers.joinToString(",") { escape(it) })
                writer.write("\n")
                rows.forEach { row ->
                    writer.write(row.joinToString(",") { escape(it) })
                    writer.write("\n")
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 读取 CSV 文件为二维列表。
     */
    fun read(file: File): List<List<String>> {
        if (!file.exists()) return emptyList()
        val result = mutableListOf<List<String>>()
        file.readLines().forEach { line ->
            result.add(line.split(",").map { unescape(it) })
        }
        return result
    }

    private fun escape(s: String): String {
        return if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            "\"" + s.replace("\"", "\"\"") + "\""
        } else s
    }

    private fun unescape(s: String): String {
        return if (s.startsWith("\"") && s.endsWith("\"")) {
            s.substring(1, s.length - 1).replace("\"\"", "\"")
        } else s
    }
}
