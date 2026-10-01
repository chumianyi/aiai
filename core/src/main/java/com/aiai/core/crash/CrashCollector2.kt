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
import android.os.Build
import com.aiai.common.util.other.Logger
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 崩溃收集器。
 *
 * 收集崩溃时间、设备信息、堆栈、内存状态。
 */
object CrashCollector {

    private lateinit var context: Context
    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun install(context: Context) {
        this.context = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            handleCrash(thread, throwable)
        }
        Logger.d("CrashCollector installed")
    }

    private fun handleCrash(thread: Thread, throwable: Throwable) {
        try {
            val report = buildCrashReport(thread, throwable)
            saveCrashReport(report)
            Logger.e("CrashCollector", "Crash caught: ${report.message}")
        } catch (e: Exception) {
            Logger.e("CrashCollector", e)
        } finally {
            defaultHandler?.uncaughtException(thread, throwable)
        }
    }

    private fun buildCrashReport(thread: Thread, throwable: Throwable): CrashReport {
        val sw = StringWriter()
        throwable.printStackTrace(PrintWriter(sw))
        val stackTrace = sw.toString()

        val runtime = Runtime.getRuntime()
        val usedMem = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
        val maxMem = runtime.maxMemory() / 1024 / 1024

        return CrashReport(
            time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
            threadName = thread.name,
            message = throwable.message ?: "unknown",
            exceptionClass = throwable.javaClass.name,
            stackTrace = stackTrace,
            deviceBrand = Build.BRAND,
            deviceModel = Build.MODEL,
            sdkInt = Build.VERSION.SDK_INT,
            release = Build.VERSION.RELEASE ?: "",
            usedMemoryMB = usedMem,
            maxMemoryMB = maxMem
        )
    }

    private fun saveCrashReport(report: CrashReport) {
        try {
            val dir = java.io.File(context.filesDir, "crash")
            if (!dir.exists()) dir.mkdirs()
            val file = java.io.File(dir, "crash_${System.currentTimeMillis()}.txt")
            file.writeText(report.toString())
        } catch (e: Exception) {
            Logger.e("CrashCollector", e)
        }
    }
}
