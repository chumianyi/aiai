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
package com.aiai.data.repository.impl

import com.aiai.data.dao.FavoriteDao
import com.aiai.data.entity.FavoriteEntity
import com.aiai.data.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

/** 收藏仓库实现 */
class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao,
) : FavoriteRepository {

    override fun getFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()
    override fun getFavoritesByCategory(category: String): Flow<List<FavoriteEntity>> = favoriteDao.getFavoritesByCategory(category)
    override suspend fun addFavorite(favorite: FavoriteEntity) = favoriteDao.insert(favorite)
    override suspend fun removeFavorite(id: String) = favoriteDao.deleteById(id)
    override suspend fun updateNote(id: String, note: String) = favoriteDao.updateNote(id, note)
    override fun searchFavorites(keyword: String): Flow<List<FavoriteEntity>> = favoriteDao.searchFavorites(keyword)
    override suspend fun getFavoriteCount(): Int = favoriteDao.getCount()
    override suspend fun getByMessageId(messageId: String): FavoriteEntity? = favoriteDao.getByMessageId(messageId)
}
