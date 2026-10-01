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
 * 消息标签管理器
 *
 * 管理单条消息的标签和分类。
 */
class MessageTagManager(private val context: Context) {

    data class MessageTag(
        val id: String,
        val name: String,
        val color: Int
    )

    private val _tags = MutableStateFlow<List<MessageTag>>(emptyList())
    val tags: StateFlow<List<MessageTag>> = _tags.asStateFlow()

    private val _messageTags = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val messageTags: StateFlow<Map<String, List<String>>> = _messageTags.asStateFlow()

    init {
        loadDefaultTags()
    }

    private fun loadDefaultTags() {
        _tags.value = listOf(
            MessageTag("important", "重要", 0xFFE53935.toInt()),
            MessageTag("todo", "待办", 0xFFFB8C00.toInt()),
            MessageTag("idea", "灵感", 0xFF43A047.toInt()),
            MessageTag("question", "疑问", 0xFF1E88E5.toInt())
        )
    }

    fun addTagToMessage(messageId: String, tagId: String) {
        val current = _messageTags.value[messageId] ?: emptyList()
        if (tagId !in current) {
            _messageTags.value = _messageTags.value + (messageId to (current + tagId))
        }
    }

    fun removeTagFromMessage(messageId: String, tagId: String) {
        val current = _messageTags.value[messageId] ?: return
        _messageTags.value = _messageTags.value + (messageId to current - tagId)
    }

    fun getTagsForMessage(messageId: String): List<MessageTag> {
        val tagIds = _messageTags.value[messageId] ?: return emptyList()
        return _tags.value.filter { it.id in tagIds }
    }

    fun getMessagesByTag(tagId: String): List<String> {
        return _messageTags.value.filter { (_, tags) -> tagId in tags }.keys.toList()
    }
}
