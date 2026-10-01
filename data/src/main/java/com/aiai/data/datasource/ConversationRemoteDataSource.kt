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

import com.aiai.data.entity.ConversationEntity
import com.aiai.network.api.ConversationApiService
import com.aiai.network.model.response.ConversationResponse

/** 会话远程数据源 */
class ConversationRemoteDataSource(
    private val conversationApi: ConversationApiService,
) : RemoteDataSource<ConversationResponse>() {
    override suspend fun fetch(): Result<ConversationResponse> = Result.success(ConversationResponse())
    override suspend fun post(data: ConversationResponse): Result<ConversationResponse> = Result.success(data)
    override suspend fun update(data: ConversationResponse): Result<ConversationResponse> = Result.success(data)
    override suspend fun delete(id: String): Result<Unit> {
        return try { conversationApi.deleteConversation(id); Result.success(Unit) }
        catch (e: Exception) { Result.failure(e) }
    }
}
