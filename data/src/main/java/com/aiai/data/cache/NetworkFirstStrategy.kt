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
package com.aiai.data.cache

/**
 * 网络优先策略。
 *
 * 先请求网络，网络失败时返回本地缓存。
 *
 * @param T 数据类型
 */
class NetworkFirstStrategy<T> : CacheStrategy<T> {

    override suspend fun fetch(
        localFetch: suspend () -> T?,
        remoteFetch: suspend () -> Result<T>,
    ): Result<T> {
        return try {
            remoteFetch()
        } catch (e: Exception) {
            val localData = localFetch()
            if (localData != null) Result.success(localData)
            else Result.failure(e)
        }
    }
}
