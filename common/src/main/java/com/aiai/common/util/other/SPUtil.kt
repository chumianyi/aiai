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
 * SharedPreferences 工具类。
 */
object SPUtil {

    private const val PREF_NAME = "aiai_prefs"
    private lateinit var sp: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) = sp.edit().putString(key, value).apply()
    fun getString(key: String, def: String = "") = sp.getString(key, def) ?: def

    fun putInt(key: String, value: Int) = sp.edit().putInt(key, value).apply()
    fun getInt(key: String, def: Int = 0) = sp.getInt(key, def)

    fun putLong(key: String, value: Long) = sp.edit().putLong(key, value).apply()
    fun getLong(key: String, def: Long = 0L) = sp.getLong(key, def)

    fun putBoolean(key: String, value: Boolean) = sp.edit().putBoolean(key, value).apply()
    fun getBoolean(key: String, def: Boolean = false) = sp.getBoolean(key, def)

    fun putFloat(key: String, value: Float) = sp.edit().putFloat(key, value).apply()
    fun getFloat(key: String, def: Float = 0f) = sp.getFloat(key, def)

    fun putObject(key: String, obj: Any) = sp.edit().putString(key, gson.toJson(obj)).apply()
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return try { gson.fromJson(sp.getString(key, null), clazz) } catch (e: Exception) { null }
    }

    fun remove(key: String) = sp.edit().remove(key).apply()
    fun clear() = sp.edit().clear().apply()
}
