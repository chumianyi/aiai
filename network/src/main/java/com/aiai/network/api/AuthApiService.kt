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

import com.aiai.network.model.request.AuthRequest
import com.aiai.network.model.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 认证 API 服务接口。
 *
 * 提供登录、注册、登出、刷新Token、验证码等认证相关接口。
 */
interface AuthApiService {

    /**
     * 用户登录。
     *
     * @param request 登录请求体
     * @return 登录响应
     */
    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest.LoginRequest): Response<AuthResponse.LoginResponse>

    /**
     * 用户注册。
     *
     * @param request 注册请求体
     * @return 注册响应
     */
    @POST("auth/register")
    suspend fun register(@Body request: AuthRequest.RegisterRequest): Response<AuthResponse.LoginResponse>

    /**
     * 用户登出。
     *
     * @return 登出响应
     */
    @POST("auth/logout")
    suspend fun logout(): Response<AuthResponse.LogoutResponse>

    /**
     * 刷新 Token。
     *
     * @param refreshToken 刷新令牌
     * @return 新的 Token 响应
     */
    @POST("auth/refresh")
    suspend fun refreshToken(
        @Query("refresh_token") refreshToken: String
    ): Response<AuthResponse.RefreshTokenResponse>

    /**
     * 发送验证码。
     *
     * @param request 验证码请求体
     * @return 发送响应
     */
    @POST("auth/send-code")
    suspend fun sendVerificationCode(
        @Body request: AuthRequest.VerificationCodeRequest
    ): Response<AuthResponse.VerificationCodeResponse>

    /**
     * 验证验证码。
     *
     * @param request 验证请求体
     * @return 验证响应
     */
    @POST("auth/verify-code")
    suspend fun verifyCode(
        @Body request: AuthRequest.VerifyCodeRequest
    ): Response<AuthResponse.VerifyCodeResponse>

    /**
     * 忘记密码。
     *
     * @param request 忘记密码请求体
     * @return 响应
     */
    @POST("auth/forgot-password")
    suspend fun forgotPassword(
        @Body request: AuthRequest.ForgotPasswordRequest
    ): Response<AuthResponse.BaseResponse>

    /**
     * 重置密码。
     *
     * @param request 重置密码请求体
     * @return 响应
     */
    @POST("auth/reset-password")
    suspend fun resetPassword(
        @Body request: AuthRequest.ResetPasswordRequest
    ): Response<AuthResponse.BaseResponse>

    /**
     * 修改密码。
     *
     * @param request 修改密码请求体
     * @return 响应
     */
    @POST("auth/change-password")
    suspend fun changePassword(
        @Body request: AuthRequest.ChangePasswordRequest
    ): Response<AuthResponse.BaseResponse>

    /**
     * 获取用户信息。
     *
     * @return 用户信息响应
     */
    @GET("auth/userinfo")
    suspend fun getUserInfo(): Response<AuthResponse.UserInfoResponse>

    /**
     * 第三方登录。
     *
     * @param request 第三方登录请求体
     * @return 登录响应
     */
    @POST("auth/social-login")
    suspend fun socialLogin(
        @Body request: AuthRequest.SocialLoginRequest
    ): Response<AuthResponse.LoginResponse>

    /**
     * 检查 Token 有效性。
     *
     * @return 检查结果
     */
    @GET("auth/check-token")
    suspend fun checkTokenValidity(): Response<AuthResponse.TokenValidityResponse>
}
