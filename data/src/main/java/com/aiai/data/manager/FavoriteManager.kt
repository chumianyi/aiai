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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.FavoriteDao
import com.aiai.data.entity.FavoriteEntity

/**
 * 收藏管理器。
 *
 * 管理消息收藏、分类、标签等功能。
 */
class FavoriteManager(
    private val favoriteDao: FavoriteDao,
) {

    companion object {
        private const val TAG = "FavoriteManager"
    }

    /**
     * 添加收藏。
     */
    suspend fun addFavorite(
        messageId: String,
        conversationId: String,
        content: String,
        note: String = "",
        category: String = "default",
    ): Long {
        val favorite = FavoriteEntity(
            id = java.util.UUID.randomUUID().toString(),
            messageId = messageId,
            conversationId = conversationId,
            content = content,
            note = note,
            createdAt = System.currentTimeMillis(),
            category = category,
        )
        val result = favoriteDao.insert(favorite)
        Log.d(TAG, "Added favorite: $messageId")
        return result
    }

    /**
     * 删除收藏。
     */
    suspend fun removeFavorite(favoriteId: String) {
        favoriteDao.deleteById(favoriteId)
        Log.d(TAG, "Removed favorite: $favoriteId")
    }

    /**
     * 获取所有收藏。
     */
    fun getAllFavorites(): List<FavoriteEntity> {
        return favoriteDao.getAllFavoritesSync()
    }

    /**
     * 按分类获取收藏。
     */
    fun getFavoritesByCategory(category: String): List<FavoriteEntity> {
        return favoriteDao.getByCategorySync(category)
    }

    /**
     * 获取所有分类。
     */
    fun getAllCategories(): List<String> {
        val favorites = favoriteDao.getAllFavoritesSync()
        return favorites.map { it.category }.distinct()
    }

    /**
     * 搜索收藏内容。
     */
    fun searchFavorites(query: String): List<FavoriteEntity> {
        return favoriteDao.searchFavoritesSync(query)
    }

    /**
     * 更新收藏备注。
     */
    suspend fun updateNote(favoriteId: String, note: String) {
        favoriteDao.updateNote(favoriteId, note)
        Log.d(TAG, "Updated note for: $favoriteId")
    }

    /**
     * 获取收藏总数。
     */
    fun getFavoriteCount(): Int {
        return favoriteDao.getCountSync()
    }
}
