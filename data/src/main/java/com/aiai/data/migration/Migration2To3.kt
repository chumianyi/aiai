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
 * 数据库迁移2→3。
 *
 * 新增api_usage表和notifications表。
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS api_usage (
                id TEXT PRIMARY KEY NOT NULL,
                modelName TEXT NOT NULL DEFAULT '',
                promptTokens INTEGER NOT NULL DEFAULT 0,
                completionTokens INTEGER NOT NULL DEFAULT 0,
                totalTokens INTEGER NOT NULL DEFAULT 0,
                cost REAL NOT NULL DEFAULT 0.0,
                requestTime INTEGER NOT NULL,
                responseTime INTEGER NOT NULL DEFAULT 0,
                status TEXT NOT NULL DEFAULT 'success'
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX index_api_usage_modelName ON api_usage(modelName)")
        db.execSQL("CREATE INDEX index_api_usage_requestTime ON api_usage(requestTime)")

        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS notifications (
                id TEXT PRIMARY KEY NOT NULL,
                title TEXT NOT NULL DEFAULT '',
                content TEXT NOT NULL DEFAULT '',
                type TEXT NOT NULL DEFAULT 'system',
                isRead INTEGER NOT NULL DEFAULT 0,
                createdAt INTEGER NOT NULL,
                actionData TEXT NOT NULL DEFAULT '{}'
            )
            """.trimIndent()
        )
    }
}
