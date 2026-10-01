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
import com.tencent.mmkv.MMKV

/**
 * MMKV 存储管理工具类。
 *
 * MMKV 是腾讯开源的高性能键值存储，基于 mmap。
 */
object MMKVManager {

    private const val MMKV_NAME = "aiai_mmkv"
    private lateinit var mmkv: MMKV

    /** 初始化（在 Application 中调用）。 */
    fun init(context: Context) {
        MMKV.initialize(context)
        mmkv = MMKV.mmkvWithID(MMKV_NAME)
    }

    fun putString(key: String, value: String) = mmkv.encode(key, value)
    fun getString(key: String, default: String = "") = mmkv.decodeString(key, default) ?: default

    fun putInt(key: String, value: Int) = mmkv.encode(key, value)
    fun getInt(key: String, default: Int = 0) = mmkv.decodeInt(key, default)

    fun putLong(key: String, value: Long) = mmkv.encode(key, value)
    fun getLong(key: String, default: Long = 0L) = mmkv.decodeLong(key, default)

    fun putBoolean(key: String, value: Boolean) = mmkv.encode(key, value)
    fun getBoolean(key: String, default: Boolean = false) = mmkv.decodeBool(key, default)

    fun putFloat(key: String, value: Float) = mmkv.encode(key, value)
    fun getFloat(key: String, default: Float = 0f) = mmkv.decodeFloat(key, default)

    fun remove(key: String) = mmkv.remove(key)
    fun clear() = mmkv.clearAll()
}
