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
package com.aiai.common.util.storage

import android.content.Context
import android.content.SharedPreferences

/**
 * SharedPreferences 管理工具类。
 *
 * 封装 SP 读写，支持各种基本类型。
 */
object SPManager {

    private const val PREFS_NAME = "aiai_prefs"
    private lateinit var sp: SharedPreferences

    /** 初始化（在 Application 中调用）。 */
    fun init(context: Context) {
        sp = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) = sp.edit().putString(key, value).apply()
    fun getString(key: String, default: String = "") = sp.getString(key, default) ?: default

    fun putInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()
    fun getInt(key: String, default: Int = 0) = sp.getInt(key, default)

    fun putLong(key: String, value: Long) = sp.edit().putLong(key, value).apply()
    fun getLong(key: String, default: Long = 0L) = sp.getLong(key, default)

    fun putBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()
    fun getBoolean(key: String, default: Boolean = false) = sp.getBoolean(key, default)

    fun putFloat(key: String, value: Float) = sp.edit().putFloat(key, value).apply()
    fun getFloat(key: String, default: Float = 0f) = sp.getFloat(key, default)

    fun remove(key: String) = sp.edit().remove(key).apply()
    fun clear() = sp.edit().clear().apply()
}
