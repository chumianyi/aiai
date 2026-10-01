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

import com.aiai.common.model.Event

/**
 * 登录成功事件。
 */
class LoginSuccessEvent(val userId: String, val token: String) : Event()

/**
 * 登出事件。
 */
class LogoutEvent(val reason: String = "") : Event()

/**
 * 主题变更事件。
 */
class ThemeChangedEvent(val themeMode: Int) : Event()

/**
 * 语言变更事件。
 */
class LanguageChangedEvent(val language: String) : Event()

/**
 * 网络状态变更事件。
 */
class NetworkChangedEvent(val isAvailable: Boolean, val type: String) : Event()

/**
 * 新聊天消息事件。
 */
class NewMessageEvent(val sessionId: String, val messageId: String, val content: String) : Event()

/**
 * 消息发送成功事件。
 */
class MessageSentEvent(val messageId: String, val sessionId: String) : Event()

/**
 * 消息发送失败事件。
 */
class MessageSendFailedEvent(val messageId: String, val error: String) : Event()

/**
 * 会话更新事件。
 */
class SessionUpdatedEvent(val sessionId: String) : Event()

/**
 * 配置变更事件。
 */
class ConfigChangedEvent(val key: String, val value: Any?) : Event()

/**
 * 用户信息更新事件。
 */
class UserInfoUpdatedEvent(val userId: String, val avatar: String, val name: String) : Event()
