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
import androidx.core.content.edit

/**
 * 搜索历史管理：保存最近搜索关键词。
 */
class SearchHistoryManager(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("aiai_search_history", Context.MODE_PRIVATE)

    /** 历史记录（最新在前）。 */
    fun history(): List<String> =
        prefs.getStringSet(KEY, emptySet())?.toList() ?: emptyList()

    /** 添加搜索词，去重并最多保留 10 条。 */
    fun add(keyword: String) {
        if (keyword.isBlank()) return
        val set = history().toMutableList()
        set.remove(keyword)
        set.add(0, keyword)
        prefs.edit().putStringSet(KEY, set.take(10).toSet()).apply()
    }

    fun clear() = prefs.edit().remove(KEY).apply()

    companion object {
        private const val KEY = "search_history"
    }
}
