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
import com.aiai.data.entity.FavoriteEntity
import kotlinx.coroutines.flow.Flow

/**
 * 收藏DAO接口。
 */
@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<FavoriteEntity>)

    @Update
    suspend fun update(favorite: FavoriteEntity)

    @Delete
    suspend fun delete(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM favorites ORDER BY createdAt DESC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE category = :category ORDER BY createdAt DESC")
    fun getFavoritesByCategory(category: String): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE messageId = :messageId LIMIT 1")
    suspend fun getByMessageId(messageId: String): FavoriteEntity?

    @Query("SELECT * FROM favorites WHERE content LIKE '%' || :keyword || '%' ORDER BY createdAt DESC")
    fun searchFavorites(keyword: String): Flow<List<FavoriteEntity>>

    @Query("DELETE FROM favorites WHERE conversationId = :conversationId")
    suspend fun deleteByConversation(conversationId: String)

    @Query("SELECT COUNT(*) FROM favorites")
    suspend fun getCount(): Int

    @Query("UPDATE favorites SET note = :note WHERE id = :id")
    suspend fun updateNote(id: String, note: String)
}
