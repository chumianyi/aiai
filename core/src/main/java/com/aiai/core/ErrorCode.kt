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
package com.aiai.core

/**
 * 错误码常量定义。
 *
 * 0-99: 通用错误
 * 100-199: 网络错误
 * 200-299: 业务错误
 * 300-399: 认证错误
 * 400-499: 参数错误
 * 500-599: 服务器错误
 */
object ErrorCode {

    // region 通用
    const val SUCCESS = 0
    const val UNKNOWN = -1
    const val PARSE_ERROR = -2
    const val CANCELLED = -3
    // endregion

    // region 网络错误 (100-199)
    const val NETWORK_ERROR = 100
    const val NETWORK_TIMEOUT = 101
    const val NETWORK_UNAVAILABLE = 102
    const val NETWORK_CONNECTION_ERROR = 103
    // endregion

    // region 业务错误 (200-299)
    const val BUSINESS_ERROR = 200
    const val DATA_EMPTY = 201
    const val DATA_EXPIRED = 202
    const val DATA_DUPLICATE = 203
    // endregion

    // region 认证错误 (300-399)
    const val UNAUTHORIZED = 300
    const val TOKEN_EXPIRED = 301
    const val TOKEN_INVALID = 302
    const val PERMISSION_DENIED = 303
    const val ACCOUNT_BANNED = 304
    // endregion

    // region 参数错误 (400-499)
    const val PARAM_ERROR = 400
    const val PARAM_MISSING = 401
    const val PARAM_INVALID = 402
    // endregion

    // region 服务器错误 (500-599)
    const val SERVER_ERROR = 500
    const val SERVER_BUSY = 501
    const val SERVER_MAINTENANCE = 502
    const val NOT_FOUND = 503
    // endregion

    // region 文件错误 (600-699)
    const val FILE_NOT_FOUND = 600
    const val FILE_TOO_LARGE = 601
    const val FILE_UPLOAD_FAILED = 602
    const val FILE_DOWNLOAD_FAILED = 603
    // endregion

    // region 聊天错误 (700-799)
    const val CHAT_SEND_FAILED = 700
    const val CHAT_NETWORK_ERROR = 701
    const val CHAT_SESSION_NOT_FOUND = 702
    const val CHAT_MESSAGE_TOO_LONG = 703
    const val CHAT_RATE_LIMITED = 704
    // endregion

    // region AI 错误 (800-899)
    const val AI_MODEL_NOT_FOUND = 800
    const val AI_RATE_LIMIT = 801
    const val AI_CONTENT_FILTERED = 802
    const val AI_TOKEN_LIMIT = 803
    const val AI_SERVER_ERROR = 804
    // endregion
}
