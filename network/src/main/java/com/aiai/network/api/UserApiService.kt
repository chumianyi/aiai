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

import com.aiai.network.model.request.UserSettingRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.UserInfoResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

/**
 * 用户API服务接口。
 *
 * 提供用户信息查询、设置更新、使用统计等功能。
 *
 * 接口列表：
 * - GET /user/info - 获取用户信息
 * - PUT /user/settings - 更新用户设置
 * - GET /user/usage - 获取使用统计
 * - GET /user/quota - 获取配额信息
 */
interface UserApiService {

    /**
     * 获取用户信息。
     *
     * @return 用户信息
     */
    @GET("user/info")
    suspend fun getUserInfo(): ApiResponse<UserInfoResponse>

    /**
     * 更新用户设置。
     *
     * @param request 用户设置请求
     * @return 更新后的用户信息
     */
    @PUT("user/settings")
    suspend fun updateUserSettings(@Body request: UserSettingRequest): ApiResponse<UserInfoResponse>

    /**
     * 获取使用统计信息。
     *
     * @param period 统计周期（day/week/month）
     * @return 使用统计数据
     */
    @GET("user/usage")
    suspend fun getUserUsage(
        @Query("period") period: String = "month",
    ): ApiResponse<Map<String, Any>>

    /**
     * 获取用户配额信息。
     *
     * @return 配额信息
     */
    @GET("user/quota")
    suspend fun getUserQuota(): ApiResponse<Map<String, Any>>

    /**
     * 更新用户头像。
     *
     * @param avatarUrl 头像URL
     * @return 更新后的用户信息
     */
    @PUT("user/avatar")
    suspend fun updateAvatar(@Query("avatar_url") avatarUrl: String): ApiResponse<UserInfoResponse>

    /**
     * 更新用户名。
     *
     * @param username 新用户名
     * @return 更新后的用户信息
     */
    @PUT("user/username")
    suspend fun updateUsername(@Query("username") username: String): ApiResponse<UserInfoResponse>

    /**
     * 注销账号。
     *
     * @param password 密码验证
     * @return 操作结果
     */
    @PUT("user/deactivate")
    suspend fun deactivateAccount(@Query("password") password: String): ApiResponse<Unit>

    /**
     * 获取用户通知设置。
     *
     * @return 通知设置
     */
    @GET("user/notification-settings")
    suspend fun getNotificationSettings(): ApiResponse<Map<String, Boolean>>

    /**
     * 更新用户通知设置。
     *
     * @param settings 通知设置Map
     * @return 操作结果
     */
    @PUT("user/notification-settings")
    suspend fun updateNotificationSettings(@Body settings: Map<String, Boolean>): ApiResponse<Unit>

    /**
     * 获取会员信息。
     *
     * @return 会员信息
     */
    @GET("user/membership")
    suspend fun getMembershipInfo(): ApiResponse<Map<String, Any>>
}
