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

import com.aiai.network.api.ChatApiService
import com.aiai.network.model.request.ChatRequest
import com.aiai.network.model.response.ChatResponse

/**
 * 聊天远程数据源。
 */
class ChatRemoteDataSource(
    private val chatApi: ChatApiService,
) : RemoteDataSource<ChatResponse>() {

    override suspend fun fetch(): Result<ChatResponse> {
        return Result.success(ChatResponse())
    }

    override suspend fun post(data: ChatResponse): Result<ChatResponse> {
        return Result.success(data)
    }

    override suspend fun update(data: ChatResponse): Result<ChatResponse> {
        return Result.success(data)
    }

    override suspend fun delete(id: String): Result<Unit> {
        return try {
            chatApi.deleteMessage(id)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 发送消息。
     */
    suspend fun sendMessage(request: ChatRequest): Result<ChatResponse> {
        return try {
            val response = chatApi.sendMessage(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
