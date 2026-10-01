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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 登录请求模型。
 *
 * @property username 用户名
 * @property password 密码
 * @property email 邮箱
 * @property captcha 验证码
 * @property deviceId 设备ID
 * @property deviceName 设备名称
 * @property platform 平台
 */
data class LoginRequest(
    @SerializedName("username")
    val username: String = "",

    @SerializedName("password")
    val password: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("captcha")
    val captcha: String = "",

    @SerializedName("device_id")
    val deviceId: String = "",

    @SerializedName("device_name")
    val deviceName: String = "",

    @SerializedName("platform")
    val platform: String = "android",
)

/**
 * 注册请求模型。
 *
 * @property username 用户名
 * @property email 邮箱
 * @property password 密码
 * @property confirmPassword 确认密码
 * @property inviteCode 邀请码
 */
data class RegisterRequest(
    @SerializedName("username")
    val username: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("password")
    val password: String = "",

    @SerializedName("confirm_password")
    val confirmPassword: String = "",

    @SerializedName("invite_code")
    val inviteCode: String = "",
)

/**
 * 密码重置请求模型。
 *
 * @property email 邮箱
 * @property newPassword 新密码
 * @property verifyCode 验证码
 */
data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String = "",

    @SerializedName("new_password")
    val newPassword: String = "",

    @SerializedName("verify_code")
    val verifyCode: String = "",
)

/**
 * Token刷新请求模型。
 *
 * @property refreshToken 刷新令牌
 */
data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String = "",
)

/**
 * 用户资料更新请求模型。
 *
 * @property username 用户名
 * @property avatarUrl 头像URL
 * @property bio 个人简介
 */
data class UpdateProfileRequest(
    @SerializedName("username")
    val username: String = "",

    @SerializedName("avatar_url")
    val avatarUrl: String = "",

    @SerializedName("bio")
    val bio: String = "",
)

/**
 * 反馈请求模型。
 *
 * @property type 反馈类型
 * @property content 反馈内容
 * @property contact 联系方式
 * @property screenshots 截图URL列表
 * @property appVersion 应用版本
 */
data class FeedbackRequest(
    @SerializedName("type")
    val type: String = "bug",

    @SerializedName("content")
    val content: String = "",

    @SerializedName("contact")
    val contact: String = "",

    @SerializedName("screenshots")
    val screenshots: List<String> = emptyList(),

    @SerializedName("app_version")
    val appVersion: String = "",
)
