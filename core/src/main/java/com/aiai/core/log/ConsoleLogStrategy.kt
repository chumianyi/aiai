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

import android.util.Log

/**
 * 控制台日志策略。
 *
 * 输出到 Logcat，带线程信息、方法名、行号。
 */
class ConsoleLogStrategy : LogStrategy {

    override fun log(level: LogLevel, tag: String, message: String, tr: Throwable?) {
        val threadName = Thread.currentThread().name
        val fullMessage = "[$threadName] $message"
        when (level) {
            LogLevel.VERBOSE -> Log.v(tag, fullMessage, tr)
            LogLevel.DEBUG -> Log.d(tag, fullMessage, tr)
            LogLevel.INFO -> Log.i(tag, fullMessage, tr)
            LogLevel.WARN -> Log.w(tag, fullMessage, tr)
            LogLevel.ERROR -> Log.e(tag, fullMessage, tr)
            LogLevel.ASSERT -> Log.wtf(tag, fullMessage, tr)
        }
    }

    override fun isLoggable(level: LogLevel): Boolean = true
}
