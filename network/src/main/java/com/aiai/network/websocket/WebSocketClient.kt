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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * WebSocket客户端，管理WebSocket连接的生命周期。
 *
 * 功能：
 * - 建立和断开WebSocket连接
 * - 自动重连（指数退避策略）
 * - 心跳保活
 * - 消息收发
 * - 消息队列（断线时缓存消息）
 *
 * @property config API配置
 */
class WebSocketClient(
    private val config: ApiConfig = ApiConfig.getInstance(),
) {

    companion object {
        private const val TAG = "WebSocketClient"
        private const val NORMAL_CLOSE_CODE = 1000
        private const val MAX_MESSAGE_QUEUE_SIZE = 100
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .pingInterval(config.pingIntervalMs, TimeUnit.MILLISECONDS)
            .connectTimeout(config.connectTimeoutMs, TimeUnit.MILLISECONDS)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()
    }

    private var webSocket: WebSocket? = null
    private val isConnected = AtomicBoolean(false)
    private val isManualClose = AtomicBoolean(false)
    private val reconnectAttempts = AtomicInteger(0)
    private val messageQueue = ConcurrentLinkedQueue<WebSocketMessage>()
    private val listeners = mutableListOf<WebSocketListener>()
    private val heartbeatManager = HeartbeatManager(config.pingIntervalMs)
    private val reconnectManager = ReconnectManager(config.maxReconnectAttempts)

    /**
     * 添加事件监听器。
     *
     * @param listener 监听器
     */
    fun addListener(listener: WebSocketListener) {
        synchronized(listeners) {
            if (!listeners.contains(listener)) {
                listeners.add(listener)
            }
        }
    }

    /**
     * 移除事件监听器。
     *
     * @param listener 监听器
     */
    fun removeListener(listener: WebSocketListener) {
        synchronized(listeners) {
            listeners.remove(listener)
        }
    }

    /**
     * 建立WebSocket连接。
     */
    fun connect() {
        if (isConnected.get()) return

        isManualClose.set(false)
        val request = Request.Builder()
            .url(config.webSocketUrl)
            .header("Authorization", "Bearer ${config.apiKey}")
            .header("User-Agent", config.buildUserAgent())
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected.set(true)
                reconnectAttempts.set(0)
                heartbeatManager.start()
                flushMessageQueue()
                notifyConnected()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                notifyRawMessage(text)
                try {
                    val message = WebSocketMessage.fromJson(text)
                    notifyMessage(message)
                } catch (e: Exception) {
                    // 忽略解析失败的消息
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                webSocket.close(code, reason)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                isConnected.set(false)
                heartbeatManager.stop()
                notifyDisconnected(code, reason)

                if (!isManualClose.get()) {
                    scheduleReconnect()
                } else {
                    notifyClosed()
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                isConnected.set(false)
                heartbeatManager.stop()
                notifyError(t)

                if (!isManualClose.get()) {
                    scheduleReconnect()
                }
            }
        })
    }

    /**
     * 断开WebSocket连接。
     */
    fun disconnect() {
        isManualClose.set(true)
        reconnectManager.cancel()
        heartbeatManager.stop()
        webSocket?.close(NORMAL_CLOSE_CODE, "client manual close")
        webSocket = null
        isConnected.set(false)
    }

    /**
     * 发送文本消息。
     *
     * @param message 消息内容
     * @return true如果发送成功
     */
    fun sendMessage(message: WebSocketMessage): Boolean {
        if (isConnected.get()) {
            val json = message.toJson()
            return webSocket?.send(json) ?: false
        } else {
            // 断线时加入队列
            if (messageQueue.size < MAX_MESSAGE_QUEUE_SIZE) {
                messageQueue.offer(message)
            }
            return false
        }
    }

    /**
     * 是否已连接。
     *
     * @return true如果已连接
     */
    fun isConnected(): Boolean = isConnected.get()

    /**
     * 调度重连。
     */
    private fun scheduleReconnect() {
        val attempt = reconnectAttempts.incrementAndGet()
        val delayMs = reconnectManager.calculateDelay(attempt)

        notifyReconnecting(attempt, delayMs)

        reconnectManager.scheduleReconnect(delayMs) {
            connect()
        }
    }

    /**
     * 重发队列中的消息。
     */
    private fun flushMessageQueue() {
        while (messageQueue.isNotEmpty()) {
            val message = messageQueue.poll() ?: break
            webSocket?.send(message.toJson())
        }
    }

    private fun notifyConnected() = listeners.forEach { it.onConnected() }
    private fun notifyDisconnected(code: Int, reason: String) = listeners.forEach { it.onDisconnected(code, reason) }
    private fun notifyMessage(message: WebSocketMessage) = listeners.forEach { it.onMessage(message) }
    private fun notifyRawMessage(text: String) = listeners.forEach { it.onRawMessage(text) }
    private fun notifyError(error: Throwable) = listeners.forEach { it.onError(error) }
    private fun notifyReconnecting(attempt: Int, delayMs: Long) = listeners.forEach { it.onReconnecting(attempt, delayMs) }
    private fun notifyClosed() = listeners.forEach { it.onClosed() }
}
