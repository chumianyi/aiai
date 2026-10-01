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

/**
 * WebSocket事件监听器接口。
 *
 * 用于接收WebSocket连接的各种事件回调。
 */
interface WebSocketListener {

    /**
     * 连接成功。
     */
    fun onConnected()

    /**
     * 连接断开。
     *
     * @param code 关闭代码
     * @param reason 关闭原因
     */
    fun onDisconnected(code: Int, reason: String)

    /**
     * 收到文本消息。
     *
     * @param message 消息对象
     */
    fun onMessage(message: WebSocketMessage)

    /**
     * 收到原始文本消息。
     *
     * @param text 原始文本
     */
    fun onRawMessage(text: String)

    /**
     * 连接错误。
     *
     * @param error 异常
     */
    fun onError(error: Throwable)

    /**
     * 重连开始。
     *
     * @param attempt 第几次重连
     * @param delayMs 延迟毫秒数
     */
    fun onReconnecting(attempt: Int, delayMs: Long)

    /**
     * 连接已关闭（不再重连）。
     */
    fun onClosed()
}
