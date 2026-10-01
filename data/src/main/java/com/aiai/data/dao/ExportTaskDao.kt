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
package com.aiai.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.aiai.data.entity.ExportTaskEntity
import kotlinx.coroutines.flow.Flow

/**
 * 导出任务DAO接口。
 */
@Dao
interface ExportTaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: ExportTaskEntity)

    @Update
    suspend fun update(task: ExportTaskEntity)

    @Delete
    suspend fun delete(task: ExportTaskEntity)

    @Query("DELETE FROM export_tasks WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM export_tasks ORDER BY createdAt DESC")
    fun getAllTasks(): Flow<List<ExportTaskEntity>>

    @Query("SELECT * FROM export_tasks WHERE status = :status ORDER BY createdAt DESC")
    fun getTasksByStatus(status: String): Flow<List<ExportTaskEntity>>

    @Query("SELECT * FROM export_tasks WHERE conversationId = :conversationId ORDER BY createdAt DESC")
    fun getTasksByConversation(conversationId: String): Flow<List<ExportTaskEntity>>

    @Query("UPDATE export_tasks SET status = :status, completedAt = :completedAt WHERE id = :id")
    suspend fun updateStatus(id: String, status: String, completedAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM export_tasks WHERE id = :id")
    suspend fun getTaskById(id: String): ExportTaskEntity?

    @Query("SELECT COUNT(*) FROM export_tasks WHERE status = 'processing'")
    suspend fun getProcessingCount(): Int
}
