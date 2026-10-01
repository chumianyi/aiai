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
import com.aiai.data.entity.ChatAttachmentEntity
import kotlinx.coroutines.flow.Flow

/**
 * 聊天附件DAO接口。
 */
@Dao
interface ChatAttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(attachment: ChatAttachmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attachments: List<ChatAttachmentEntity>)

    @Update
    suspend fun update(attachment: ChatAttachmentEntity)

    @Delete
    suspend fun delete(attachment: ChatAttachmentEntity)

    @Query("DELETE FROM chat_attachments WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM chat_attachments WHERE messageId = :messageId ORDER BY fileName ASC")
    fun getAttachmentsByMessage(messageId: String): Flow<List<ChatAttachmentEntity>>

    @Query("SELECT * FROM chat_attachments WHERE uploadStatus = :status ORDER BY createdAt ASC")
    fun getAttachmentsByStatus(status: String): Flow<List<ChatAttachmentEntity>>

    @Query("UPDATE chat_attachments SET uploadStatus = :status WHERE id = :id")
    suspend fun updateUploadStatus(id: String, status: String)

    @Query("DELETE FROM chat_attachments WHERE messageId = :messageId")
    suspend fun deleteByMessage(messageId: String)

    @Query("SELECT COUNT(*) FROM chat_attachments WHERE messageId = :messageId")
    suspend fun getCountByMessage(messageId: String): Int

    @Query("SELECT * FROM chat_attachments WHERE fileType = :fileType")
    suspend fun getByType(fileType: String): List<ChatAttachmentEntity>
}
