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
package com.aiai.chat.sorter

import com.aiai.chat.data.model.Conversation

/**
 * 会话排序器
 *
 * 会话排序规则：置顶会话优先，其余按最后更新时间倒序。
 */
object ConversationSorter {

    /**
     * 排序会话列表
     *
     * 排序规则：
     * 1. 置顶会话排在前面
     * 2. 同组内按最后更新时间倒序
     * 3. 收藏会话优先于普通会话
     */
    fun sort(conversations: List<Conversation>): List<Conversation> {
        return conversations.sortedWith(
            compareByDescending<Conversation> { it.isPinned }
                .thenByDescending { it.isFavorite }
                .thenByDescending { it.updatedAt }
        )
    }

    /**
     * 按文件夹分组排序
     */
    fun sortByFolder(conversations: List<Conversation>): Map<String, List<Conversation>> {
        val sorted = sort(conversations)
        return sorted.groupBy { it.folder ?: "默认" }
    }

    /**
     * 搜索过滤
     */
    fun filter(conversations: List<Conversation>, query: String): List<Conversation> {
        if (query.isBlank()) return conversations
        return conversations.filter { conv ->
            conv.title.contains(query, ignoreCase = true) ||
            conv.lastMessage.contains(query, ignoreCase = true)
        }
    }
}
