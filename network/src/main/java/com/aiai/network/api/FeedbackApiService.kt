/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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

import com.aiai.network.model.request.FeedbackRequest
import com.aiai.network.model.response.FeedbackResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 反馈 API 服务接口。
 *
 * 提供提交反馈、反馈列表、回复等反馈相关接口。
 */
interface FeedbackApiService {

    /**
     * 提交反馈。
     *
     * @param request 提交反馈请求体
     * @return 反馈响应
     */
    @POST("feedback/submit")
    suspend fun submitFeedback(@Body request: FeedbackRequest.SubmitFeedbackRequest): Response<FeedbackResponse.FeedbackResponse>

    /**
     * 获取反馈列表。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @param type 反馈类型
     * @return 反馈列表响应
     */
    @GET("feedback/list")
    suspend fun getFeedbackList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("type") type: String? = null
    ): Response<FeedbackResponse.FeedbackListResponse>

    /**
     * 获取反馈详情。
     *
     * @param feedbackId 反馈ID
     * @return 反馈详情响应
     */
    @GET("feedback/{feedbackId}")
    suspend fun getFeedbackDetail(@Path("feedbackId") feedbackId: String): Response<FeedbackResponse.FeedbackDetailResponse>

    /**
     * 回复反馈。
     *
     * @param feedbackId 反馈ID
     * @param request 回复请求体
     * @return 回复响应
     */
    @POST("feedback/{feedbackId}/reply")
    suspend fun replyFeedback(
        @Path("feedbackId") feedbackId: String,
        @Body request: FeedbackRequest.ReplyRequest
    ): Response<FeedbackResponse.ReplyResponse>

    /**
     * 删除反馈。
     *
     * @param feedbackId 反馈ID
     * @return 删除响应
     */
    @POST("feedback/{feedbackId}/delete")
    suspend fun deleteFeedback(@Path("feedbackId") feedbackId: String): Response<FeedbackResponse.BaseResponse>

    /**
     * 上传反馈图片。
     *
     * @param request 上传请求体
     * @return 上传响应
     */
    @POST("feedback/upload-image")
    suspend fun uploadFeedbackImage(
        @Body request: FeedbackRequest.UploadImageRequest
    ): Response<FeedbackResponse.UploadImageResponse>

    /**
     * 获取反馈类型列表。
     *
     * @return 反馈类型列表响应
     */
    @GET("feedback/types")
    suspend fun getFeedbackTypes(): Response<FeedbackResponse.FeedbackTypeListResponse>
}
