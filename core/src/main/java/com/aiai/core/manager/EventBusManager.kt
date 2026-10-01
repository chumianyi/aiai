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
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 事件总线管理器。
 *
 * 提供事件订阅、发布、粘性事件等功能。
 */
object EventBusManager {

    private const val TAG = "EventBusManager"

    private val eventBus = EventBus.builder()
        .logNoSubscriberMessages(false)
        .sendNoSubscriberEvent(false)
        .build()

    /**
     * 注册订阅者。
     *
     * @param subscriber 订阅者
     */
    fun register(subscriber: Any) {
        try {
            if (!eventBus.isRegistered(subscriber)) {
                eventBus.register(subscriber)
                Log.d(TAG, "Registered: ${subscriber.javaClass.simpleName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register failed", e)
        }
    }

    /**
     * 取消注册。
     *
     * @param subscriber 订阅者
     */
    fun unregister(subscriber: Any) {
        try {
            if (eventBus.isRegistered(subscriber)) {
                eventBus.unregister(subscriber)
                Log.d(TAG, "Unregistered: ${subscriber.javaClass.simpleName}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Unregister failed", e)
        }
    }

    /**
     * 发布事件。
     *
     * @param event 事件
     */
    fun post(event: Any) {
        try {
            eventBus.post(event)
            Log.d(TAG, "Posted: ${event.javaClass.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Post failed", e)
        }
    }

    /**
     * 发布粘性事件。
     *
     * @param event 事件
     */
    fun postSticky(event: Any) {
        try {
            eventBus.postSticky(event)
            Log.d(TAG, "Posted sticky: ${event.javaClass.simpleName}")
        } catch (e: Exception) {
            Log.e(TAG, "Post sticky failed", e)
        }
    }

    /**
     * 获取粘性事件。
     *
     * @param eventType 事件类型
     * @return 粘性事件
     */
    fun <T> getStickyEvent(eventType: Class<T>): T? {
        return try {
            eventBus.getStickyEvent(eventType)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 移除粘性事件。
     *
     * @param eventType 事件类型
     * @return 移除的事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T? {
        return try {
            eventBus.removeStickyEvent(eventType)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 移除所有粘性事件。
     */
    fun removeAllStickyEvents() {
        try {
            eventBus.removeAllStickyEvents()
        } catch (e: Exception) {
            Log.e(TAG, "Remove all sticky events failed", e)
        }
    }

    /**
     * 检查是否已注册。
     *
     * @param subscriber 订阅者
     * @return 是否已注册
     */
    fun isRegistered(subscriber: Any): Boolean {
        return eventBus.isRegistered(subscriber)
    }
}

/**
 * 基础事件。
 */
open class BaseEvent {
    val timestamp: Long = System.currentTimeMillis()
}

/**
 * 消息事件。
 *
 * @property message 消息内容
 */
data class MessageEvent(val message: String) : BaseEvent()

/**
 * 登录事件。
 *
 * @property isLoggedIn 是否已登录
 */
data class LoginEvent(val isLoggedIn: Boolean) : BaseEvent()

/**
 * 主题变更事件。
 *
 * @property themeType 主题类型
 */
data class ThemeChangedEvent(val themeType: Int) : BaseEvent()

/**
 * 语言变更事件。
 *
 * @property languageCode 语言代码
 */
data class LanguageChangedEvent(val languageCode: String) : BaseEvent()

/**
 * 网络状态事件。
 *
 * @property isAvailable 网络是否可用
 * @property type 网络类型
 */
data class NetworkStateEvent(val isAvailable: Boolean, val type: String) : BaseEvent()
