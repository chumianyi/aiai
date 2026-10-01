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
 * HTTP状态码处理工具。
 *
 * 统一管理HTTP状态码的判断和处理逻辑。
 */
object HttpResponseCode {

    // ===== 2xx Success =====
    const val OK = 200
    const val CREATED = 201
    const val ACCEPTED = 202
    const val NO_CONTENT = 204

    // ===== 3xx Redirection =====
    const val MOVED_PERMANENTLY = 301
    const val FOUND = 302
    const val NOT_MODIFIED = 304

    // ===== 4xx Client Error =====
    const val BAD_REQUEST = 400
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val METHOD_NOT_ALLOWED = 405
    const val REQUEST_TIMEOUT = 408
    const val CONFLICT = 409
    const val GONE = 410
    const val PAYLOAD_TOO_LARGE = 413
    const val UNSUPPORTED_MEDIA_TYPE = 415
    const val TOO_MANY_REQUESTS = 429

    // ===== 5xx Server Error =====
    const val INTERNAL_SERVER_ERROR = 500
    const val NOT_IMPLEMENTED = 501
    const val BAD_GATEWAY = 502
    const val SERVICE_UNAVAILABLE = 503
    const val GATEWAY_TIMEOUT = 504

    /**
     * 判断是否为成功状态码。
     *
     * @param code HTTP状态码
     * @return true如果2xx
     */
    fun isSuccess(code: Int): Boolean = code in 200..299

    /**
     * 判断是否为重定向。
     *
     * @param code HTTP状态码
     * @return true如果3xx
     */
    fun isRedirect(code: Int): Boolean = code in 300..399

    /**
     * 判断是否为客户端错误。
     *
     * @param code HTTP状态码
     * @return true如果4xx
     */
    fun isClientError(code: Int): Boolean = code in 400..499

    /**
     * 判断是否为服务器错误。
     *
     * @param code HTTP状态码
     * @return true如果5xx
     */
    fun isServerError(code: Int): Boolean = code in 500..599

    /**
     * 判断是否需要重试。
     *
     * @param code HTTP状态码
     * @return true如果可重试（5xx、429）
     */
    fun isRetryable(code: Int): Boolean {
        return code == TOO_MANY_REQUESTS || code == BAD_GATEWAY ||
            code == SERVICE_UNAVAILABLE || code == GATEWAY_TIMEOUT
    }
}
