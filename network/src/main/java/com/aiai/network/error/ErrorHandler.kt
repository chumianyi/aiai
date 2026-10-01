/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.error

/**
 * 错误处理器，将异常转换为用户友好的错误信息。
 *
 * 统一处理网络请求中的各种异常，提供标准化的错误信息。
 */
object ErrorHandler {

    /**
     * 处理异常，转换为ApiException。
     *
     * @param throwable 原始异常
     * @return 处理后的ApiException
     */
    fun handle(throwable: Throwable): ApiException {
        return when (throwable) {
            is ApiException -> throwable
            is java.net.SocketTimeoutException -> ApiException.timeout()
            is java.net.UnknownHostException -> ApiException(
                code = ErrorCode.DNS_FAILED,
                message = "无法解析域名，请检查网络",
                type = "dns_error",
            )
            is java.net.ConnectException -> ApiException(
                code = ErrorCode.CONNECTION_FAILED,
                message = "连接失败，请检查网络",
                type = "connection_error",
            )
            is javax.net.ssl.SSLException -> ApiException(
                code = ErrorCode.SSL_HANDSHAKE_FAILED,
                message = "安全连接失败",
                type = "ssl_error",
            )
            is java.io.IOException -> ApiException(
                code = ErrorCode.NETWORK_INTERRUPTED,
                message = "网络中断，请重试",
                type = "io_error",
            )
            else -> ApiException(
                code = ErrorCode.UNKNOWN_ERROR,
                message = throwable.message ?: "未知错误",
                type = "unknown",
            )
        }
    }

    /**
     * 处理HTTP状态码。
     *
     * @param httpCode HTTP状态码
     * @param message 错误信息
     * @return 对应的ApiException
     */
    fun handleHttpCode(httpCode: Int, message: String = ""): ApiException {
        val (code, type) = when (httpCode) {
            400 -> ErrorCode.BAD_REQUEST to "bad_request"
            401 -> ErrorCode.UNAUTHORIZED to "unauthorized"
            403 -> ErrorCode.FORBIDDEN to "forbidden"
            404 -> ErrorCode.NOT_FOUND to "not_found"
            408 -> ErrorCode.REQUEST_TIMEOUT to "timeout"
            409 -> ErrorCode.RESOURCE_CONFLICT to "conflict"
            413 -> ErrorCode.PAYLOAD_TOO_LARGE to "too_large"
            415 -> ErrorCode.UNSUPPORTED_MEDIA_TYPE to "unsupported_type"
            429 -> ErrorCode.RATE_LIMITED to "rate_limit"
            500 -> ErrorCode.SERVER_ERROR to "server_error"
            502 -> ErrorCode.UPSTREAM_ERROR to "bad_gateway"
            503 -> ErrorCode.SERVICE_UNAVAILABLE to "unavailable"
            504 -> ErrorCode.GATEWAY_TIMEOUT to "gateway_timeout"
            else -> ErrorCode.UNKNOWN_ERROR to "unknown"
        }
        return ApiException(
            code = code,
            message = message.ifBlank { ErrorCode.getDefaultMessage(code) },
            type = type,
            httpCode = httpCode,
        )
    }

    /**
     * 获取用户友好的错误提示。
     *
     * @param throwable 异常
     * @return 用户可理解的错误信息
     */
    fun getUserMessage(throwable: Throwable): String {
        val apiException = handle(throwable)
        return ErrorCode.getDefaultMessage(apiException.code)
    }
}
