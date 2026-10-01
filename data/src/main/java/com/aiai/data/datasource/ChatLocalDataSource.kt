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

import com.aiai.data.dao.MessageDao
import com.aiai.data.entity.MessageEntity

/**
 * 聊天本地数据源。
 */
class ChatLocalDataSource(
    private val messageDao: MessageDao,
) : LocalDataSource<MessageEntity, String>() {

    override suspend fun getById(id: String): MessageEntity? = messageDao.getLastMessage(id)
    override suspend fun insert(entity: MessageEntity) = messageDao.insert(entity)
    override suspend fun insertAll(entities: List<MessageEntity>) = messageDao.insertAll(entities)
    override suspend fun update(entity: MessageEntity) = messageDao.updateStatus(entity.id, entity.status)
    override suspend fun delete(entity: MessageEntity) = messageDao.delete(entity)
    override suspend fun deleteById(id: String) = messageDao.deleteById(id)
    override suspend fun getAll(): List<MessageEntity> = emptyList()
    override suspend fun clear() {}
}
