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
package com.aiai.data.datasource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 认证本地数据源。
 *
 * 管理用户认证信息的本地存储。
 */
@Singleton
class AuthLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREF_NAME = "aiai_auth_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USERNAME = "username"
        private const val KEY_EMAIL = "email"
        private const val KEY_AVATAR = "avatar"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRES_AT = "expires_at"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    /**
     * 保存登录信息。
     */
    fun saveLoginInfo(
        userId: String,
        username: String,
        email: String,
        avatar: String,
        accessToken: String,
        refreshToken: String,
        expiresAt: Long
    ) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_EMAIL, email)
            putString(KEY_AVATAR, avatar)
            putString(KEY_ACCESS_TOKEN, accessToken)
            putString(KEY_REFRESH_TOKEN, refreshToken)
            putLong(KEY_EXPIRES_AT, expiresAt)
            putBoolean(KEY_IS_LOGGED_IN, true)
        }.apply()
    }

    /**
     * 清除登录信息。
     */
    fun clearLoginInfo() {
        prefs.edit().clear().apply()
    }

    /**
     * 获取用户ID。
     */
    fun getUserId(): String? = prefs.getString(KEY_USER_ID, null)

    /**
     * 获取用户名。
     */
    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    /**
     * 获取邮箱。
     */
    fun getEmail(): String? = prefs.getString(KEY_EMAIL, null)

    /**
     * 获取头像。
     */
    fun getAvatar(): String? = prefs.getString(KEY_AVATAR, null)

    /**
     * 获取访问令牌。
     */
    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS_TOKEN, null)

    /**
     * 获取刷新令牌。
     */
    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH_TOKEN, null)

    /**
     * 获取过期时间。
     */
    fun getExpiresAt(): Long = prefs.getLong(KEY_EXPIRES_AT, 0)

    /**
     * 是否已登录。
     */
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    /**
     * 更新访问令牌。
     */
    fun updateAccessToken(accessToken: String, expiresAt: Long) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putLong(KEY_EXPIRES_AT, expiresAt)
            .apply()
    }
}
