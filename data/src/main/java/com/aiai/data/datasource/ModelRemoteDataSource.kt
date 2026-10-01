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
package com.aiai.data.datasource

import com.aiai.network.api.ModelApiService
import com.aiai.network.model.response.ModelResponse

/** 模型远程数据源 */
class ModelRemoteDataSource(
    private val modelApi: ModelApiService,
) : RemoteDataSource<ModelResponse>() {
    override suspend fun fetch(): Result<ModelResponse> = Result.success(ModelResponse())
    override suspend fun post(data: ModelResponse): Result<ModelResponse> = Result.success(data)
    override suspend fun update(data: ModelResponse): Result<ModelResponse> = Result.success(data)
    override suspend fun delete(id: String): Result<Unit> {
        return try { modelApi.deleteModelConfig(id); Result.success(Unit) }
        catch (e: Exception) { Result.failure(e) }
    }
}
