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
package com.aiai.data.repository.impl

import android.util.Log
import com.aiai.data.model.AuthState
import com.aiai.data.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 认证仓库实现类。
 *
 * 提供用户登录、注册、登出、Token管理等功能。
 */
class AuthRepositoryImpl : AuthRepository {

    companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthorized)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    private var token: String? = null
    private var refreshToken: String? = null
    private var tokenExpireTime: Long = 0L
    private var userId: String? = null
    private var username: String? = null
    private var email: String? = null
    private var avatar: String? = null

    override suspend fun login(username: String, password: String): Result<AuthState> {
        Log.d(TAG, "Login attempt: $username")
        return try {
            // 模拟登录请求
            if (username.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("用户名或密码不能为空")
                Result.failure(Exception("用户名或密码不能为空"))
            } else {
                // 模拟成功登录
                this.username = username
                this.token = "mock_token_${System.currentTimeMillis()}"
                this.refreshToken = "mock_refresh_${System.currentTimeMillis()}"
                this.tokenExpireTime = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
                this.userId = "user_${System.currentTimeMillis()}"

                val state = AuthState.Authorized(
                    token = token!!,
                    userId = userId!!,
                    username = username
                )
                _authState.value = state
                Log.d(TAG, "Login success: $username")
                Result.success(state)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Login failed", e)
            _authState.value = AuthState.Error(e.message ?: "登录失败")
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, email: String, password: String): Result<AuthState> {
        Log.d(TAG, "Register attempt: $username, $email")
        return try {
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                _authState.value = AuthState.Error("所有字段不能为空")
                Result.failure(Exception("所有字段不能为空"))
            } else if (password.length < 6) {
                _authState.value = AuthState.Error("密码长度至少6位")
                Result.failure(Exception("密码长度至少6位"))
            } else {
                // 模拟注册成功
                this.username = username
                this.email = email
                this.token = "mock_token_${System.currentTimeMillis()}"
                this.refreshToken = "mock_refresh_${System.currentTimeMillis()}"
                this.tokenExpireTime = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
                this.userId = "user_${System.currentTimeMillis()}"

                val state = AuthState.Authorized(
                    token = token!!,
                    userId = userId!!,
                    username = username
                )
                _authState.value = state
                Log.d(TAG, "Register success: $username")
                Result.success(state)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Register failed", e)
            _authState.value = AuthState.Error(e.message ?: "注册失败")
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        Log.d(TAG, "Logout")
        return try {
            token = null
            refreshToken = null
            tokenExpireTime = 0L
            userId = null
            username = null
            email = null
            avatar = null
            _authState.value = AuthState.Unauthorized
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Logout failed", e)
            Result.failure(e)
        }
    }

    override suspend fun refreshToken(): Result<String> {
        Log.d(TAG, "Refresh token")
        return try {
            if (refreshToken.isNullOrBlank()) {
                Result.failure(Exception("无刷新令牌"))
            } else {
                token = "mock_token_${System.currentTimeMillis()}"
                tokenExpireTime = System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000
                Log.d(TAG, "Token refreshed")
                Result.success(token!!)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Refresh token failed", e)
            Result.failure(e)
        }
    }

    override suspend fun sendVerificationCode(email: String): Result<Unit> {
        Log.d(TAG, "Send verification code: $email")
        return try {
            if (email.isBlank()) {
                Result.failure(Exception("邮箱不能为空"))
            } else {
                // 模拟发送验证码
                Log.d(TAG, "Verification code sent to: $email")
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Send verification code failed", e)
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String, code: String, newPassword: String): Result<Unit> {
        Log.d(TAG, "Reset password: $email")
        return try {
            if (email.isBlank() || code.isBlank() || newPassword.isBlank()) {
                Result.failure(Exception("所有字段不能为空"))
            } else {
                Log.d(TAG, "Password reset success")
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Reset password failed", e)
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Boolean {
        return token != null && System.currentTimeMillis() < tokenExpireTime
    }

    override fun getToken(): String? = token

    override fun getUserId(): String? = userId

    override fun getUsername(): String? = username

    override fun getEmail(): String? = email

    override fun getAvatar(): String? = avatar

    override suspend fun updateProfile(username: String?, avatar: String?): Result<Unit> {
        Log.d(TAG, "Update profile")
        return try {
            username?.let { this.username = it }
            avatar?.let { this.avatar = it }
            Log.d(TAG, "Profile updated")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Update profile failed", e)
            Result.failure(e)
        }
    }
}
