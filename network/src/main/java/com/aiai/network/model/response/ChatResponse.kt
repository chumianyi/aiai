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
 * 聊天响应模型。
 *
 * 表示一次聊天请求的完整响应。
 *
 * @property id 响应唯一ID
 * @property object 对象类型（chat.completion）
 * @property created 创建时间戳
 * @property model 使用的模型名称
 * @property choices 生成的选项列表
 * @property usage token使用量统计
 */
data class ChatResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("object")
    val objectType: String = "chat.completion",

    @SerializedName("created")
    val created: Long = 0L,

    @SerializedName("model")
    val model: String = "",

    @SerializedName("choices")
    val choices: List<ChatChoice> = emptyList(),

    @SerializedName("usage")
    val usage: Usage = Usage(),
) {
    /**
     * 获取第一个选项的消息内容。
     *
     * @return 消息内容，如果无选项返回空字符串
     */
    fun firstMessageContent(): String {
        return choices.firstOrNull()?.message?.content ?: ""
    }

    /**
     * 获取完成原因。
     *
     * @return finish_reason
     */
    fun finishReason(): String {
        return choices.firstOrNull()?.finishReason ?: "unknown"
    }
}
