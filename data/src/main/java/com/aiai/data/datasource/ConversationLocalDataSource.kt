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
package com.aiai.data.datasource

import com.aiai.data.dao.ConversationDao
import com.aiai.data.entity.ConversationEntity

/** 会话本地数据源 */
class ConversationLocalDataSource(
    private val conversationDao: ConversationDao,
) : LocalDataSource<ConversationEntity, String>() {
    override suspend fun getById(id: String): ConversationEntity? = conversationDao.getConversationById(id)
    override suspend fun insert(entity: ConversationEntity) = conversationDao.insert(entity)
    override suspend fun insertAll(entities: List<ConversationEntity>) = conversationDao.insertAll(entities)
    override suspend fun update(entity: ConversationEntity) = conversationDao.update(entity)
    override suspend fun delete(entity: ConversationEntity) = conversationDao.delete(entity)
    override suspend fun deleteById(id: String) = conversationDao.deleteById(id)
    override suspend fun getAll(): List<ConversationEntity> = emptyList()
    override suspend fun clear() {}
}
