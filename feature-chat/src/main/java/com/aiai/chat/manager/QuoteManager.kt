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
package com.aiai.chat.manager

import android.content.Context
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息引用管理器
 *
 * 管理消息的引用和回复功能。
 */
class QuoteManager(private val context: Context) {

    data class QuoteInfo(
        val quotedMessageId: String,
        val quotedContent: String,
        val quotedRole: String
    )

    private val _currentQuote = MutableStateFlow<QuoteInfo?>(null)
    val currentQuote: StateFlow<QuoteInfo?> = _currentQuote.asStateFlow()

    fun setQuote(message: ChatMessage) {
        _currentQuote.value = QuoteInfo(
            quotedMessageId = message.id,
            quotedContent = message.content,
            quotedRole = message.role.name
        )
    }

    fun clearQuote() {
        _currentQuote.value = null
    }

    fun hasQuote(): Boolean {
        return _currentQuote.value != null
    }
}
