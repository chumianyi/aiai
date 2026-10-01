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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * SharedPreferences 封装工具类。
 *
 * 支持对象存储。
 */
object SPUtils {

    private const val PREF_NAME = "aiai_prefs"
    private lateinit var sp: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // region 基本类型

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

    // endregion

    // region 对象

    fun putObject(key: String, obj: Any) {
        sp.edit().putString(key, gson.toJson(obj)).apply()
    }

    fun <T> getObject(key: String, clazz: Class<T>): T? {
        val json = sp.getString(key, null) ?: return null
        return try {
            gson.fromJson(json, clazz)
        } catch (e: Exception) {
            null
        }
    }

    // endregion

    // region 其他

    fun remove(key: String) = sp.edit().remove(key).apply()
    fun clear() = sp.edit().clear().apply()
    fun contains(key: String) = sp.contains(key)

    // endregion
}
