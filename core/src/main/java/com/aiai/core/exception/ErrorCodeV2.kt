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
 * 错误码 V2。
 */
object ErrorCodeV2 {
    const val SUCCESS = 0
    const val UNKNOWN = -1
    const val NETWORK_ERROR = 1001
    const val TIMEOUT = 1002
    const val PARSE_ERROR = 1003
    const val SERVER_ERROR = 2001
    const val UNAUTHORIZED = 4001
    const val FORBIDDEN = 4003
    const val NOT_FOUND = 4004
    const val NOT_LOGIN = 4001
    const val TOKEN_EXPIRED = 4002
    const val PARAM_ERROR = 3001
    const val DATA_EMPTY = 3002
}
