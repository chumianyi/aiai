/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.data.enums

/**
 * 消息角色枚举
 *
 * 标识消息的发送方角色，用于区分UI样式和业务逻辑。
 */
enum class MessageRole {
    /** 用户发送的消息 */
    USER,

    /** AI助手回复的消息 */
    ASSISTANT,

    /** 系统消息（提示、通知等） */
    SYSTEM,

    /** 工具调用消息 */
    TOOL
}
