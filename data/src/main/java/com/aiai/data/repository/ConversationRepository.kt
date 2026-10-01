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
package com.aiai.data.repository

import com.aiai.data.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

/** 会话仓库接口 */
interface ConversationRepository {
    fun getConversations(): Flow<List<ConversationEntity>>
    fun getArchivedConversations(): Flow<List<ConversationEntity>>
    suspend fun getConversationById(id: String): ConversationEntity?
    suspend fun createConversation(conversation: ConversationEntity): Result<String>
    suspend fun updateConversation(conversation: ConversationEntity)
    suspend fun deleteConversation(id: String)
    suspend fun renameConversation(id: String, title: String)
    suspend fun setPinned(id: String, pinned: Boolean)
    suspend fun setArchived(id: String, archived: Boolean)
    fun searchConversations(keyword: String): Flow<List<ConversationEntity>>
    suspend fun updateMessageCount(id: String, count: Int)
    suspend fun getConversationCount(): Int
}
