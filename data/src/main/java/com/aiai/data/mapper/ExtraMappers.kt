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

import com.aiai.data.entity.FavoriteEntity
import com.aiai.data.entity.NotificationEntity
import com.aiai.data.entity.PluginEntity
import com.aiai.data.entity.SearchHistoryEntity
import com.aiai.data.entity.PromptTemplateEntity
import com.aiai.data.entity.ChatAttachmentEntity
import com.aiai.data.entity.ApiUsageEntity
import com.aiai.data.entity.ExportTaskEntity
import com.aiai.data.model.Favorite
import com.aiai.data.model.Notification
import com.aiai.data.model.Plugin

/**
 * 收藏Entity↔Domain转换。
 */
object FavoriteMapper {

    /**
     * Entity转Domain。
     */
    fun toDomain(entity: FavoriteEntity): Favorite {
        return Favorite(
            id = entity.id,
            messageId = entity.messageId,
            conversationId = entity.conversationId,
            content = entity.content,
            note = entity.note,
            createdAt = entity.createdAt,
            category = entity.category,
        )
    }

    /**
     * Domain转Entity。
     */
    fun toEntity(domain: Favorite): FavoriteEntity {
        return FavoriteEntity(
            id = domain.id,
            messageId = domain.messageId,
            conversationId = domain.conversationId,
            content = domain.content,
            note = domain.note,
            createdAt = domain.createdAt,
            category = domain.category,
        )
    }

    /**
     * Entity列表转Domain列表。
     */
    fun toDomainList(entities: List<FavoriteEntity>): List<Favorite> {
        return entities.map { toDomain(it) }
    }

    /**
     * Domain列表转Entity列表。
     */
    fun toEntityList(domains: List<Favorite>): List<FavoriteEntity> {
        return domains.map { toEntity(it) }
    }
}

/**
 * 通知Entity↔Domain转换。
 */
object NotificationMapper {

    /**
     * Entity转Domain。
     */
    fun toDomain(entity: NotificationEntity): Notification {
        return Notification(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            type = entity.type,
            isRead = entity.isRead,
            createdAt = entity.createdAt,
            actionData = emptyMap(),
        )
    }

    /**
     * Entity列表转Domain列表。
     */
    fun toDomainList(entities: List<NotificationEntity>): List<Notification> {
        return entities.map { toDomain(it) }
    }
}

/**
 * 插件Entity↔Domain转换。
 */
object PluginMapper {

    /**
     * Entity转Domain。
     */
    fun toDomain(entity: PluginEntity): Plugin {
        return Plugin(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            icon = entity.icon,
            version = entity.version,
            isEnabled = entity.isEnabled,
            category = "",
        )
    }

    /**
     * Entity列表转Domain列表。
     */
    fun toDomainList(entities: List<PluginEntity>): List<Plugin> {
        return entities.map { toDomain(it) }
    }
}

/**
 * 搜索历史Entity→Domain（简单数据类）。
 */
data class SearchHistory(
    val id: String,
    val keyword: String,
    val searchType: String,
    val createdAt: Long,
)

/**
 * 搜索历史Mapper。
 */
object SearchHistoryMapper {
    fun toDomain(entity: SearchHistoryEntity): SearchHistory {
        return SearchHistory(
            id = entity.id,
            keyword = entity.keyword,
            searchType = entity.searchType,
            createdAt = entity.createdAt,
        )
    }

    fun toDomainList(entities: List<SearchHistoryEntity>): List<SearchHistory> {
        return entities.map { toDomain(it) }
    }
}

/**
 * 提示词模板Domain模型。
 */
data class PromptTemplate(
    val id: String,
    val title: String,
    val content: String,
    val category: String,
    val tags: List<String>,
    val isCustom: Boolean,
    val isFavorite: Boolean,
    val usageCount: Int,
    val createdAt: Long,
    val updatedAt: Long,
)

/**
 * 提示词模板Mapper。
 */
object PromptTemplateMapper {
    fun toDomain(entity: PromptTemplateEntity): PromptTemplate {
        return PromptTemplate(
            id = entity.id,
            title = entity.title,
            content = entity.content,
            category = entity.category,
            tags = emptyList(),
            isCustom = entity.isCustom,
            isFavorite = entity.isFavorite,
            usageCount = entity.usageCount,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
        )
    }

    fun toDomainList(entities: List<PromptTemplateEntity>): List<PromptTemplate> {
        return entities.map { toDomain(it) }
    }
}

/**
 * 附件Domain模型。
 */
data class Attachment(
    val id: String,
    val messageId: String,
    val fileName: String,
    val filePath: String,
    val fileType: String,
    val fileSize: Long,
    val mimeType: String,
)

/**
 * 附件Mapper。
 */
object AttachmentMapper {
    fun toDomain(entity: ChatAttachmentEntity): Attachment {
        return Attachment(
            id = entity.id,
            messageId = entity.messageId,
            fileName = entity.fileName,
            filePath = entity.filePath,
            fileType = entity.fileType,
            fileSize = entity.fileSize,
            mimeType = entity.mimeType,
        )
    }

    fun toDomainList(entities: List<ChatAttachmentEntity>): List<Attachment> {
        return entities.map { toDomain(it) }
    }
}

/**
 * API使用记录Domain模型。
 */
data class ApiUsage(
    val id: String,
    val modelName: String,
    val promptTokens: Int,
    val completionTokens: Int,
    val totalTokens: Int,
    val cost: Double,
    val requestTime: Long,
    val responseTime: Long,
    val status: String,
)

/**
 * API使用记录Mapper。
 */
object ApiUsageMapper {
    fun toDomain(entity: ApiUsageEntity): ApiUsage {
        return ApiUsage(
            id = entity.id,
            modelName = entity.modelName,
            promptTokens = entity.promptTokens,
            completionTokens = entity.completionTokens,
            totalTokens = entity.totalTokens,
            cost = entity.cost,
            requestTime = entity.requestTime,
            responseTime = entity.responseTime,
            status = entity.status,
        )
    }

    fun toDomainList(entities: List<ApiUsageEntity>): List<ApiUsage> {
        return entities.map { toDomain(it) }
    }
}

/**
 * 导出任务Domain模型。
 */
data class ExportTask(
    val id: String,
    val conversationId: String,
    val exportFormat: String,
    val filePath: String,
    val status: String,
    val createdAt: Long,
    val completedAt: Long,
)

/**
 * 导出任务Mapper。
 */
object ExportTaskMapper {
    fun toDomain(entity: ExportTaskEntity): ExportTask {
        return ExportTask(
            id = entity.id,
            conversationId = entity.conversationId,
            exportFormat = entity.exportFormat,
            filePath = entity.filePath,
            status = entity.status,
            createdAt = entity.createdAt,
            completedAt = entity.completedAt,
        )
    }

    fun toDomainList(entities: List<ExportTaskEntity>): List<ExportTask> {
        return entities.map { toDomain(it) }
    }
}
