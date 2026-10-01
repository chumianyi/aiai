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
package com.aiai.data.repository

import com.aiai.data.model.AuthState
import kotlinx.coroutines.flow.Flow

/**
 * 认证仓库接口。
 *
 * 定义用户认证相关的数据操作。
 */
interface AuthRepository {

    /**
     * 获取认证状态流。
     */
    val authState: Flow<AuthState>

    /**
     * 用户登录。
     *
     * @param account 账号
     * @param password 密码
     * @return 结果
     */
    suspend fun login(account: String, password: String): Result<AuthState.Authenticated>

    /**
     * 用户注册。
     *
     * @param email 邮箱
     * @param password 密码
     * @param verificationCode 验证码
     * @return 结果
     */
    suspend fun register(
        email: String,
        password: String,
        verificationCode: String
    ): Result<AuthState.Authenticated>

    /**
     * 用户登出。
     */
    suspend fun logout()

    /**
     * 刷新 Token。
     */
    suspend fun refreshToken(): Result<Boolean>

    /**
     * 发送验证码。
     *
     * @param target 目标（邮箱或手机号）
     * @param type 类型
     */
    suspend fun sendVerificationCode(target: String, type: String): Result<Boolean>

    /**
     * 修改密码。
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    suspend fun changePassword(oldPassword: String, newPassword: String): Result<Boolean>

    /**
     * 忘记密码。
     *
     * @param email 邮箱
     */
    suspend fun forgotPassword(email: String): Result<Boolean>

    /**
     * 获取当前用户ID。
     */
    fun getCurrentUserId(): String?

    /**
     * 检查是否已登录。
     */
    fun isLoggedIn(): Boolean
}
