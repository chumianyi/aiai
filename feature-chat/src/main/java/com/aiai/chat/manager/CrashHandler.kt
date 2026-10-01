/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 崩溃处理管理器
 *
 * 捕获应用运行时异常，记录日志并提供恢复建议。
 */
class CrashHandler(private val context: Context) : Thread.UncaughtExceptionHandler {

    data class CrashLog(
        val timestamp: Long,
        val exceptionType: String,
        val message: String,
        val stackTrace: String
    )

    private val _crashLogs = MutableStateFlow<List<CrashLog>>(emptyList())
    val crashLogs: StateFlow<List<CrashLog>> = _crashLogs.asStateFlow()

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable) {
        val log = CrashLog(
            timestamp = System.currentTimeMillis(),
            exceptionType = e.javaClass.simpleName,
            message = e.message ?: "Unknown error",
            stackTrace = e.stackTraceToString()
        )
        _crashLogs.value = _crashLogs.value + log
        defaultHandler?.uncaughtException(t, e)
    }

    fun clearLogs() {
        _crashLogs.value = emptyList()
    }

    fun getRecentCrash(): CrashLog? {
        return _crashLogs.value.lastOrNull()
    }
}
