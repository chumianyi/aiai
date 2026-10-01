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
package com.aiai.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aiai.data.dao.ApiUsageDao
import com.aiai.data.dao.ChatAttachmentDao
import com.aiai.data.dao.ConversationDao
import com.aiai.data.dao.ExportTaskDao
import com.aiai.data.dao.FavoriteDao
import com.aiai.data.dao.MessageDao
import com.aiai.data.dao.ModelConfigDao
import com.aiai.data.dao.NotificationDao
import com.aiai.data.dao.PluginDao
import com.aiai.data.dao.PromptTemplateDao
import com.aiai.data.dao.SearchHistoryDao
import com.aiai.data.dao.UserSettingDao
import com.aiai.data.entity.ApiUsageEntity
import com.aiai.data.entity.ChatAttachmentEntity
import com.aiai.data.entity.ConversationEntity
import com.aiai.data.entity.ExportTaskEntity
import com.aiai.data.entity.FavoriteEntity
import com.aiai.data.entity.MessageEntity
import com.aiai.data.entity.ModelConfigEntity
import com.aiai.data.entity.NotificationEntity
import com.aiai.data.entity.PluginEntity
import com.aiai.data.entity.PromptTemplateEntity
import com.aiai.data.entity.SearchHistoryEntity
import com.aiai.data.entity.UserSettingEntity
import com.aiai.data.migration.MIGRATION_1_2
import com.aiai.data.migration.MIGRATION_2_3
import com.aiai.data.migration.MIGRATION_3_4
import com.aiai.data.migration.MIGRATION_4_5

/**
 * Room数据库主类。
 *
 * 包含所有Entity和DAO，管理数据库版本和迁移。
 */
@Database(
    entities = [
        MessageEntity::class,
        ConversationEntity::class,
        ModelConfigEntity::class,
        UserSettingEntity::class,
        FavoriteEntity::class,
        SearchHistoryEntity::class,
        PromptTemplateEntity::class,
        PluginEntity::class,
        ChatAttachmentEntity::class,
        ApiUsageEntity::class,
        ExportTaskEntity::class,
        NotificationEntity::class,
    ],
    version = 5,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao
    abstract fun conversationDao(): ConversationDao
    abstract fun modelConfigDao(): ModelConfigDao
    abstract fun userSettingDao(): UserSettingDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun promptTemplateDao(): PromptTemplateDao
    abstract fun pluginDao(): PluginDao
    abstract fun chatAttachmentDao(): ChatAttachmentDao
    abstract fun apiUsageDao(): ApiUsageDao
    abstract fun exportTaskDao(): ExportTaskDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        private const val DATABASE_NAME = "aiai_database.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME,
            )
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                .addCallback(DatabaseCallback())
                .build()
        }
    }
}
