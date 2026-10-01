/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * API 端点候选列表。
 */
object ApiEndpoints {

    data class Endpoint(val name: String, val url: String)

    fun common(): List<Endpoint> = listOf(
        Endpoint("OpenAI 官方", "https://api.openai.com/v1"),
        Endpoint("Azure OpenAI", "https://your-resource.openai.azure.com"),
        Endpoint("Anthropic Claude", "https://api.anthropic.com"),
        Endpoint("本地 Ollama", "http://localhost:11434"),
        Endpoint("自定义", "")
    )
}
