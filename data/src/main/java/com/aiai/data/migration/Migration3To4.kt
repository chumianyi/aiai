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
package com.aiai.data.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 数据库迁移3→4。
 *
 * 新增export_tasks表。
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS export_tasks (
                id TEXT PRIMARY KEY NOT NULL,
                conversationId TEXT NOT NULL DEFAULT '',
                exportFormat TEXT NOT NULL DEFAULT 'markdown',
                filePath TEXT NOT NULL DEFAULT '',
                status TEXT NOT NULL DEFAULT 'pending',
                createdAt INTEGER NOT NULL,
                completedAt INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX index_export_tasks_status ON export_tasks(status)")
    }
}
