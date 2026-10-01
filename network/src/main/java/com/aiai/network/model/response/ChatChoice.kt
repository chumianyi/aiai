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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 聊天选择项模型。
 *
 * @property index 选项索引
 * @property message 响应消息
 * @property finishReason 完成原因（stop/length/content_filter）
 */
data class ChatChoice(
    @SerializedName("index")
    val index: Int = 0,

    @SerializedName("message")
    val message: ChatMessage = ChatMessage(),

    @SerializedName("finish_reason")
    val finishReason: String = "stop",
)
