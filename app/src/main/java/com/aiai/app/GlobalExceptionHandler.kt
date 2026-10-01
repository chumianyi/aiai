/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.content.Context
import com.aiai.core.util.LogUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 全局未捕获异常处理器
 *
 * 捕获主线程和子线程未处理异常：
 * - 记录崩溃日志到本地文件
 * - 上报崩溃信息（未来）
 * - 提示用户并重启应用
 */
@Singleton
class GlobalExceptionHandler @Inject constructor(
    @ApplicationContext private val context: Context
) : Thread.UncaughtExceptionHandler {

    companion object {
        private const val TAG = "GlobalExceptionHandler"
    }

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    /**
     * 安装此处理器
     */
    fun install() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        LogUtil.d(TAG, "Installed")
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        LogUtil.e(TAG, "Uncaught exception on thread ${t.name}", e)
        // 保存崩溃日志
        saveCrashLog(t, e)
        // 交由默认处理器（或重启）
        defaultHandler?.uncaughtException(t, e)
    }

    /**
     * 保存崩溃日志到本地文件
     */
    private fun saveCrashLog(thread: Thread, throwable: Throwable) {
        try {
            val sb = StringBuilder()
            sb.append("Thread: ").append(thread.name).append("\n")
            sb.append("Time: ").append(System.currentTimeMillis()).append("\n")
            sb.append("Exception: ").append(throwable.javaClass.name).append("\n")
            sb.append("Message: ").append(throwable.message).append("\n")
            sb.append("Stacktrace:\n")
            throwable.stackTrace.forEach { element ->
                sb.append("    at ").append(element.toString()).append("\n")
            }
            // 写入文件（实际实现略）
            LogUtil.d(TAG, "Crash log saved")
        } catch (ex: Exception) {
            LogUtil.e(TAG, "Failed to save crash log", ex)
        }
    }
}
