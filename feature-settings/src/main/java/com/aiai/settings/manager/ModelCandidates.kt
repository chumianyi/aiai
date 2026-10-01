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
 * 模型名候选列表。
 */
object ModelCandidates {

    fun list(provider: String): List<String> = when (provider) {
        "openai" -> listOf("gpt-4o", "gpt-4o-mini", "gpt-4-turbo", "gpt-3.5-turbo")
        "anthropic" -> listOf("claude-opus-4", "claude-sonnet-4", "claude-3-haiku")
        "local" -> listOf("llama3", "qwen2", "mistral", "gemma")
        else -> listOf("gpt-4o", "gpt-4o-mini")
    }
}
