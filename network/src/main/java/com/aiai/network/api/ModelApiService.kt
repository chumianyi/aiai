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
package com.aiai.network.api

import com.aiai.network.model.request.ModelConfigRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.ModelResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 模型管理API服务接口。
 *
 * 提供AI模型列表查询、模型详情获取、模型参数配置等功能。
 *
 * 接口列表：
 * - GET /model/list - 获取可用模型列表
 * - GET /model/{id} - 获取模型详情
 * - PUT /model/{id}/config - 更新模型参数配置
 * - GET /model/{id}/pricing - 获取模型定价
 */
interface ModelApiService {

    /**
     * 获取可用模型列表。
     *
     * @param category 模型分类（text/image/audio/all）
     * @param onlyAvailable 是否仅返回可用模型
     * @return 模型列表
     */
    @GET("model/list")
    suspend fun getModelList(
        @Query("category") category: String = "all",
        @Query("only_available") onlyAvailable: Boolean = true,
    ): ApiResponse<List<ModelResponse>>

    /**
     * 获取模型详情。
     *
     * @param modelId 模型ID
     * @return 模型详细信息
     */
    @GET("model/{modelId}")
    suspend fun getModelDetail(@Path("modelId") modelId: String): ApiResponse<ModelResponse>

    /**
     * 更新模型参数配置。
     *
     * @param modelId 模型ID
     * @param request 模型配置请求
     * @return 更新后的模型信息
     */
    @PUT("model/{modelId}/config")
    suspend fun updateModelConfig(
        @Path("modelId") modelId: String,
        @Body request: ModelConfigRequest,
    ): ApiResponse<ModelResponse>

    /**
     * 获取模型定价信息。
     *
     * @param modelId 模型ID
     * @return 模型定价
     */
    @GET("model/{modelId}/pricing")
    suspend fun getModelPricing(@Path("modelId") modelId: String): ApiResponse<Map<String, Any>>

    /**
     * 设置默认模型。
     *
     * @param modelId 模型ID
     * @return 操作结果
     */
    @POST("model/{modelId}/set-default")
    suspend fun setDefaultModel(@Path("modelId") modelId: String): ApiResponse<Unit>

    /**
     * 获取默认模型。
     *
     * @return 默认模型信息
     */
    @GET("model/default")
    suspend fun getDefaultModel(): ApiResponse<ModelResponse>

    /**
     * 获取推荐模型列表。
     *
     * @param scenario 使用场景（chat/code/writing/image）
     * @return 推荐模型列表
     */
    @GET("model/recommended")
    suspend fun getRecommendedModels(
        @Query("scenario") scenario: String = "chat",
    ): ApiResponse<List<ModelResponse>>

    /**
     * 删除自定义模型配置。
     *
     * @param modelId 模型ID
     * @return 操作结果
     */
    @DELETE("model/{modelId}")
    suspend fun deleteModelConfig(@Path("modelId") modelId: String): ApiResponse<Unit>
}
