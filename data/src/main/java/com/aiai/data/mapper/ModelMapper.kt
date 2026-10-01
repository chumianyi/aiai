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

import com.aiai.data.entity.ModelConfigEntity
import com.aiai.data.model.ModelConfig
import com.aiai.network.model.response.ModelResponse

/**
 * 模型Mapper，Entity↔Domain↔Response转换。
 */
object ModelMapper {

    fun entityToDomain(entity: ModelConfigEntity): ModelConfig {
        return ModelConfig(
            id = entity.id,
            name = entity.name,
            baseUrl = entity.baseUrl,
            apiKey = entity.apiKey,
            modelName = entity.modelName,
            temperature = entity.temperature,
            topP = entity.topP,
            maxTokens = entity.maxTokens,
            isDefault = entity.isDefault,
            isActive = entity.isActive,
        )
    }

    fun domainToEntity(domain: ModelConfig): ModelConfigEntity {
        return ModelConfigEntity(
            id = domain.id,
            name = domain.name,
            baseUrl = domain.baseUrl,
            apiKey = domain.apiKey,
            modelName = domain.modelName,
            temperature = domain.temperature,
            topP = domain.topP,
            maxTokens = domain.maxTokens,
            isDefault = domain.isDefault,
            isActive = domain.isActive,
        )
    }

    fun responseToDomain(response: ModelResponse): ModelConfig {
        return ModelConfig(
            id = response.id,
            name = response.name,
            baseUrl = "",
            apiKey = "",
            modelName = response.id,
            temperature = 0.7,
            topP = 1.0,
            maxTokens = response.maxTokens,
            isDefault = false,
            isActive = response.isAvailable,
        )
    }

    fun entityListToDomainList(entities: List<ModelConfigEntity>): List<ModelConfig> {
        return entities.map { entityToDomain(it) }
    }
}
