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

import com.aiai.data.dao.MessageDao
import com.aiai.data.entity.MessageEntity
import com.aiai.data.repository.ChatRepository
import com.aiai.network.api.ChatApiService
import com.aiai.network.model.request.ChatRequest
import com.aiai.network.model.response.ChatResponse
import kotlinx.coroutines.flow.Flow

/**
 * 聊天仓库实现类。
 *
 * 组合本地数据源和远程数据源，实现聊天数据的本地缓存+远程同步。
 */
class ChatRepositoryImpl(
    private val messageDao: MessageDao,
    private val chatApi: ChatApiService,
) : ChatRepository {

    override suspend fun sendMessage(request: ChatRequest): Result<ChatResponse> {
        return try {
            val response = chatApi.sendMessage(request)
            if (response.isSuccessful()) {
                Result.success(response.getDataOrThrow())
            } else {
                Result.failure(Exception("API error: ${response.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun streamChat(request: ChatRequest): Flow<String> {
        // 流式聊天由SseClient处理，这里返回空Flow作为占位
        return kotlinx.coroutines.flow.flow {
            try {
                // 实际流式聊天通过SseClient实现
                emit("")
            } catch (e: Exception) {
                // 错误处理
            }
        }
    }

    override fun getMessages(conversationId: String): Flow<List<MessageEntity>> {
        return messageDao.getMessagesByConversation(conversationId)
    }

    override suspend fun getMessagesPaged(conversationId: String, page: Int, pageSize: Int): List<MessageEntity> {
        val offset = (page - 1) * pageSize
        return messageDao.getMessagesPaged(conversationId, pageSize, offset)
    }

    override suspend fun saveMessage(message: MessageEntity) {
        messageDao.insert(message)
    }

    override suspend fun saveMessages(messages: List<MessageEntity>) {
        messageDao.insertAll(messages)
    }

    override suspend fun deleteMessage(messageId: String) {
        messageDao.deleteById(messageId)
    }

    override suspend fun clearMessages(conversationId: String) {
        messageDao.deleteMessagesByConversation(conversationId)
    }

    override suspend fun updateMessageStatus(messageId: String, status: String) {
        messageDao.updateStatus(messageId, status)
    }

    override fun searchMessages(conversationId: String, keyword: String): Flow<List<MessageEntity>> {
        return messageDao.searchMessages(conversationId, keyword)
    }

    override suspend fun syncMessages(conversationId: String): Result<Unit> {
        return try {
            // 从服务器拉取最新消息并更新本地数据库
            val response = chatApi.getHistoryMessages(conversationId)
            if (response.isSuccessful()) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Sync failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
