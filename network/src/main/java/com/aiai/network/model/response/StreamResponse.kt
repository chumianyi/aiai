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
 * SSE流式响应模型。
 *
 * 表示Server-Sent Events的单次事件数据。
 *
 * @property id 响应ID
 * @property objectType 对象类型
 * @property created 创建时间戳
 * @property model 模型名称
 * @property choices 流式选项列表
 * @property delta 流式增量内容
 * @property finishReason 完成原因
 */
data class StreamResponse(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("object")
    val objectType: String = "chat.completion.chunk",

    @SerializedName("created")
    val created: Long = 0L,

    @SerializedName("model")
    val model: String = "",

    @SerializedName("choices")
    val choices: List<StreamChoice> = emptyList(),

    @SerializedName("finish_reason")
    val finishReason: String? = null,
) {
    /**
     * 是否为结束事件。
     *
     * @return true如果finishReason非空
     */
    fun isFinished(): Boolean = finishReason != null

    /**
     * 获取增量文本内容。
     *
     * @return 增量文本
     */
    fun deltaContent(): String {
        return choices.firstOrNull()?.delta?.content ?: ""
    }
}

/**
 * 流式选项模型。
 *
 * @property index 选项索引
 * @property delta 增量内容
 * @property finishReason 完成原因
 */
data class StreamChoice(
    @SerializedName("index")
    val index: Int = 0,

    @SerializedName("delta")
    val delta: StreamDelta = StreamDelta(),

    @SerializedName("finish_reason")
    val finishReason: String? = null,
)
