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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.MessageDao
import com.aiai.data.dao.ConversationDao
import com.aiai.data.dao.FavoriteDao
import com.aiai.data.dao.NotificationDao
import com.aiai.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * 消息搜索管理器。
 *
 * 提供全局消息搜索、会话内搜索等功能。
 *
 * @property messageDao 消息DAO
 * @property conversationDao 会话DAO
 * @property favoriteDao 收藏DAO
 */
class MessageSearchManager(
    private val messageDao: MessageDao,
    private val conversationDao: ConversationDao,
    private val favoriteDao: FavoriteDao,
) {

    companion object {
        private const val TAG = "MessageSearchManager"
    }

    /**
     * 全局搜索消息。
     *
     * @param query 搜索关键词
     * @return 搜索结果列表
     */
    suspend fun searchAll(query: String): List<SearchResult> {
        if (query.isBlank()) return emptyList()
        Log.d(TAG, "Searching for: $query")

        val results = mutableListOf<SearchResult>()

        // 搜索消息
        val messages = messageDao.searchMessagesSync(query)
        results.addAll(messages.map { msg ->
            SearchResult(
                type = ResultType.MESSAGE,
                id = msg.id,
                title = msg.content.take(50),
                subtitle = "会话: ${msg.conversationId}",
                timestamp = msg.createdAt,
            )
        })

        // 搜索会话
        val conversations = conversationDao.searchConversationsSync(query)
        results.addAll(conversations.map { conv ->
            SearchResult(
                type = ResultType.CONVERSATION,
                id = conv.id,
                title = conv.title,
                subtitle = "模型: ${conv.modelName}",
                timestamp = conv.updatedAt,
            )
        })

        // 搜索收藏
        val favorites = favoriteDao.searchFavoritesSync(query)
        results.addAll(favorites.map { fav ->
            SearchResult(
                type = ResultType.FAVORITE,
                id = fav.id,
                title = fav.content.take(50),
                subtitle = "分类: ${fav.category}",
                timestamp = fav.createdAt,
            )
        })

        return results.sortedByDescending { it.timestamp }
    }

    /**
     * 按类型搜索。
     */
    suspend fun searchByType(query: String, type: ResultType): List<SearchResult> {
        return when (type) {
            ResultType.MESSAGE -> {
                messageDao.searchMessagesSync(query).map {
                    SearchResult(type, it.id, it.content.take(50), it.conversationId, it.createdAt)
                }
            }
            ResultType.CONVERSATION -> {
                conversationDao.searchConversationsSync(query).map {
                    SearchResult(type, it.id, it.title, it.modelName, it.updatedAt)
                }
            }
            ResultType.FAVORITE -> {
                favoriteDao.searchFavoritesSync(query).map {
                    SearchResult(type, it.id, it.content.take(50), it.category, it.createdAt)
                }
            }
        }
    }

    /**
     * 搜索建议。
     *
     * @param prefix 关键词前缀
     * @return 建议列表
     */
    suspend fun getSearchSuggestions(prefix: String): List<String> {
        if (prefix.isBlank() || prefix.length < 2) return emptyList()
        val messages = messageDao.searchMessagesSync(prefix)
        return messages.map { it.content.take(20) }.distinct().take(10)
    }

    /**
     * 搜索结果数据类。
     */
    data class SearchResult(
        val type: ResultType,
        val id: String,
        val title: String,
        val subtitle: String,
        val timestamp: Long,
    )

    /**
     * 搜索结果类型。
     */
    enum class ResultType {
        MESSAGE,
        CONVERSATION,
        FAVORITE,
    }
}
