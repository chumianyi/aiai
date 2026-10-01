/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import android.content.Context
import android.content.SharedPreferences
import android.os.MemoryFile
/**
 * SharedPreferences 工具：便捷读写、批量提交、清除分组。
 */
class PrefUtils(context: Context, name: String = "aiai_prefs") {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun putString(key: String, value: String) = prefs.edit().putString(key, value).apply()
    fun getString(key: String, def: String = ""): String = prefs.getString(key, def) ?: def

    fun putInt(key: String, value: Int) = prefs.edit().putInt(key, value).apply()
    fun getInt(key: String, def: Int = 0): Int = prefs.getInt(key, def)

    fun putLong(key: String, value: Long) = prefs.edit().putLong(key, value).apply()
    fun getLong(key: String, def: Long = 0L): Long = prefs.getLong(key, def)

    fun putFloat(key: String, value: Float) = prefs.edit().putFloat(key, value).apply()
    fun getFloat(key: String, def: Float = 0f): Float = prefs.getFloat(key, def)

    fun putBoolean(key: String, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
    fun getBoolean(key: String, def: Boolean = false): Boolean = prefs.getBoolean(key, def)

    fun putStringSet(key: String, set: Set<String>) = prefs.edit().putStringSet(key, set).apply()
    fun getStringSet(key: String): Set<String> = prefs.getStringSet(key, emptySet()) ?: emptySet()

    fun remove(key: String) = prefs.edit().remove(key).apply()

    fun clear() = prefs.edit().clear().apply()

    fun contains(key: String): Boolean = prefs.contains(key)

    /** 同步提交（需要立即落盘时使用）。 */
    fun commit() = prefs.edit().commit()
}
