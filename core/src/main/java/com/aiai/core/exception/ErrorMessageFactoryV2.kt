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
 * 错误消息工厂 V2。
 */
object ErrorMessageFactoryV2 {
    fun get(code: Int): String = when (code) {
        ErrorCodeV2.SUCCESS -> "成功"
        ErrorCodeV2.NETWORK_ERROR -> "网络错误"
        ErrorCodeV2.TIMEOUT -> "请求超时"
        ErrorCodeV2.SERVER_ERROR -> "服务器错误"
        ErrorCodeV2.UNAUTHORIZED -> "未授权"
        ErrorCodeV2.NOT_LOGIN -> "请先登录"
        ErrorCodeV2.TOKEN_EXPIRED -> "登录已过期"
        else -> "未知错误"
    }
}
