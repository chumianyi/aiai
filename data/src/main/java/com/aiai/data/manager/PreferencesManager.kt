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
package com.aiai.data.manager

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * DataStore偏好设置管理器。
 *
 * 使用Jetpack DataStore管理用户偏好设置。
 *
 * @property context 上下文
 */
class PreferencesManager(
    private val context: Context,
) {

    companion object {
        private const val TAG = "PreferencesManager"
        private const val DATASTORE_NAME = "aiai_preferences"

        // 键定义
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_LANGUAGE = stringPreferencesKey("language")
        val KEY_FONT_SCALE = floatPreferencesKey("font_scale")
        val KEY_BUBBLE_STYLE = stringPreferencesKey("bubble_style")
        val KEY_AUTO_SCROLL = booleanPreferencesKey("auto_scroll")
        val KEY_SEND_ON_ENTER = booleanPreferencesKey("send_on_enter")
        val KEY_MARKDOWN_ENABLED = booleanPreferencesKey("markdown_enabled")
        val KEY_CODE_HIGHLIGHT = booleanPreferencesKey("code_highlight")
        val KEY_VOICE_INPUT = booleanPreferencesKey("voice_input_enabled")
        val KEY_IMAGE_GEN = booleanPreferencesKey("image_generation_enabled")
        val KEY_NOTIFICATION = booleanPreferencesKey("notification_enabled")
        val KEY_CACHE_SIZE_LIMIT = intPreferencesKey("cache_size_limit")
        val KEY_USER_TOKEN = stringPreferencesKey("user_token")
        val KEY_REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val KEY_LAST_SYNC_TIME = longPreferencesKey("last_sync_time")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    /**
     * 获取主题模式。
     */
    val themeMode: Flow<String> = context.dataStore.data.map { it[KEY_THEME_MODE] ?: "system" }

    /**
     * 保存主题模式。
     */
    suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { it[KEY_THEME_MODE] = mode }
    }

    /**
     * 获取语言。
     */
    val language: Flow<String> = context.dataStore.data.map { it[KEY_LANGUAGE] ?: "zh-CN" }

    /**
     * 保存语言。
     */
    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[KEY_LANGUAGE] = lang }
    }

    /**
     * 获取字体缩放。
     */
    val fontScale: Flow<Float> = context.dataStore.data.map { it[KEY_FONT_SCALE] ?: 1.0f }

    /**
     * 保存字体缩放。
     */
    suspend fun setFontScale(scale: Float) {
        context.dataStore.edit { it[KEY_FONT_SCALE] = scale }
    }

    /**
     * 获取自动滚动设置。
     */
    val autoScroll: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_SCROLL] ?: true }

    /**
     * 保存自动滚动设置。
     */
    suspend fun setAutoScroll(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_SCROLL] = enabled }
    }

    /**
     * 获取Markdown渲染设置。
     */
    val markdownEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_MARKDOWN_ENABLED] ?: true }

    /**
     * 保存Markdown渲染设置。
     */
    suspend fun setMarkdownEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_MARKDOWN_ENABLED] = enabled }
    }

    /**
     * 获取代码高亮设置。
     */
    val codeHighlight: Flow<Boolean> = context.dataStore.data.map { it[KEY_CODE_HIGHLIGHT] ?: true }

    /**
     * 保存代码高亮设置。
     */
    suspend fun setCodeHighlight(enabled: Boolean) {
        context.dataStore.edit { it[KEY_CODE_HIGHLIGHT] = enabled }
    }

    /**
     * 获取通知设置。
     */
    val notificationEnabled: Flow<Boolean> = context.dataStore.data.map { it[KEY_NOTIFICATION] ?: true }

    /**
     * 保存通知设置。
     */
    suspend fun setNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[KEY_NOTIFICATION] = enabled }
    }

    /**
     * 获取用户Token。
     */
    val userToken: Flow<String?> = context.dataStore.data.map { it[KEY_USER_TOKEN] }

    /**
     * 保存用户Token。
     */
    suspend fun setUserToken(token: String) {
        context.dataStore.edit { it[KEY_USER_TOKEN] = token }
    }

    /**
     * 获取刷新Token。
     */
    val refreshToken: Flow<String?> = context.dataStore.data.map { it[KEY_REFRESH_TOKEN] }

    /**
     * 保存刷新Token。
     */
    suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { it[KEY_REFRESH_TOKEN] = token }
    }

    /**
     * 获取引导完成状态。
     */
    val onboardingCompleted: Flow<Boolean> = context.dataStore.data.map { it[KEY_ONBOARDING_COMPLETED] ?: false }

    /**
     * 保存引导完成状态。
     */
    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[KEY_ONBOARDING_COMPLETED] = completed }
    }

    /**
     * 清除所有偏好设置。
     */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
        Log.d(TAG, "All preferences cleared")
    }
}
