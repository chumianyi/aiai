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
import com.aiai.data.entity.MessageEntity
import com.aiai.data.entity.ConversationEntity

/**
 * 消息过滤管理器。
 *
 * 提供消息筛选、过滤等功能。
 */
class MessageFilterManager {

    companion object {
        private const val TAG = "MessageFilterManager"
    }

    /**
     * 按角色过滤消息。
     */
    fun filterByRole(messages: List<MessageEntity>, role: String): List<MessageEntity> {
        return messages.filter { it.role == role }
    }

    /**
     * 按时间范围过滤消息。
     */
    fun filterByTimeRange(messages: List<MessageEntity>, start: Long, end: Long): List<MessageEntity> {
        return messages.filter { it.createdAt in start..end }
    }

    /**
     * 按消息类型过滤。
     */
    fun filterByType(messages: List<MessageEntity>, messageType: String): List<MessageEntity> {
        return messages.filter { it.messageType == messageType }
    }

    /**
     * 按状态过滤。
     */
    fun filterByStatus(messages: List<MessageEntity>, status: String): List<MessageEntity> {
        return messages.filter { it.status == status }
    }

    /**
     * 过滤包含附件的消息。
     */
    fun filterWithAttachments(messages: List<MessageEntity>): List<MessageEntity> {
        return messages.filter { it.extraJson.contains("attachment") }
    }

    /**
     * 过滤错误消息。
     */
    fun filterErrorMessages(messages: List<MessageEntity>): List<MessageEntity> {
        return messages.filter { it.status == "error" }
    }

    /**
     * 按token数过滤。
     */
    fun filterByTokenCount(messages: List<MessageEntity>, minTokens: Int): List<MessageEntity> {
        return messages.filter { it.tokenCount >= minTokens }
    }

    /**
     * 排序选项。
     */
    enum class SortOrder {
        BY_TIME_ASC,
        BY_TIME_DESC,
        BY_TOKEN_COUNT,
    }

    /**
     * 排序消息。
     */
    fun sortMessages(messages: List<MessageEntity>, order: SortOrder): List<MessageEntity> {
        return when (order) {
            SortOrder.BY_TIME_ASC -> messages.sortedBy { it.createdAt }
            SortOrder.BY_TIME_DESC -> messages.sortedByDescending { it.createdAt }
            SortOrder.BY_TOKEN_COUNT -> messages.sortedByDescending { it.tokenCount }
        }
    }

    /**
     * 过滤会话列表。
     */
    fun filterConversations(
        conversations: List<ConversationEntity>,
        showPinnedOnly: Boolean = false,
        showArchived: Boolean = false,
        searchQuery: String = "",
    ): List<ConversationEntity> {
        var result = conversations
        if (showPinnedOnly) result = result.filter { it.isPinned }
        if (!showArchived) result = result.filter { !it.isArchived }
        if (searchQuery.isNotBlank()) {
            result = result.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
        return result.sortedWith(compareByDescending<ConversationEntity> { it.isPinned }.thenByDescending { it.updatedAt })
    }
}
