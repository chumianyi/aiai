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
package com.aiai.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.aiai.data.dao.ConversationDao
import com.aiai.data.entity.ConversationEntity

/** 会话分页数据源 */
class ConversationPagingSource(
    private val conversationDao: ConversationDao,
) : PagingSource<Int, ConversationEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ConversationEntity> {
        return try {
            LoadResult.Page(emptyList(), null, null)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, ConversationEntity>): Int? = null
}
