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
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.FavoriteItem
import com.aiai.settings.model.FavoriteType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 收藏管理：持久化收藏列表，支持分类、搜索、导出。
 */
class FavoriteManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_favorites", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _items = mutableListOf<FavoriteItem>()
    val items: List<FavoriteItem> get() = _items.toList()

    init { load() }

    private fun load() {
        val raw = prefs.getString("favs", null) ?: return
        try {
            val type = object : TypeToken<List<FavoriteItem>>() {}.type
            _items.clear(); _items.addAll(gson.fromJson(raw, type))
        } catch (_: Exception) {}
    }

    private fun persist() {
        prefs.edit().putString("favs", gson.toJson(_items)).apply()
    }

    fun add(item: FavoriteItem) {
        _items.add(0, item); persist()
    }

    fun remove(id: String) {
        _items.removeAll { it.id == id }; persist()
    }

    fun clear() { _items.clear(); persist() }

    fun filter(query: String, type: FavoriteType?): List<FavoriteItem> {
        return _items.filter {
            (type == null || it.type == type) &&
                (query.isBlank() || it.title.contains(query, true) || it.summary.contains(query, true))
        }
    }

    fun categories(): List<String> = _items.map { it.category }.distinct()

    /** 导出为 Markdown。 */
    fun exportToMarkdown(): String {
        return buildString {
            appendLine("# 爱Ai 收藏导出")
            appendLine()
            _items.forEach {
                appendLine("## ${it.title}")
                appendLine(it.summary)
                appendLine()
            }
        }
    }
}
