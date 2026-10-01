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
package com.aiai.network.websocket

import com.aiai.network.config.ApiConfig
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * WebSocket消息封装。
 *
 * @property type 消息类型（chat/heartbeat/system/error）
 * @property payload 消息负载
 * @property messageId 消息ID
 * @property timestamp 时间戳
 */
data class WebSocketMessage(
    val type: String = "chat",
    val payload: String = "",
    val messageId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
) {
    companion object {
        const val TYPE_CHAT = "chat"
        const val TYPE_HEARTBEAT = "heartbeat"
        const val TYPE_SYSTEM = "system"
        const val TYPE_ERROR = "error"
        const val TYPE_TYPING = "typing"
        const val TYPE_READ = "read"
        const val TYPE_NOTIFICATION = "notification"

        private val gson = Gson()

        /**
         * 从JSON字符串解析WebSocketMessage。
         *
         * @param json JSON字符串
         * @return WebSocketMessage对象
         */
        fun fromJson(json: String): WebSocketMessage {
            return gson.fromJson(json, WebSocketMessage::class.java)
        }

        /**
         * 构建心跳消息。
         *
         * @return 心跳消息
         */
        fun heartbeat(): WebSocketMessage = WebSocketMessage(
            type = TYPE_HEARTBEAT,
            payload = "ping",
        )
    }

    /**
     * 转换为JSON字符串。
     *
     * @return JSON字符串
     */
    fun toJson(): String {
        return gson.toJson(this)
    }

    /**
     * 是否为心跳消息。
     *
     * @return true如果是心跳
     */
    fun isHeartbeat(): Boolean = type == TYPE_HEARTBEAT
}
