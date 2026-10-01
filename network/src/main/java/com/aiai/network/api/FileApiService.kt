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

import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * 文件API服务接口。
 *
 * 提供图片上传、文件上传、文件下载、OCR识别等功能。
 *
 * 接口列表：
 * - POST /file/upload/image - 上传图片
 * - POST /file/upload - 上传文件
 * - GET /file/download/{id} - 下载文件
 * - POST /file/ocr - OCR文字识别
 */
interface FileApiService {

    /**
     * 上传图片文件。
     *
     * @param file 图片文件（MultipartBody.Part）
     * @param compress 是否压缩
     * @return 上传结果
     */
    @Multipart
    @POST("file/upload/image")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Query("compress") compress: Boolean = true,
    ): ApiResponse<FileUploadResponse>

    /**
     * 上传通用文件。
     *
     * @param file 文件（MultipartBody.Part）
     * @param fileType 文件类型
     * @return 上传结果
     */
    @Multipart
    @POST("file/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Query("file_type") fileType: String = "document",
    ): ApiResponse<FileUploadResponse>

    /**
     * 下载文件。
     *
     * @param fileId 文件ID
     * @return 文件流
     */
    @Streaming
    @GET("file/download/{fileId}")
    suspend fun downloadFile(@Path("fileId") fileId: String): ResponseBody

    /**
     * 通过URL下载文件。
     *
     * @param url 文件URL
     * @return 文件流
     */
    @Streaming
    @GET
    suspend fun downloadFileByUrl(@Url url: String): ResponseBody

    /**
     * OCR文字识别。
     *
     * @param fileId 已上传的文件ID
     * @param language 识别语言
     * @return OCR识别结果
     */
    @POST("file/ocr")
    suspend fun ocrRecognition(
        @Query("file_id") fileId: String,
        @Query("language") language: String = "zh-CN",
    ): ApiResponse<Map<String, Any>>

    /**
     * 删除文件。
     *
     * @param fileId 文件ID
     * @return 操作结果
     */
    @DELETE("file/{fileId}")
    suspend fun deleteFile(@Path("fileId") fileId: String): ApiResponse<Unit>

    /**
     * 获取文件信息。
     *
     * @param fileId 文件ID
     * @return 文件信息
     */
    @GET("file/{fileId}")
    suspend fun getFileInfo(@Path("fileId") fileId: String): ApiResponse<FileUploadResponse>

    /**
     * 批量上传文件。
     *
     * @param files 文件列表
     * @return 上传结果列表
     */
    @Multipart
    @POST("file/upload/batch")
    suspend fun batchUploadFiles(
        @Part files: List<MultipartBody.Part>,
    ): ApiResponse<List<FileUploadResponse>>

    /**
     * 获取上传进度。
     *
     * @param uploadId 上传任务ID
     * @return 进度信息
     */
    @GET("file/upload/progress/{uploadId}")
    suspend fun getUploadProgress(@Path("uploadId") uploadId: String): ApiResponse<Map<String, Any>>

    /**
     * 取消上传。
     *
     * @param uploadId 上传任务ID
     * @return 操作结果
     */
    @DELETE("file/upload/{uploadId}")
    suspend fun cancelUpload(@Path("uploadId") uploadId: String): ApiResponse<Unit>
}
