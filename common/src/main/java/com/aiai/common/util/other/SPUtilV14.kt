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
 * SP 工具类 V14。
 */
object SPUtilV14 {
    private const val NAME = "aiai_prefs"
    private lateinit var sp: SharedPreferences
    private val gson = Gson()
    fun init(ctx: Context) { sp = ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE) }
    fun put(k: String, v: String) = sp.edit().putString(k, v).apply()
    fun get(k: String, d: String = "") = sp.getString(k, d) ?: d
    fun put(k: String, v: Int) = sp.edit().putInt(k, v).apply()
    fun get(k: String, d: Int = 0) = sp.getInt(k, d)
    fun put(k: String, v: Boolean) = sp.edit().putBoolean(k, v).apply()
    fun get(k: String, d: Boolean = false) = sp.getBoolean(k, d)
    fun put(k: String, v: Long) = sp.edit().putLong(k, v).apply()
    fun get(k: String, d: Long = 0L) = sp.getLong(k, d)
    fun put(k: String, v: Float) = sp.edit().putFloat(k, v).apply()
    fun get(k: String, d: Float = 0f) = sp.getFloat(k, d)
    fun putObj(k: String, o: Any) = sp.edit().putString(k, gson.toJson(o)).apply()
    fun <T> getObj(k: String, c: Class<T>): T? = try { gson.fromJson(sp.getString(k, null), c) } catch (e: Exception) { null }
    fun remove(k: String) = sp.edit().remove(k).apply()
    fun clear() = sp.edit().clear().apply()
}
