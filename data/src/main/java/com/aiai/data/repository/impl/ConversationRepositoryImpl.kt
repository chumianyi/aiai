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
package com.aiai.data.repository.impl

import com.aiai.data.dao.ConversationDao
import com.aiai.data.entity.ConversationEntity
import com.aiai.data.repository.ConversationRepository
import com.aiai.network.api.ConversationApiService
import kotlinx.coroutines.flow.Flow

/** 会话仓库实现 */
class ConversationRepositoryImpl(
    private val conversationDao: ConversationDao,
    private val conversationApi: ConversationApiService,
) : ConversationRepository {

    override fun getConversations(): Flow<List<ConversationEntity>> = conversationDao.getAllConversations()
    override fun getArchivedConversations(): Flow<List<ConversationEntity>> = conversationDao.getArchivedConversations()
    override suspend fun getConversationById(id: String): ConversationEntity? = conversationDao.getConversationById(id)
    override suspend fun createConversation(conversation: ConversationEntity): Result<String> {
        conversationDao.insert(conversation)
        return Result.success(conversation.id)
    }
    override suspend fun updateConversation(conversation: ConversationEntity) = conversationDao.update(conversation)
    override suspend fun deleteConversation(id: String) = conversationDao.deleteById(id)
    override suspend fun renameConversation(id: String, title: String) = conversationDao.updateTitle(id, title)
    override suspend fun setPinned(id: String, pinned: Boolean) = conversationDao.setPinned(id, pinned)
    override suspend fun setArchived(id: String, archived: Boolean) = conversationDao.setArchived(id, archived)
    override fun searchConversations(keyword: String): Flow<List<ConversationEntity>> = conversationDao.searchConversations(keyword)
    override suspend fun updateMessageCount(id: String, count: Int) = conversationDao.updateMessageCount(id, count)
    override suspend fun getConversationCount(): Int = conversationDao.getConversationCount()
}
