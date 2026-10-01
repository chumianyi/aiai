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
package com.aiai.data.model

/**
 * 认证状态域模型。
 *
 * 表示用户认证的各种状态。
 */
sealed class AuthState {

    /** 未登录状态。 */
    object Unauthenticated : AuthState()

    /** 正在加载状态。 */
    object Loading : AuthState()

    /**
     * 已认证状态。
     *
     * @property userId 用户ID
     * @property username 用户名
     * @property email 邮箱
     * @property avatar 头像URL
     * @property accessToken 访问令牌
     * @property expiresAt 过期时间
     */
    data class Authenticated(
        val userId: String,
        val username: String,
        val email: String,
        val avatar: String,
        val accessToken: String,
        val expiresAt: Long
    ) : AuthState()

    /**
     * 认证错误状态。
     *
     * @param message 错误信息
     * @param code 错误码
     */
    data class Error(
        val message: String,
        val code: Int = -1
    ) : AuthState()
}
