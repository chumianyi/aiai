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
package com.aiai.common.util.device

import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * CPU 信息工具类。
 */
object CpuUtil {

    /** 获取 CPU 核心数。 */
    fun getCpuCores(): Int {
        return Runtime.getRuntime().availableProcessors()
    }

    /** 获取 CPU 型号。 */
    fun getCpuModel(): String {
        return try {
            BufferedReader(FileReader("/proc/cpuinfo")).use { reader ->
                val line = reader.readLine()
                line?.substringAfter("Hardware")?.trim() ?: "unknown"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }

    /** 获取 CPU 频率信息。 */
    fun getCpuFreq(): String {
        return try {
            val file = File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
            if (file.exists()) {
                val freq = file.readText().trim().toLong() / 1000
                "${freq}MHz"
            } else "unknown"
        } catch (e: Exception) {
            "unknown"
        }
    }
}
