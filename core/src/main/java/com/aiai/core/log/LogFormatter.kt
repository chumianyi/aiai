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

/**
 * 日志格式化器。
 */
object LogFormatter {

    /** 格式化日志消息。 */
    fun format(level: LogLevel, tag: String, message: String): String {
        return "${level.name}/$tag: $message"
    }

    /** 获取调用栈信息（跳过 Logger 自身帧）。 */
    fun getCallerInfo(): String {
        val stack = Throwable().stackTrace
        // 找到第一个不在 log 包中的栈帧
        val caller = stack.firstOrNull {
            !it.className.startsWith("com.aiai.core.log") &&
                !it.className.startsWith("com.aiai.common.util.other.Logger")
        }
        return caller?.let { "${it.fileName}:${it.lineNumber}" } ?: "unknown"
    }
}
