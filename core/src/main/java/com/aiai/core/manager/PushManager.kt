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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.core.manager

import android.util.Log

/**
 * 推送管理器。
 *
 * 提供推送通知注册、接收、处理等功能。
 */
object PushManager {

    private const val TAG = "PushManager"

    /**
     * 推送消息数据类。
     *
     * @property title 标题
     * @property content 内容
     * @property extras 额外数据
     * @property timestamp 时间戳
     */
    data class PushMessage(
        val title: String,
        val content: String,
        val extras: Map<String, String> = emptyMap(),
        val timestamp: Long = System.currentTimeMillis()
    )

    private var pushToken: String? = null
    private var isPushEnabled: Boolean = true
    private var onMessageReceivedListener: ((PushMessage) -> Unit)? = null
    private val messageHistory = mutableListOf<PushMessage>()

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "PushManager initialized")
    }

    /**
     * 设置推送 Token。
     *
     * @param token 推送 Token
     */
    fun setPushToken(token: String) {
        pushToken = token
        Log.d(TAG, "Push token set: ${token.take(10)}...")
    }

    /**
     * 获取推送 Token。
     *
     * @return 推送 Token
     */
    fun getPushToken(): String? = pushToken

    /**
     * 启用/禁用推送。
     *
     * @param enabled 是否启用
     */
    fun setPushEnabled(enabled: Boolean) {
        isPushEnabled = enabled
        Log.d(TAG, "Push enabled: $enabled")
    }

    /**
     * 是否启用推送。
     *
     * @return 是否启用
     */
    fun isPushEnabled(): Boolean = isPushEnabled

    /**
     * 处理收到的推送消息。
     *
     * @param message 推送消息
     */
    fun onMessageReceived(message: PushMessage) {
        if (!isPushEnabled) {
            Log.d(TAG, "Push disabled, ignoring message")
            return
        }

        messageHistory.add(0, message)
        if (messageHistory.size > 100) {
            messageHistory.removeAt(messageHistory.size - 1)
        }

        Log.d(TAG, "Push message received: ${message.title}")
        onMessageReceivedListener?.invoke(message)
    }

    /**
     * 设置消息接收监听。
     *
     * @param listener 监听器
     */
    fun setOnMessageReceivedListener(listener: (PushMessage) -> Unit) {
        onMessageReceivedListener = listener
    }

    /**
     * 获取消息历史。
     *
     * @return 消息历史列表
     */
    fun getMessageHistory(): List<PushMessage> {
        return messageHistory.toList()
    }

    /**
     * 清除消息历史。
     */
    fun clearMessageHistory() {
        messageHistory.clear()
        Log.d(TAG, "Push message history cleared")
    }

    /**
     * 订阅主题。
     *
     * @param topic 主题
     */
    fun subscribeTopic(topic: String) {
        Log.d(TAG, "Subscribed to topic: $topic")
    }

    /**
     * 取消订阅主题。
     *
     * @param topic 主题
     */
    fun unsubscribeTopic(topic: String) {
        Log.d(TAG, "Unsubscribed from topic: $topic")
    }
}
