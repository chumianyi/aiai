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
 * 数据库迁移1→2。
 *
 * 新增chat_attachments表。
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS chat_attachments (
                id TEXT PRIMARY KEY NOT NULL,
                messageId TEXT NOT NULL,
                fileName TEXT NOT NULL DEFAULT '',
                filePath TEXT NOT NULL DEFAULT '',
                fileType TEXT NOT NULL DEFAULT 'file',
                fileSize INTEGER NOT NULL DEFAULT 0,
                mimeType TEXT NOT NULL DEFAULT '',
                width INTEGER NOT NULL DEFAULT 0,
                height INTEGER NOT NULL DEFAULT 0,
                duration INTEGER NOT NULL DEFAULT 0,
                uploadStatus TEXT NOT NULL DEFAULT 'pending',
                FOREIGN KEY(messageId) REFERENCES messages(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX index_chat_attachments_messageId ON chat_attachments(messageId)")
    }
}
