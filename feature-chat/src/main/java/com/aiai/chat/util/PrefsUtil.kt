/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.aiai.chat.data.model.ModelConfig

/**
 * SharedPreferences工具类
 *
 * 封装本地轻量数据存储，提供类型安全的读写方法。
 */
class PrefsUtil(context: Context) {

    private val prefs: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    companion object {
        private const val KEY_API_BASE_URL = "api_base_url"
        private const val KEY_API_KEY = "api_key"
        private const val KEY_DEFAULT_MODEL = "default_model"
        private const val KEY_SYSTEM_PROMPT = "system_prompt"
        private const val KEY_TEMPERATURE = "temperature"
        private const val KEY_MAX_TOKENS = "max_tokens"
        private const val KEY_TOP_P = "top_p"
        private const val KEY_FIRST_LAUNCH = "first_launch"
        private const val KEY_HAS_SEEN_GUIDE = "has_seen_guide"
        private const val KEY_USER_NICKNAME = "user_nickname"
        private const val KEY_USER_AVATAR = "user_avatar"
        private const val KEY_DAILY_MESSAGE_COUNT = "daily_message_count"
        private const val KEY_DAILY_COUNT_DATE = "daily_count_date"
        private const val KEY_TOTAL_MESSAGES = "total_messages"
        private const val KEY_TOTAL_CONVERSATIONS = "total_conversations"
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATE_ENABLED = "vibrate_enabled"
        private const val KEY_KEEP_SCREEN_ON = "keep_screen_on"
        private const val KEY_STREAM_OUTPUT = "stream_output"
        private const val KEY_CODE_HIGHLIGHT = "code_highlight"
        private const val KEY_FONT_SIZE = "font_size"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_LAST_CONVERSATION_ID = "last_conversation_id"
        private const val KEY_SEARCH_HISTORY = "search_history"
    }

    // === API配置 ===

    var apiBaseUrl: String
        get() = prefs.getString(KEY_API_BASE_URL, "https://api.openai.com/v1") ?: ""
        set(value) = prefs.edit().putString(KEY_API_BASE_URL, value).apply()

    var apiKey: String
        get() = prefs.getString(KEY_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_API_KEY, value).apply()

    var defaultModel: String
        get() = prefs.getString(KEY_DEFAULT_MODEL, "gpt-4o-mini") ?: "gpt-4o-mini"
        set(value) = prefs.edit().putString(KEY_DEFAULT_MODEL, value).apply()

    // === 生成参数 ===

    var systemPrompt: String
        get() = prefs.getString(KEY_SYSTEM_PROMPT, "") ?: ""
        set(value) = prefs.edit().putString(KEY_SYSTEM_PROMPT, value).apply()

    var temperature: Float
        get() = prefs.getFloat(KEY_TEMPERATURE, 0.7f)
        set(value) = prefs.edit().putFloat(KEY_TEMPERATURE, value).apply()

    var maxTokens: Int
        get() = prefs.getInt(KEY_MAX_TOKENS, 2048)
        set(value) = prefs.edit().putInt(KEY_MAX_TOKENS, value).apply()

    var topP: Float
        get() = prefs.getFloat(KEY_TOP_P, 1.0f)
        set(value) = prefs.edit().putFloat(KEY_TOP_P, value).apply()

    // === 用户信息 ===

    var userNickname: String
        get() = prefs.getString(KEY_USER_NICKNAME, "爱Ai用户") ?: "爱Ai用户"
        set(value) = prefs.edit().putString(KEY_USER_NICKNAME, value).apply()

    var userAvatar: String
        get() = prefs.getString(KEY_USER_AVATAR, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_AVATAR, value).apply()

    // === 统计 ===

    var totalMessages: Int
        get() = prefs.getInt(KEY_TOTAL_MESSAGES, 0)
        set(value) = prefs.edit().putInt(KEY_TOTAL_MESSAGES, value).apply()

    var totalConversations: Int
        get() = prefs.getInt(KEY_TOTAL_CONVERSATIONS, 0)
        set(value) = prefs.edit().putInt(KEY_TOTAL_CONVERSATIONS, value).apply()

    fun incrementMessageCount() {
        totalMessages = totalMessages + 1
    }

    fun incrementConversationCount() {
        totalConversations = totalConversations + 1
    }

    // === 设置 ===

    var notificationEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION_ENABLED, value).apply()

    var soundEnabled: Boolean
        get() = prefs.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_SOUND_ENABLED, value).apply()

    var vibrateEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATE_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_VIBRATE_ENABLED, value).apply()

    var keepScreenOn: Boolean
        get() = prefs.getBoolean(KEY_KEEP_SCREEN_ON, false)
        set(value) = prefs.edit().putBoolean(KEY_KEEP_SCREEN_ON, value).apply()

    var streamOutput: Boolean
        get() = prefs.getBoolean(KEY_STREAM_OUTPUT, true)
        set(value) = prefs.edit().putBoolean(KEY_STREAM_OUTPUT, value).apply()

    var codeHighlight: Boolean
        get() = prefs.getBoolean(KEY_CODE_HIGHLIGHT, true)
        set(value) = prefs.edit().putBoolean(KEY_CODE_HIGHLIGHT, value).apply()

    var fontSize: Int
        get() = prefs.getInt(KEY_FONT_SIZE, 15)
        set(value) = prefs.edit().putInt(KEY_FONT_SIZE, value).apply()

    var themeMode: String
        get() = prefs.getString(KEY_THEME_MODE, "system") ?: "system"
        set(value) = prefs.edit().putString(KEY_THEME_MODE, value).apply()

    // === 其他 ===

    var firstLaunch: Boolean
        get() = prefs.getBoolean(KEY_FIRST_LAUNCH, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_LAUNCH, value).apply()

    var hasSeenGuide: Boolean
        get() = prefs.getBoolean(KEY_HAS_SEEN_GUIDE, false)
        set(value) = prefs.edit().putBoolean(KEY_HAS_SEEN_GUIDE, value).apply()

    var lastConversationId: String
        get() = prefs.getString(KEY_LAST_CONVERSATION_ID, "") ?: ""
        set(value) = prefs.edit().putString(KEY_LAST_CONVERSATION_ID, value).apply()

    /**
     * 清除所有数据
     */
    fun clear() {
        prefs.edit().clear().apply()
    }
}
