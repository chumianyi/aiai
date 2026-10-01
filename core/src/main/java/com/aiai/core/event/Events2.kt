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
package com.aiai.core.event

/**
 * 事件基类。
 */
abstract class Event {
    val timestamp: Long = System.currentTimeMillis()
}

/** 登录成功事件。 */
class LoginSuccessEvent(val userId: String, val userName: String) : Event()

/** 登出事件。 */
class LogoutEvent(val reason: String = "") : Event()

/** 网络状态变化事件。 */
class NetworkChangedEvent(val isAvailable: Boolean, val type: String = "") : Event()

/** 主题变化事件。 */
class ThemeChangedEvent(val mode: Int) : Event()

/** 语言变化事件。 */
class LanguageChangedEvent(val language: String) : Event()

/** 消息通知事件。 */
class MessageEvent(val title: String, val content: String) : Event()

/** 用户信息更新事件。 */
class UserInfoUpdateEvent(val userId: String) : Event()

/** 数据刷新事件。 */
class DataRefreshEvent(val key: String) : Event()

/** 页面切换事件。 */
class PageSwitchEvent(val from: String, val to: String) : Event()

/** 错误事件。 */
class ErrorEvent(val code: Int, val message: String) : Event()
