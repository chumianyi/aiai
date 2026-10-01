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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 插件请求模型。
 *
 * 用于插件调用的API请求。
 *
 * @property pluginId 插件ID
 * @property action 执行动作
 * @property params 调用参数
 * @property conversationId 关联会话ID
 * @property messageId 关联消息ID
 * @property async 是否异步执行
 * @property timeoutMs 超时时间（毫秒）
 */
data class PluginRequest(
    @SerializedName("plugin_id")
    val pluginId: String = "",

    @SerializedName("action")
    val action: String = "invoke",

    @SerializedName("params")
    val params: Map<String, Any> = emptyMap(),

    @SerializedName("conversation_id")
    val conversationId: String = "",

    @SerializedName("message_id")
    val messageId: String = "",

    @SerializedName("async")
    val async: Boolean = false,

    @SerializedName("timeout_ms")
    val timeoutMs: Long = 30_000L,
) {
    init {
        require(pluginId.isNotBlank()) { "plugin_id must not be blank" }
        require(timeoutMs > 0) { "timeout_ms must be positive" }
    }
}
