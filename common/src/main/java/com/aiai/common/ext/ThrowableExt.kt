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
package com.aiai.common.ext

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Throwable 相关扩展函数集合。
 *
 * 提供错误信息提取、堆栈格式化等能力。
 */

/** 获取异常的简要描述信息（类名 + message）。 */
fun Throwable.shortMessage(): String {
    return "${this::class.java.simpleName}: $message"
}

/** 将完整堆栈格式化为字符串。 */
fun Throwable.stackTraceToString(): String {
    val sw = StringWriter()
    printStackTrace(PrintWriter(sw))
    return sw.toString()
}

/** 获取异常链上所有 cause 的类名列表。 */
fun Throwable.causeChain(): List<String> {
    val list = mutableListOf<String>()
    var current: Throwable? = this
    while (current != null) {
        list.add(current::class.java.name)
        current = current.cause
    }
    return list
}

/** 判断是否为网络相关异常。 */
fun Throwable.isNetworkError(): Boolean {
    return this is java.io.IOException ||
        this is java.net.SocketTimeoutException ||
        this is java.net.UnknownHostException
}

/** 打印异常到 Logcat（ERROR 级别）。 */
fun Throwable.log(tag: String = "AiAi") {
    Log.e(tag, shortMessage(), this)
}
