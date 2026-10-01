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
package com.aiai.data.model

/**
 * 聊天状态。
 */
sealed class ChatState {
    /** 空闲 */
    object Idle : ChatState()

    /** 加载中 */
    object Loading : ChatState()

    /** 流式输出中 */
    data class Streaming(val partialContent: String = "") : ChatState()

    /** 成功 */
    data class Success(val message: Message) : ChatState()

    /** 错误 */
    data class Error(val message: String, val throwable: Throwable? = null) : ChatState()

    /** 空状态 */
    object Empty : ChatState()
}
