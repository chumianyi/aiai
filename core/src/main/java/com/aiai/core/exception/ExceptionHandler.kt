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
package com.aiai.core.exception

import android.util.Log
import com.aiai.common.ext.shortMessage
import com.aiai.common.ext.stackTraceToString
import com.aiai.core.ErrorCode

/**
 * 全局异常处理器。
 *
 * 分类处理网络异常、业务异常、未知异常。
 */
object ExceptionHandler {

    private const val TAG = "ExceptionHandler"

    /**
     * 处理异常，返回用户友好的错误信息。
     */
    fun handle(throwable: Throwable): String {
        Log.e(TAG, "Handling exception: ${throwable.shortMessage()}", throwable)

        return when (throwable) {
            is AppException -> ErrorMessageFactory.getMessage(throwable.code)
            is java.net.SocketTimeoutException -> "网络请求超时，请稍后重试"
            is java.net.UnknownHostException -> "网络连接不可用，请检查网络设置"
            is java.io.IOException -> "网络异常，请稍后重试"
            is com.google.gson.JsonSyntaxException -> "数据解析错误"
            else -> "未知错误，请稍后重试"
        }
    }

    /**
     * 获取完整错误堆栈日志。
     */
    fun getErrorLog(throwable: Throwable): String {
        return buildString {
            appendLine("Type: ${throwable::class.java.name}")
            appendLine("Message: ${throwable.message}")
            appendLine("Stack:")
            appendLine(throwable.stackTraceToString())
        }
    }
}
