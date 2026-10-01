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
package com.aiai.data.mapper

import com.aiai.data.entity.ConversationEntity
import com.aiai.data.model.Conversation
import com.aiai.network.model.response.ConversationResponse

/**
 * 会话Mapper，Entity↔Domain↔Response转换。
 */
object ConversationMapper {

    fun entityToDomain(entity: ConversationEntity): Conversation {
        return Conversation(
            id = entity.id,
            title = entity.title,
            modelName = entity.modelName,
            systemPrompt = entity.systemPrompt,
            temperature = entity.temperature,
            topP = entity.topP,
            maxTokens = entity.maxTokens,
            isPinned = entity.isPinned,
            isArchived = entity.isArchived,
            messageCount = entity.messageCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun domainToEntity(domain: Conversation): ConversationEntity {
        return ConversationEntity(
            id = domain.id,
            title = domain.title,
            modelName = domain.modelName,
            systemPrompt = domain.systemPrompt,
            temperature = domain.temperature,
            topP = domain.topP,
            maxTokens = domain.maxTokens,
            isPinned = domain.isPinned,
            isArchived = domain.isArchived,
            messageCount = domain.messageCount,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
        )
    }

    fun responseToDomain(response: ConversationResponse): Conversation {
        return Conversation(
            id = response.id,
            title = response.title,
            modelName = response.modelName,
            systemPrompt = response.systemPrompt,
            temperature = response.temperature,
            topP = response.topP,
            maxTokens = response.maxTokens,
            isPinned = response.isPinned,
            isArchived = response.isArchived,
            messageCount = response.messageCount,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis(),
        )
    }

    fun entityListToDomainList(entities: List<ConversationEntity>): List<Conversation> {
        return entities.map { entityToDomain(it) }
    }
}
