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
 * 流式增量内容模型。
 *
 * SSE流式响应中每次推送的增量数据。
 *
 * @property role 角色（首次chunk时包含）
 * @property content 增量文本内容
 * @property toolCalls 增量工具调用
 */
data class StreamDelta(
    @SerializedName("role")
    val role: String? = null,

    @SerializedName("content")
    val content: String = "",

    @SerializedName("tool_calls")
    val toolCalls: List<ToolCallResponse>? = null,
) {
    /**
     * 是否为空增量。
     *
     * @return true如果内容为空且无工具调用
     */
    fun isEmpty(): Boolean {
        return content.isEmpty() && toolCalls.isNullOrEmpty()
    }
}
