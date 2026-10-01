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

/**
 * 数据库客户端单例。
 *
 * 提供AppDatabase的单例访问。
 */
object DatabaseClient {

    @Volatile
    private var database: AppDatabase? = null

    /**
     * 初始化数据库。
     */
    fun init(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = AppDatabase.getInstance(context)
                }
            }
        }
    }

    /**
     * 获取数据库实例。
     */
    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("Database not initialized. Call init() first.")
    }

    /**
     * 关闭数据库。
     */
    fun close() {
        database?.close()
        database = null
    }
}
