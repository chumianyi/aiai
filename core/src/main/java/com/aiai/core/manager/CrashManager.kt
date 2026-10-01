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
package com.aiai.core.manager

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 崩溃管理器。
 *
 * 提供崩溃捕获、日志记录、崩溃信息收集等功能。
 */
object CrashManager : Thread.UncaughtExceptionHandler {

    private const val TAG = "CrashManager"
    private const val CRASH_DIR = "crash"
    private const val MAX_CRASH_FILES = 10

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null
    private var context: Context? = null
    private var onCrashListener: ((CrashInfo) -> Unit)? = null

    /**
     * 崩溃信息数据类。
     *
     * @property timestamp 崩溃时间
     * @property threadName 线程名
     * @property exception 异常
     * @property deviceInfo 设备信息
     * @property stackTrace 堆栈信息
     */
    data class CrashInfo(
        val timestamp: Long,
        val threadName: String,
        val exception: Throwable,
        val deviceInfo: Map<String, String>,
        val stackTrace: String
    )

    /**
     * 初始化。
     *
     * @param context 上下文
     * @param onCrashListener 崩溃回调
     */
    fun init(context: Context, onCrashListener: ((CrashInfo) -> Unit)? = null) {
        this.context = context.applicationContext
        this.onCrashListener = onCrashListener
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        Log.d(TAG, "CrashManager initialized")
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        Log.e(TAG, "Uncaught exception in thread: ${t.name}", e)

        val crashInfo = buildCrashInfo(t, e)
        onCrashListener?.invoke(crashInfo)
        saveCrashLog(crashInfo)

        // 交给默认处理器处理
        defaultHandler?.uncaughtException(t, e)
    }

    /**
     * 构建崩溃信息。
     */
    private fun buildCrashInfo(thread: Thread, e: Throwable): CrashInfo {
        val deviceInfo = collectDeviceInfo()
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return CrashInfo(
            timestamp = System.currentTimeMillis(),
            threadName = thread.name,
            exception = e,
            deviceInfo = deviceInfo,
            stackTrace = sw.toString()
        )
    }

    /**
     * 收集设备信息。
     */
    private fun collectDeviceInfo(): Map<String, String> {
        return mutableMapOf<String, String>().apply {
            put("MANUFACTURER", Build.MANUFACTURER)
            put("MODEL", Build.MODEL)
            put("DEVICE", Build.DEVICE)
            put("PRODUCT", Build.PRODUCT)
            put("SDK_INT", Build.VERSION.SDK_INT.toString())
            put("RELEASE", Build.VERSION.RELEASE)
            put("APP_VERSION", getAppVersion())
        }
    }

    /**
     * 获取应用版本。
     */
    private fun getAppVersion(): String {
        return try {
            val ctx = context ?: return "unknown"
            val pm = ctx.packageManager
            val packageInfo = pm.getPackageInfo(ctx.packageName, 0)
            "${packageInfo.versionName}(${packageInfo.versionCode})"
        } catch (e: Exception) {
            "unknown"
        }
    }

    /**
     * 保存崩溃日志。
     */
    private fun saveCrashLog(crashInfo: CrashInfo) {
        try {
            val ctx = context ?: return
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val fileName = "crash_${dateFormat.format(Date(crashInfo.timestamp))}.log"

            val crashDir = java.io.File(ctx.filesDir, CRASH_DIR)
            if (!crashDir.exists()) {
                crashDir.mkdirs()
            }

            val content = buildString {
                appendLine("=== Crash Report ===")
                appendLine("Time: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(crashInfo.timestamp))}")
                appendLine("Thread: ${crashInfo.threadName}")
                appendLine()
                appendLine("=== Device Info ===")
                crashInfo.deviceInfo.forEach { (key, value) ->
                    appendLine("$key: $value")
                }
                appendLine()
                appendLine("=== Stack Trace ===")
                appendLine(crashInfo.stackTrace)
            }

            java.io.File(crashDir, fileName).writeText(content)
            Log.d(TAG, "Crash log saved: $fileName")

            // 清理旧日志
            cleanOldCrashLogs(crashDir)
        } catch (e: Exception) {
            Log.e(TAG, "Save crash log failed", e)
        }
    }

    /**
     * 清理旧崩溃日志。
     */
    private fun cleanOldCrashLogs(crashDir: java.io.File) {
        try {
            val files = crashDir.listFiles { file -> file.name.startsWith("crash_") }
                ?.sortedBy { it.lastModified() } ?: return

            if (files.size > MAX_CRASH_FILES) {
                files.take(files.size - MAX_CRASH_FILES).forEach { it.delete() }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Clean old crash logs failed", e)
        }
    }

    /**
     * 获取崩溃日志列表。
     */
    fun getCrashLogs(): List<String> {
        val ctx = context ?: return emptyList()
        val crashDir = java.io.File(ctx.filesDir, CRASH_DIR)
        if (!crashDir.exists()) return emptyList()
        return crashDir.listFiles { file -> file.name.startsWith("crash_") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { it.absolutePath } ?: emptyList()
    }

    /**
     * 清除所有崩溃日志。
     */
    fun clearCrashLogs() {
        val ctx = context ?: return
        val crashDir = java.io.File(ctx.filesDir, CRASH_DIR)
        crashDir.listFiles()?.forEach { it.delete() }
    }

    /**
     * 捕获异常（不崩溃）。
     *
     * @param tag 标签
     * @param block 代码块
     */
    fun safe(tag: String = "SafeRun", block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            Log.e(TAG, "Safe run error: $tag", e)
        }
    }
}
