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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.crash

import android.content.Context
import java.io.File
import java.io.FileWriter

/**
 * 崩溃上报器。
 *
 * 本地保存崩溃报告，支持后续上传。
 */
object CrashUploader {

    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    /** 本地保存崩溃报告。 */
    fun saveLocal(report: CrashReport) {
        try {
            val dir = File(context.cacheDir, "crash")
            dir.mkdirs()
            val file = File(dir, "crash_${report.time}.txt")
            FileWriter(file).use { writer ->
                writer.write("Time: ${report.time}\n")
                writer.write("Thread: ${report.threadName}\n")
                writer.write("Type: ${report.exceptionType}\n")
                writer.write("Message: ${report.message}\n")
                writer.write("AppVersion: ${report.appVersion}\n")
                writer.write("Device: ${report.deviceInfo}\n")
                writer.write("Memory: ${report.memoryInfo}\n")
                writer.write("\nStack Trace:\n${report.stackTrace}\n")
            }
        } catch (e: Exception) {
            // 忽略
        }
    }

    /** 上传所有本地崩溃报告（占位）。 */
    suspend fun uploadAll() {
        // TODO: 实际上传到服务器
    }
}
