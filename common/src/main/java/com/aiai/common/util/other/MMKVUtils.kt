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
 * MMKV 封装工具类。
 */
object MMKVUtils {

    private const val MMKV_NAME = "aiai_mmkv"
    private lateinit var mmkv: com.tencent.mmkv.MMKV
    private val gson = Gson()

    fun init(context: Context) {
        com.tencent.mmkv.MMKV.initialize(context)
        mmkv = com.tencent.mmkv.MMKV.mmkvWithID(MMKV_NAME)
    }

    fun putString(key: String, value: String) = mmkv.encode(key, value)
    fun getString(key: String, def: String = "") = mmkv.decodeString(key, def) ?: def

    fun putInt(key: String, value: Int) = mmkv.encode(key, value)
    fun getInt(key: String, def: Int = 0) = mmkv.decodeInt(key, def)

    fun putLong(key: String, value: Long) = mmkv.encode(key, value)
    fun getLong(key: String, def: Long = 0L) = mmkv.decodeLong(key, def)

    fun putBoolean(key: String, value: Boolean) = mmkv.encode(key, value)
    fun getBoolean(key: String, def: Boolean = false) = mmkv.decodeBool(key, def)

    fun putFloat(key: String, value: Float) = mmkv.encode(key, value)
    fun getFloat(key: String, def: Float = 0f) = mmkv.decodeFloat(key, def)

    fun putObject(key: String, obj: Any) = mmkv.encode(key, gson.toJson(obj))
    fun <T> getObject(key: String, clazz: Class<T>): T? {
        return try { gson.fromJson(mmkv.decodeString(key, null), clazz) } catch (e: Exception) { null }
    }

    fun remove(key: String) = mmkv.remove(key)
    fun clear() = mmkv.clearAll()
}
