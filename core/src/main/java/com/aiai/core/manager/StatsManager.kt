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
 * 统计管理器。
 *
 * 提供使用统计、事件追踪、数据上报等功能。
 */
object StatsManager {

    private const val TAG = "StatsManager"

    /**
     * 统计事件数据类。
     *
     * @property eventName 事件名称
     * @property properties 事件属性
     * @property timestamp 时间戳
     */
    data class StatsEvent(
        val eventName: String,
        val properties: Map<String, Any> = emptyMap(),
        val timestamp: Long = System.currentTimeMillis()
    )

    private var isEnabled: Boolean = true
    private val eventQueue = mutableListOf<StatsEvent>()
    private val screenViewTimes = mutableMapOf<String, Long>()

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "StatsManager initialized")
    }

    /**
     * 设置统计开关。
     *
     * @param enabled 是否启用
     */
    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        Log.d(TAG, "Stats enabled: $enabled")
    }

    /**
     * 是否启用统计。
     *
     * @return 是否启用
     */
    fun isEnabled(): Boolean = isEnabled

    /**
     * 追踪事件。
     *
     * @param eventName 事件名称
     * @param properties 事件属性
     */
    fun trackEvent(eventName: String, properties: Map<String, Any> = emptyMap()) {
        if (!isEnabled) return

        val event = StatsEvent(eventName, properties)
        eventQueue.add(event)

        Log.d(TAG, "Event tracked: $eventName, props: $properties")

        // 队列满时上报
        if (eventQueue.size >= 20) {
            flush()
        }
    }

    /**
     * 页面开始浏览。
     *
     * @param screenName 页面名称
     */
    fun onScreenStart(screenName: String) {
        if (!isEnabled) return
        screenViewTimes[screenName] = System.currentTimeMillis()
        Log.d(TAG, "Screen start: $screenName")
    }

    /**
     * 页面结束浏览。
     *
     * @param screenName 页面名称
     */
    fun onScreenEnd(screenName: String) {
        if (!isEnabled) return
        val startTime = screenViewTimes.remove(screenName)
        if (startTime != null) {
            val duration = System.currentTimeMillis() - startTime
            trackEvent("screen_view", mapOf(
                "screen_name" to screenName,
                "duration_ms" to duration
            ))
        }
        Log.d(TAG, "Screen end: $screenName")
    }

    /**
     * 设置用户属性。
     *
     * @param properties 用户属性
     */
    fun setUserProperties(properties: Map<String, Any>) {
        if (!isEnabled) return
        Log.d(TAG, "User properties set: $properties")
    }

    /**
     * 上报事件。
     */
    fun flush() {
        if (eventQueue.isEmpty()) return

        Log.d(TAG, "Flushing ${eventQueue.size} events")
        // 实际实现需要发送到服务器
        eventQueue.clear()
    }

    /**
     * 获取队列中的事件数。
     *
     * @return 事件数
     */
    fun pendingEventCount(): Int = eventQueue.size

    /**
     * 清除所有统计数据。
     */
    fun clear() {
        eventQueue.clear()
        screenViewTimes.clear()
        Log.d(TAG, "Stats cleared")
    }
}
