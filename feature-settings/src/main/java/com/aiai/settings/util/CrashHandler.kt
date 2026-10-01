/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import android.content.Context
import android.widget.Toast

/**
 * 全局异常捕获：在设置页可查看崩溃日志。
 */
class CrashHandler private constructor(private val context: Context) : Thread.UncaughtExceptionHandler {

    private val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()

    /** 最近一次崩溃信息。 */
    var lastCrash: String? = null
        private set

    fun install() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        val sb = StringBuilder()
        sb.appendLine("Thread: ${t.name}")
        sb.appendLine("Exception: ${e.javaClass.name}: ${e.message}")
        e.stackTrace.take(20).forEach { sb.appendLine("    at $it") }
        lastCrash = sb.toString()
        Toast.makeText(context, "发生错误：${e.message}", Toast.LENGTH_LONG).show()
        defaultHandler?.uncaughtException(t, e)
    }

    companion object {
        @Volatile
        private var instance: CrashHandler? = null
        fun get(context: Context): CrashHandler {
            return instance ?: synchronized(this) {
                instance ?: CrashHandler(context.applicationContext).also { instance = it }
            }
        }
    }
}
