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
 * 收藏消息管理器
 *
 * 管理用户收藏的消息，支持添加、删除、列表查询。
 */
class FavoriteManager(private val context: Context) {

    private val _favorites = MutableStateFlow<List<ChatMessage>>(emptyList())
    val favorites: StateFlow<List<ChatMessage>> = _favorites.asStateFlow()

    fun addFavorite(message: ChatMessage) {
        val current = _favorites.value
        if (current.none { it.id == message.id }) {
            _favorites.value = current + message.copy(isFavorite = true)
        }
    }

    fun removeFavorite(messageId: String) {
        _favorites.value = _favorites.value.filterNot { it.id == messageId }
    }

    fun isFavorite(messageId: String): Boolean {
        return _favorites.value.any { it.id == messageId }
    }

    fun clearAll() {
        _favorites.value = emptyList()
    }

    fun searchFavorites(keyword: String): List<ChatMessage> {
        if (keyword.isBlank()) return _favorites.value
        return _favorites.value.filter { it.content.contains(keyword, ignoreCase = true) }
    }
}
