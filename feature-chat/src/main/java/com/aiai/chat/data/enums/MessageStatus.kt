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
 * 消息状态枚举
 *
 * 跟踪消息在发送和接收过程中的生命周期状态。
 */
enum class MessageStatus {
    /** 消息发送中 */
    SENDING,

    /** 消息发送成功 */
    SENT,

    /** 消息发送失败 */
    FAILED,

    /** 消息已被对方读取 */
    READ,

    /** 正在流式接收中 */
    STREAMING,

    /** 流式接收完成 */
    STREAM_COMPLETE,

    /** 已停止生成 */
    STOPPED,

    /** 已删除 */
    DELETED
}
