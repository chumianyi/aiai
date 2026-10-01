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
import com.aiai.common.ext.stackTraceToString
import com.aiai.core.BuildConfig

/**
 * 崩溃收集器。
 *
 * 收集崩溃信息：崩溃时间、设备信息、堆栈、内存状态。
 */
object CrashCollector : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private lateinit var context: Context

    /** 安装崩溃收集器。 */
    fun install(context: Context) {
        this.context = context.applicationContext
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        // 生成崩溃报告
        val report = CrashReport(
            time = System.currentTimeMillis(),
            threadName = t.name,
            exceptionType = e::class.java.name,
            message = e.message ?: "",
            stackTrace = e.stackTraceToString(),
            deviceInfo = collectDeviceInfo(),
            appVersion = BuildConfig.VERSION_NAME,
            memoryInfo = collectMemoryInfo()
        )

        // 保存到本地
        CrashUploader.saveLocal(report)

        // 交给默认处理器
        defaultHandler?.uncaughtException(t, e)
    }

    private fun collectDeviceInfo(): String {
        return buildString {
            append("Brand: ").append(android.os.Build.BRAND).append("\n")
            append("Model: ").append(android.os.Build.MODEL).append("\n")
            append("SDK: ").append(android.os.Build.VERSION.SDK_INT).append("\n")
            append("Release: ").append(android.os.Build.VERSION.RELEASE)
        }
    }

    private fun collectMemoryInfo(): String {
        val runtime = Runtime.getRuntime()
        return "Used: ${(runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024}MB, " +
            "Max: ${runtime.maxMemory() / 1024 / 1024}MB"
    }
}
