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
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.core

import com.aiai.common.util.storage.MMKVManager

/**
 * 应用配置管理类。
 *
 * 读取 BuildConfig 和本地配置（MMKV）。
 */
object AppConfig {

    // region API 配置
    var apiBaseUrl: String
        get() = MMKVManager.getString(Constants.KEY_API_BASE_URL, "https://api.example.com/v1")
        set(value) { MMKVManager.putString(Constants.KEY_API_BASE_URL, value) }

    var apiKey: String
        get() = MMKVManager.getString(Constants.KEY_API_KEY, "")
        set(value) { MMKVManager.putString(Constants.KEY_API_KEY, value) }

    var modelName: String
        get() = MMKVManager.getString(Constants.KEY_MODEL_NAME, "gpt-4o")
        set(value) { MMKVManager.putString(Constants.KEY_MODEL_NAME, value) }
    // endregion

    // region 用户配置
    var userId: String
        get() = MMKVManager.getString("user_id", "")
        set(value) { MMKVManager.putString("user_id", value) }

    var token: String
        get() = MMKVManager.getString("token", "")
        set(value) { MMKVManager.putString("token", value) }

    var isLoggedIn: Boolean
        get() = MMKVManager.getBoolean("is_logged_in", false)
        set(value) { MMKVManager.putBoolean("is_logged_in", value) }
    // endregion

    // region 主题
    var themeMode: Int
        get() = MMKVManager.getInt("theme_mode", 0) // 0=跟随系统
        set(value) { MMKVManager.putInt("theme_mode", value) }

    var languageMode: Int
        get() = MMKVManager.getInt("language_mode", 0) // 0=跟随系统
        set(value) { MMKVManager.putInt("language_mode", value) }
    // endregion

    // region 调试
    var isDebug: Boolean = false
    var isBeta: Boolean = false
    var versionName: String = ""
    var versionCode: Long = 0L
    // endregion
}
