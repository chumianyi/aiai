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
 * 自定义应用异常类。
 *
 * @param code 错误码
 * @param message 错误信息
 * @param cause 原始异常
 */
class AppException(
    val code: Int,
    override val message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    companion object {
        /** 网络错误。 */
        fun network(cause: Throwable? = null) = AppException(-1, "网络连接失败", cause)

        /** 解析错误。 */
        fun parse(cause: Throwable? = null) = AppException(-2, "数据解析错误", cause)

        /** 未知错误。 */
        fun unknown(cause: Throwable? = null) = AppException(-99, "未知错误", cause)
    }
}
