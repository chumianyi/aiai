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
 * 错误码常量。
 */
object ErrorCode {
    const val SUCCESS = 0
    const val UNKNOWN_ERROR = -1

    // 网络相关 1000-1999
    const val NETWORK_NO_CONNECTION = 1001
    const val NETWORK_TIMEOUT = 1002
    const val NETWORK_CONNECTION_ERROR = 1003
    const val NETWORK_SSL_ERROR = 1004
    const val NETWORK_IO_ERROR = 1005
    const val NETWORK_PARSE_ERROR = 1006

    // HTTP 2000-2999
    const val HTTP_BAD_REQUEST = 2001
    const val HTTP_UNAUTHORIZED = 2002
    const val HTTP_FORBIDDEN = 2003
    const val HTTP_NOT_FOUND = 2004
    const val HTTP_SERVER_ERROR = 2005
    const val HTTP_BAD_GATEWAY = 2006
    const val HTTP_SERVICE_UNAVAILABLE = 2007

    // 业务 3000-3999
    const val BUSINESS_ERROR = 3001
    const val BUSINESS_PARAM_ERROR = 3002
    const val BUSINESS_DATA_EMPTY = 3003
    const val BUSINESS_DATA_EXPIRED = 3004
    const val BUSINESS_DATA_CONFLICT = 3005

    // 登录/鉴权 4000-4999
    const val AUTH_NOT_LOGIN = 4001
    const val AUTH_TOKEN_EXPIRED = 4002
    const val AUTH_TOKEN_INVALID = 4003
    const val AUTH_PERMISSION_DENIED = 4004
    const val AUTH_ACCOUNT_BANNED = 4005
    const val AUTH_ACCOUNT_NOT_EXIST = 4006
    const val AUTH_PASSWORD_ERROR = 4007

    // 数据 5000-5999
    const val DATA_PARSE_ERROR = 5001
    const val DATA_SAVE_ERROR = 5002
    const val DATA_READ_ERROR = 5003
    const val DATA_NOT_FOUND = 5004

    // 文件 6000-6999
    const val FILE_NOT_FOUND = 6001
    const val FILE_READ_ERROR = 6002
    const val FILE_WRITE_ERROR = 6003
    const val FILE_TOO_LARGE = 6004
    const val FILE_FORMAT_ERROR = 6005

    // 权限 7000-7999
    const val PERMISSION_DENIED = 7001
    const val PERMISSION_NEVER_ASK = 7002

    // 缓存 8000-8999
    const val CACHE_EXPIRED = 8001
    const val CACHE_NOT_FOUND = 8002
    const val CACHE_CLEARED = 8003
}
