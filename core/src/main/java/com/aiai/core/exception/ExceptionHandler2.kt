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

/**
 * 全局异常处理器。
 *
 * 分类处理网络/业务/未知异常，
 * 输出用户友好的提示信息。
 */
object ExceptionHandler {

    /**
     * 处理异常，返回用户可读的错误信息。
     */
    fun handle(throwable: Throwable): String {
        return when (throwable) {
            is AppException -> ErrorMessageFactory.getMessage(throwable.errorCode)
            is java.net.UnknownHostException -> ErrorMessageFactory.getMessage(ErrorCode.NETWORK_NO_CONNECTION)
            is java.net.SocketTimeoutException -> ErrorMessageFactory.getMessage(ErrorCode.NETWORK_TIMEOUT)
            is java.net.ConnectException -> ErrorMessageFactory.getMessage(ErrorCode.NETWORK_CONNECTION_ERROR)
            is javax.net.ssl.SSLException -> ErrorMessageFactory.getMessage(ErrorCode.NETWORK_SSL_ERROR)
            is java.io.IOException -> ErrorMessageFactory.getMessage(ErrorCode.NETWORK_IO_ERROR)
            else -> ErrorMessageFactory.getMessage(ErrorCode.UNKNOWN_ERROR)
        }
    }

    /**
     * 处理异常并记录日志。
     */
    fun handleAndLog(throwable: Throwable): String {
        com.aiai.common.util.other.Logger.e("ExceptionHandler", throwable)
        return handle(throwable)
    }
}
