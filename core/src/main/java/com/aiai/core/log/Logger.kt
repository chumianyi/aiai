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
package com.aiai.core.log

import android.content.Context

/**
 * 日志管理器（core 模块版本）。
 *
 * 支持多策略输出。
 */
object Logger {

    private val strategies = mutableListOf<LogStrategy>()
    var config = LogConfig()

    /** 初始化。 */
    fun init(context: Context) {
        if (config.consoleEnabled) strategies.add(ConsoleLogStrategy())
        if (config.fileEnabled) strategies.add(FileLogStrategy(context))
    }

    /** 添加策略。 */
    fun addStrategy(strategy: LogStrategy) {
        strategies.add(strategy)
    }

    fun v(message: String, tr: Throwable? = null) {
        log(LogLevel.VERBOSE, config.tag, message, tr)
    }

    fun d(message: String, tr: Throwable? = null) {
        log(LogLevel.DEBUG, config.tag, message, tr)
    }

    fun i(message: String, tr: Throwable? = null) {
        log(LogLevel.INFO, config.tag, message, tr)
    }

    fun w(message: String, tr: Throwable? = null) {
        log(LogLevel.WARN, config.tag, message, tr)
    }

    fun e(message: String, tr: Throwable? = null) {
        log(LogLevel.ERROR, config.tag, message, tr)
    }

    private fun log(level: LogLevel, tag: String, message: String, tr: Throwable?) {
        if (!config.enabled || level.ordinal < config.minLevel.ordinal) return
        strategies.forEach { it.log(level, tag, message, tr) }
    }
}
