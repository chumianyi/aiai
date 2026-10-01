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
import com.aiai.chat.data.model.Conversation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 会话标签管理器
 *
 * 管理会话的标签分类和文件夹组织。
 */
class ConversationTagManager(private val context: Context) {

    data class Tag(
        val id: String,
        val name: String,
        val color: Int,
        val count: Int = 0
    )

    private val _tags = MutableStateFlow<List<Tag>>(emptyList())
    val tags: StateFlow<List<Tag>> = _tags.asStateFlow()

    private val _folders = MutableStateFlow<List<String>>(emptyList())
    val folders: StateFlow<List<String>> = _folders.asStateFlow()

    init {
        loadDefaultTags()
    }

    private fun loadDefaultTags() {
        _tags.value = listOf(
            Tag("work", "工作", 0xFF6C63FF.toInt()),
            Tag("study", "学习", 0xFF4CAF50.toInt()),
            Tag("life", "生活", 0xFFFF9800.toInt()),
            Tag("code", "编程", 0xFF2196F3.toInt()),
            Tag("write", "写作", 0xFFE91E63.toInt())
        )
        _folders.value = listOf("默认", "工作", "学习", "收藏")
    }

    fun addTag(name: String, color: Int) {
        val tag = Tag(id = "tag_${System.currentTimeMillis()}", name = name, color = color)
        _tags.value = _tags.value + tag
    }

    fun removeTag(tagId: String) {
        _tags.value = _tags.value.filterNot { it.id == tagId }
    }

    fun addFolder(name: String) {
        if (name !in _folders.value) {
            _folders.value = _folders.value + name
        }
    }

    fun removeFolder(name: String) {
        if (name != "默认") {
            _folders.value = _folders.value.filterNot { it == name }
        }
    }

    fun getConversationsByTag(conversations: List<Conversation>, tagName: String): List<Conversation> {
        return conversations.filter { conv ->
            conv.tags.contains(tagName)
        }
    }

    fun getConversationsByFolder(conversations: List<Conversation>, folder: String): List<Conversation> {
        return conversations.filter { conv ->
            conv.folder == folder || (folder == "默认" && conv.folder == null)
        }
    }
}
