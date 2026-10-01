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
 * API异常类。
 *
 * 封装网络请求中的各种异常情况，包含错误码、错误信息、错误类型。
 *
 * @property code 错误码
 * @property message 错误信息
 * @property type 错误类型
 * @property httpCode HTTP状态码（可选）
 */
class ApiException(
    val code: Int,
    override val message: String,
    val type: String = "unknown",
    val httpCode: Int? = null,
) : Exception(message) {

    companion object {
        /**
         * 创建网络不可用异常。
         *
         * @return ApiException
         */
        fun networkUnavailable(): ApiException = ApiException(
            code = ErrorCode.NETWORK_UNAVAILABLE,
            message = "网络不可用，请检查网络连接",
            type = "network_error",
        )

        /**
         * 创建超时异常。
         *
         * @return ApiException
         */
        fun timeout(): ApiException = ApiException(
            code = ErrorCode.REQUEST_TIMEOUT,
            message = "请求超时，请稍后重试",
            type = "timeout",
        )

        /**
         * 创建未授权异常。
         *
         * @return ApiException
         */
        fun unauthorized(): ApiException = ApiException(
            code = ErrorCode.UNAUTHORIZED,
            message = "登录已过期，请重新登录",
            type = "auth_error",
        )

        /**
         * 创建服务器错误异常。
         *
         * @return ApiException
         */
        fun serverError(): ApiException = ApiException(
            code = ErrorCode.SERVER_ERROR,
            message = "服务器开小差了，请稍后重试",
            type = "server_error",
        }

        /**
         * 创建限流异常。
         *
         * @return ApiException
         */
        fun rateLimited(): ApiException = ApiException(
            code = ErrorCode.RATE_LIMITED,
            message = "请求过于频繁，请稍后再试",
            type = "rate_limit",
        )
    }
}
