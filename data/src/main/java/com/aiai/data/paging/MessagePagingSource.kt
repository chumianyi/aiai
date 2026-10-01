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
import com.aiai.data.dao.MessageDao
import com.aiai.data.entity.MessageEntity

/**
 * 消息分页数据源。
 */
class MessagePagingSource(
    private val messageDao: MessageDao,
    private val conversationId: String,
) : PagingSource<Int, MessageEntity>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageEntity> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize
            val messages = messageDao.getMessagesPaged(conversationId, pageSize, (page - 1) * pageSize)
            LoadResult.Page(
                data = messages,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (messages.isEmpty()) null else page + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MessageEntity>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
