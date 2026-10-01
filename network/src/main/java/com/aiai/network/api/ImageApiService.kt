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

import com.aiai.network.model.request.ImageGenerateRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.ImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 图片API服务接口。
 *
 * 提供文生图、图生图、图片编辑等功能。
 *
 * 接口列表：
 * - POST /image/generate - 文生图
 * - POST /image/edit - 图片编辑
 * - POST /image/variation - 图生图/图片变体
 * - GET /image/history - 图片历史
 */
interface ImageApiService {

    /**
     * 文生图：根据文本描述生成图片。
     *
     * @param request 图片生成请求
     * @return 生成的图片信息
     */
    @POST("image/generate")
    suspend fun generateImage(@Body request: ImageGenerateRequest): ApiResponse<ImageResponse>

    /**
     * 图片编辑：基于参考图和指令编辑图片。
     *
     * @param request 图片生成请求（包含参考图）
     * @return 编辑后的图片
     */
    @POST("image/edit")
    suspend fun editImage(@Body request: ImageGenerateRequest): ApiResponse<ImageResponse>

    /**
     * 图生图：基于参考图生成变体。
     *
     * @param image 参考图片
     * @param request 生成请求
     * @return 生成的图片
     */
    @Multipart
    @POST("image/variation")
    suspend fun createImageVariation(
        @Part image: MultipartBody.Part,
        @Body request: ImageGenerateRequest,
    ): ApiResponse<ImageResponse>

    /**
     * 获取图片生成历史。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @return 历史图片列表
     */
    @GET("image/history")
    suspend fun getImageHistory(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
    ): ApiResponse<List<ImageResponse>>

    /**
     * 删除生成的图片。
     *
     * @param imageId 图片ID
     * @return 操作结果
     */
    @POST("image/{imageId}/delete")
    suspend fun deleteImage(@Path("imageId") imageId: String): ApiResponse<Unit>

    /**
     * 图片风格转换。
     *
     * @param image 源图片
     * @param style 目标风格
     * @return 转换后的图片
     */
    @Multipart
    @POST("image/style-transfer")
    suspend fun styleTransfer(
        @Part image: MultipartBody.Part,
        @Query("style") style: String,
    ): ApiResponse<ImageResponse>

    /**
     * 图片放大。
     *
     * @param imageId 图片ID
     * @param scale 放大倍数
     * @return 放大后的图片
     */
    @POST("image/{imageId}/upscale")
    suspend fun upscaleImage(
        @Path("imageId") imageId: String,
        @Query("scale") scale: Int = 2,
    ): ApiResponse<ImageResponse>

    /**
     * 获取图片模板。
     *
     * @param category 模板分类
     * @return 模板列表
     */
    @GET("image/templates")
    suspend fun getImageTemplates(
        @Query("category") category: String = "all",
    ): ApiResponse<List<Map<String, Any>>>
}
