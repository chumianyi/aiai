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
import com.aiai.common.util.other.Logger

/**
 * 控制台日志策略。
 */
class ConsoleLogStrategy : LogStrategy {
    override fun log(level: Int, tag: String, message: String) {
        when (level) {
            Log.VERBOSE -> Log.v(tag, message)
            Log.DEBUG -> Log.d(tag, message)
            Log.INFO -> Log.i(tag, message)
            Log.WARN -> Log.w(tag, message)
            Log.ERROR -> Log.e(tag, message)
            Log.ASSERT -> Log.wtf(tag, message)
        }
    }
}

/**
 * 文件日志策略。
 */
class FileLogStrategy(private val dir: java.io.File) : LogStrategy {
    override fun log(level: Int, tag: String, message: String) {
        try {
            if (!dir.exists()) dir.mkdirs()
            val file = java.io.File(dir, "log_${System.currentTimeMillis() / 86400000}.txt")
            file.appendText("${System.currentTimeMillis()} $tag: $message\n")
        } catch (e: Exception) {
            Logger.e("FileLogStrategy", e)
        }
    }
}

/**
 * 日志格式化器。
 */
object LogFormatter {
    fun format(level: Int, tag: String, message: String): String {
        val levelStr = when (level) {
            Log.VERBOSE -> "V"
            Log.DEBUG -> "D"
            Log.INFO -> "I"
            Log.WARN -> "W"
            Log.ERROR -> "E"
            else -> "A"
        }
        return "$levelStr/$tag: $message"
    }
}
