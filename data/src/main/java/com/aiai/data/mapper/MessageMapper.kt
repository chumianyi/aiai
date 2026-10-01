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

import com.aiai.data.entity.MessageEntity
import com.aiai.data.model.Message
import com.aiai.network.model.response.ChatResponse
import com.aiai.network.model.response.ChatMessage

/**
 * 消息Mapper，Entity↔Domain↔Response转换。
 */
object MessageMapper {

    /** Entity → Domain */
    fun entityToDomain(entity: MessageEntity): Message {
        return Message(
            id = entity.id,
            conversationId = entity.conversationId,
            role = entity.role,
            content = entity.content,
            messageType = entity.messageType,
            status = entity.status,
            createdAt = entity.createdAt,
            tokenCount = entity.tokenCount,
            modelName = entity.modelName,
        )
    }

    /** Domain → Entity */
    fun domainToEntity(domain: Message): MessageEntity {
        return MessageEntity(
            id = domain.id,
            conversationId = domain.conversationId,
            role = domain.role,
            content = domain.content,
            messageType = domain.messageType,
            status = domain.status,
            createdAt = domain.createdAt,
            tokenCount = domain.tokenCount,
            modelName = domain.modelName,
        )
    }

    /** Response → Domain */
    fun responseToDomain(response: ChatResponse, conversationId: String): Message {
        return Message(
            id = response.id,
            conversationId = conversationId,
            role = "assistant",
            content = response.firstMessageContent(),
            messageType = "text",
            status = "sent",
            createdAt = response.created * 1000,
            tokenCount = response.usage.totalTokens,
            modelName = response.model,
        )
    }

    /** ChatMessage → Domain */
    fun chatMessageToDomain(message: ChatMessage, conversationId: String, id: String): Message {
        return Message(
            id = id,
            conversationId = conversationId,
            role = message.role,
            content = message.content,
            messageType = "text",
            status = "sent",
            createdAt = System.currentTimeMillis(),
        )
    }

    /** Entity List → Domain List */
    fun entityListToDomainList(entities: List<MessageEntity>): List<Message> {
        return entities.map { entityToDomain(it) }
    }
}
