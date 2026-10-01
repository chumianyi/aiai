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
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * DataStore 管理工具类。
 *
 * 基于 Jetpack DataStore，协程异步读写。
 */
object DataStoreManager {

    private val Context.dataStore by preferencesDataStore("aiai_settings")
    private lateinit var appContext: Context

    /** 初始化。 */
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    /** 读取字符串 Flow。 */
    fun readString(key: String, default: String = ""): Flow<String> {
        return appContext.dataStore.data.map { it[stringPreferencesKey(key)] ?: default }
    }

    /** 写入字符串。 */
    suspend fun writeString(key: String, value: String) {
        appContext.dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    /** 一次性读取。 */
    suspend fun readStringOnce(key: String, default: String = ""): String {
        return readString(key, default).first()
    }
}
