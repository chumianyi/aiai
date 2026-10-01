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
 * 调试日志管理器
 *
 * 记录应用运行时的调试日志，支持查看和导出。
 */
class DebugLogManager(private val context: Context) {

    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }

    data class LogEntry(
        val timestamp: Long,
        val level: LogLevel,
        val tag: String,
        val message: String
    )

    private val _logs = MutableStateFlow<List<LogEntry>>(emptyList())
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()

    private val _enabled = MutableStateFlow(true)
    val enabled: StateFlow<Boolean> = _enabled.asStateFlow()

    private val maxLogSize = 500

    fun log(level: LogLevel, tag: String, message: String) {
        if (!_enabled.value) return
        val entry = LogEntry(System.currentTimeMillis(), level, tag, message)
        val newLogs = _logs.value + entry
        _logs.value = if (newLogs.size > maxLogSize) newLogs.takeLast(maxLogSize) else newLogs
    }

    fun d(tag: String, message: String) = log(LogLevel.DEBUG, tag, message)
    fun i(tag: String, message: String) = log(LogLevel.INFO, tag, message)
    fun w(tag: String, message: String) = log(LogLevel.WARN, tag, message)
    fun e(tag: String, message: String) = log(LogLevel.ERROR, tag, message)

    fun setEnabled(enabled: Boolean) {
        _enabled.value = enabled
    }

    fun clearLogs() {
        _logs.value = emptyList()
    }

    fun exportLogs(): String {
        return _logs.value.joinToString("\n") {
            "${it.timestamp} [${it.level}] ${it.tag}: ${it.message}"
        }
    }

    fun getErrors(): List<LogEntry> {
        return _logs.value.filter { it.level == LogLevel.ERROR }
    }
}
