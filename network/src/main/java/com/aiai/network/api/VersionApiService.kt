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

import com.aiai.network.model.response.VersionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 版本 API 服务接口。
 *
 * 提供检查更新、下载进度、更新日志等版本相关接口。
 */
interface VersionApiService {

    /**
     * 检查更新。
     *
     * @param currentVersion 当前版本号
     * @param platform 平台（android/ios）
     * @return 版本检查响应
     */
    @GET("version/check")
    suspend fun checkUpdate(
        @Query("current_version") currentVersion: String,
        @Query("platform") platform: String = "android"
    ): Response<VersionResponse.CheckUpdateResponse>

    /**
     * 获取更新日志。
     *
     * @param version 版本号
     * @return 更新日志响应
     */
    @GET("version/changelog")
    suspend fun getChangelog(
        @Query("version") version: String? = null
    ): Response<VersionResponse.ChangelogResponse>

    /**
     * 获取最新版本信息。
     *
     * @return 版本信息响应
     */
    @GET("version/latest")
    suspend fun getLatestVersion(): Response<VersionResponse.VersionInfoResponse>

    /**
     * 获取版本历史。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @return 版本历史响应
     */
    @GET("version/history")
    suspend fun getVersionHistory(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int
    ): Response<VersionResponse.VersionHistoryResponse>

    /**
     * 上报版本使用情况。
     *
     * @param version 版本号
     * @return 上报响应
     */
    @GET("version/report")
    suspend fun reportVersionUsage(
        @Query("version") version: String
    ): Response<VersionResponse.BaseResponse>
}
