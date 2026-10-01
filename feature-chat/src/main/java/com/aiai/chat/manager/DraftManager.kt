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
 * 草稿管理器
 *
 * 管理会话的输入草稿，防止意外丢失。
 */
class DraftManager(private val context: Context) {

    private val _drafts = MutableStateFlow<Map<String, String>>(emptyMap())
    val drafts: StateFlow<Map<String, String>> = _drafts.asStateFlow()

    fun saveDraft(conversationId: String, content: String) {
        if (content.isBlank()) {
            removeDraft(conversationId)
        } else {
            _drafts.value = _drafts.value + (conversationId to content)
        }
    }

    fun getDraft(conversationId: String): String {
        return _drafts.value[conversationId] ?: ""
    }

    fun removeDraft(conversationId: String) {
        _drafts.value = _drafts.value - conversationId
    }

    fun hasDraft(conversationId: String): Boolean {
        return _drafts.value[conversationId]?.isNotBlank() == true
    }

    fun clearAllDrafts() {
        _drafts.value = emptyMap()
    }

    fun getDraftCount(): Int {
        return _drafts.value.size
    }
}
