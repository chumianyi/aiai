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
package com.aiai.network.util

import android.util.Log

/**
 * 网络请求超时配置。
 *
 * 针对不同类型的请求配置不同的超时时间。
 */
class TimeoutConfig {

    companion object {
        private const val TAG = "TimeoutConfig"

        /** 普通请求超时（毫秒） */
        const val DEFAULT_TIMEOUT = 30_000L

        /** 上传请求超时（毫秒） */
        const val UPLOAD_TIMEOUT = 60_000L

        /** 下载请求超时（毫秒） */
        const val DOWNLOAD_TIMEOUT = 120_000L

        /** SSE流式连接超时（毫秒） */
        const val SSE_TIMEOUT = 0L // 0表示不超时

        /** WebSocket连接超时（毫秒） */
        const val WEBSOCKET_TIMEOUT = 15_000L

        /** 健康检查超时（毫秒） */
        const val HEALTH_CHECK_TIMEOUT = 5_000L
    }

    /**
     * 根据URL获取合适的超时时间。
     */
    fun getTimeout(url: String): Long {
        return when {
            url.contains("/upload") -> UPLOAD_TIMEOUT
            url.contains("/download") -> DOWNLOAD_TIMEOUT
            url.contains("/stream") || url.contains("/chat/completions") -> SSE_TIMEOUT
            url.contains("ws://") || url.contains("wss://") -> WEBSOCKET_TIMEOUT
            url.contains("/health") -> HEALTH_CHECK_TIMEOUT
            else -> DEFAULT_TIMEOUT
        }
    }
}
