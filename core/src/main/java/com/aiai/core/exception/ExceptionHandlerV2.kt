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
 * 异常处理器 V2。
 */
object ExceptionHandlerV2 {
    fun handle(e: Throwable): String {
        return when (e) {
            is java.net.UnknownHostException -> ErrorMessageFactoryV2.get(ErrorCodeV2.NETWORK_ERROR)
            is java.net.SocketTimeoutException -> ErrorMessageFactoryV2.get(ErrorCodeV2.TIMEOUT)
            else -> ErrorMessageFactoryV2.get(ErrorCodeV2.UNKNOWN)
        }
    }
}
