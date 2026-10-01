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
 * 仅缓存策略。
 *
 * 只从本地缓存获取数据，不请求网络。
 *
 * @param T 数据类型
 */
class OnlyCacheStrategy<T> : CacheStrategy<T> {

    override suspend fun fetch(
        localFetch: suspend () -> T?,
        remoteFetch: suspend () -> Result<T>,
    ): Result<T> {
        val localData = localFetch()
        return if (localData != null) {
            Result.success(localData)
        } else {
            Result.failure(Exception("No cache available"))
        }
    }
}
