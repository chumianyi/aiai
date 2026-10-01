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
import com.aiai.data.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * 消息DAO接口。
 *
 * 提供聊天消息的CRUD和查询操作。
 */
@Dao
interface MessageDao {

    /**
     * 插入单条消息。
     *
     * @param message 消息实体
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity)

    /**
     * 批量插入消息。
     *
     * @param messages 消息列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(messages: List<MessageEntity>)

    /**
     * 更新消息。
     *
     * @param message 消息实体
     */
    @Update
    suspend fun update(message: MessageEntity)

    /**
     * 删除消息。
     *
     * @param message 消息实体
     */
    @Delete
    suspend fun delete(message: MessageEntity)

    /**
     * 按ID删除消息。
     *
     * @param id 消息ID
     */
    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteById(id: String)

    /**
     * 按会话查询消息列表（按时间升序）。
     *
     * @param conversationId 会话ID
     * @return 消息流
     */
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt ASC")
    fun getMessagesByConversation(conversationId: String): Flow<List<MessageEntity>>

    /**
     * 分页查询会话消息。
     *
     * @param conversationId 会话ID
     * @param limit 每页数量
     * @param offset 偏移量
     * @return 消息列表
     */
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getMessagesPaged(conversationId: String, limit: Int, offset: Int): List<MessageEntity>

    /**
     * 按类型查询消息。
     *
     * @param conversationId 会话ID
     * @param messageType 消息类型
     * @return 消息列表
     */
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND messageType = :messageType ORDER BY createdAt DESC")
    fun getMessagesByType(conversationId: String, messageType: String): Flow<List<MessageEntity>>

    /**
     * 更新消息状态。
     *
     * @param messageId 消息ID
     * @param status 新状态
     */
    @Query("UPDATE messages SET status = :status, updatedAt = :updatedAt WHERE id = :messageId")
    suspend fun updateStatus(messageId: String, status: String, updatedAt: Long = System.currentTimeMillis())

    /**
     * 批量删除会话消息。
     *
     * @param conversationId 会话ID
     */
    @Query("DELETE FROM messages WHERE conversationId = :conversationId")
    suspend fun deleteMessagesByConversation(conversationId: String)

    /**
     * 获取会话消息总数。
     *
     * @param conversationId 会话ID
     * @return 消息数量
     */
    @Query("SELECT COUNT(*) FROM messages WHERE conversationId = :conversationId")
    suspend fun getMessageCount(conversationId: String): Int

    /**
     * 获取最后一条消息。
     *
     * @param conversationId 会话ID
     * @return 最后一条消息
     */
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastMessage(conversationId: String): MessageEntity?

    /**
     * 搜索消息内容。
     *
     * @param conversationId 会话ID
     * @param keyword 搜索关键词
     * @return 匹配的消息列表
     */
    @Query("SELECT * FROM messages WHERE conversationId = :conversationId AND content LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    fun searchMessages(conversationId: String, keyword: String): Flow<List<MessageEntity>>
}
