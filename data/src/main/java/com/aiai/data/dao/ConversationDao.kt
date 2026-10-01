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
import com.aiai.data.entity.ConversationEntity
import kotlinx.coroutines.flow.Flow

/**
 * 会话DAO接口。
 *
 * 提供会话的CRUD、置顶、归档等操作。
 */
@Dao
interface ConversationDao {

    /**
     * 插入会话。
     *
     * @param conversation 会话实体
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(conversation: ConversationEntity)

    /**
     * 批量插入会话。
     *
     * @param conversations 会话列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(conversations: List<ConversationEntity>)

    /**
     * 更新会话。
     *
     * @param conversation 会话实体
     */
    @Update
    suspend fun update(conversation: ConversationEntity)

    /**
     * 删除会话。
     *
     * @param conversation 会话实体
     */
    @Delete
    suspend fun delete(conversation: ConversationEntity)

    /**
     * 按ID删除会话。
     *
     * @param id 会话ID
     */
    @Query("DELETE FROM conversations WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * 查询所有会话（置顶优先，按更新时间降序）。
     *
     * @return 会话流
     */
    @Query("SELECT * FROM conversations WHERE isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    /**
     * 查询归档会话。
     *
     * @return 归档会话流
     */
    @Query("SELECT * FROM conversations WHERE isArchived = 1 ORDER BY updatedAt DESC")
    fun getArchivedConversations(): Flow<List<ConversationEntity>>

    /**
     * 按ID查询会话。
     *
     * @param id 会话ID
     * @return 会话实体
     */
    @Query("SELECT * FROM conversations WHERE id = :id")
    suspend fun getConversationById(id: String): ConversationEntity?

    /**
     * 更新会话标题。
     *
     * @param id 会话ID
     * @param title 新标题
     */
    @Query("UPDATE conversations SET title = :title, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateTitle(id: String, title: String, updatedAt: Long = System.currentTimeMillis())

    /**
     * 设置置顶状态。
     *
     * @param id 会话ID
     * @param isPinned 是否置顶
     */
    @Query("UPDATE conversations SET isPinned = :isPinned, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setPinned(id: String, isPinned: Boolean, updatedAt: Long = System.currentTimeMillis())

    /**
     * 设置归档状态。
     *
     * @param id 会话ID
     * @param isArchived 是否归档
     */
    @Query("UPDATE conversations SET isArchived = :isArchived, updatedAt = :updatedAt WHERE id = :id")
    suspend fun setArchived(id: String, isArchived: Boolean, updatedAt: Long = System.currentTimeMillis())

    /**
     * 更新消息计数。
     *
     * @param id 会话ID
     * @param count 新的消息数
     */
    @Query("UPDATE conversations SET messageCount = :count, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateMessageCount(id: String, count: Int, updatedAt: Long = System.currentTimeMillis())

    /**
     * 搜索会话。
     *
     * @param keyword 搜索关键词
     * @return 匹配的会话列表
     */
    @Query("SELECT * FROM conversations WHERE title LIKE '%' || :keyword || '%' AND isArchived = 0 ORDER BY updatedAt DESC")
    fun searchConversations(keyword: String): Flow<List<ConversationEntity>>

    /**
     * 获取会话总数。
     *
     * @return 会话数量
     */
    @Query("SELECT COUNT(*) FROM conversations WHERE isArchived = 0")
    suspend fun getConversationCount(): Int
}
