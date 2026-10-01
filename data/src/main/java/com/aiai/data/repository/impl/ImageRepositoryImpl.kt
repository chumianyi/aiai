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

import com.aiai.data.repository.ImageRepository
import com.aiai.network.api.ImageApiService
import com.aiai.network.model.request.ImageGenerateRequest
import com.aiai.network.model.response.ImageResponse

/** 图片仓库实现 */
class ImageRepositoryImpl(
    private val imageApi: ImageApiService,
) : ImageRepository {

    override suspend fun generateImage(request: ImageGenerateRequest): Result<ImageResponse> {
        return try {
            val response = imageApi.generateImage(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun editImage(request: ImageGenerateRequest): Result<ImageResponse> {
        return try {
            val response = imageApi.editImage(request)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun getImageHistory(page: Int, pageSize: Int): Result<List<ImageResponse>> {
        return try {
            val response = imageApi.getImageHistory(page, pageSize)
            if (response.isSuccessful()) Result.success(response.data ?: emptyList())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun deleteImage(imageId: String): Result<Unit> {
        return try {
            imageApi.deleteImage(imageId)
            Result.success(Unit)
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun upscaleImage(imageId: String, scale: Int): Result<ImageResponse> {
        return try {
            val response = imageApi.upscaleImage(imageId, scale)
            if (response.isSuccessful()) Result.success(response.getDataOrThrow())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
    override suspend fun getImageTemplates(category: String): Result<List<Map<String, Any>>> {
        return try {
            val response = imageApi.getImageTemplates(category)
            if (response.isSuccessful()) Result.success(response.data ?: emptyList())
            else Result.failure(Exception(response.message))
        } catch (e: Exception) { Result.failure(e) }
    }
}
