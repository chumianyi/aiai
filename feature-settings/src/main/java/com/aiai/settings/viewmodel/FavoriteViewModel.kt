/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aiai.settings.model.FavoriteItem
import com.aiai.settings.model.FavoriteType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 收藏管理 UI 状态。 */
data class FavoriteUiState(
    val all: List<FavoriteItem> = emptyList(),
    val query: String = "",
    val selectedCategory: String? = null,
    val selectedType: FavoriteType? = null,
    val filtered: List<FavoriteItem> = emptyList()
)

/**
 * 收藏管理 ViewModel。
 */
class FavoriteViewModel(app: Application) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(
        FavoriteUiState(all = mockFavorites(), filtered = mockFavorites())
    )
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    private fun mockFavorites(): List<FavoriteItem> {
        val now = System.currentTimeMillis()
        return listOf(
            FavoriteItem("f1", FavoriteType.PROMPT, "小红书文案模板", "标题吸引人，结尾互动",
                "请以小红书风格写一篇笔记…", category = "提示词", createdAt = now - 1000),
            FavoriteItem("f2", FavoriteType.CHAT_MESSAGE, "关于 Transformer 的解释", "Self-Attention 机制…",
                "Transformer 完全基于注意力机制…", category = "对话", createdAt = now - 2000),
            FavoriteItem("f3", FavoriteType.IMAGE, "赛博朋克城市", "霓虹灯、雨夜", null,
                category = "图片", createdAt = now - 3000),
            FavoriteItem("f4", FavoriteType.ARTICLE, "Kotlin 协程入门", "挂起函数原理",
                "suspend 不阻塞线程…", category = "文章", createdAt = now - 4000)
        )
    }

    fun search(q: String) {
        _uiState.value = _uiState.value.copy(query = q)
        applyFilter()
    }

    fun filterByCategory(cat: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = cat)
        applyFilter()
    }

    fun filterByType(type: FavoriteType?) {
        _uiState.value = _uiState.value.copy(selectedType = type)
        applyFilter()
    }

    private fun applyFilter() {
        val s = _uiState.value
        val list = s.all.filter {
            (s.selectedCategory == null || it.category == s.selectedCategory) &&
                (s.selectedType == null || it.type == s.selectedType) &&
                (s.query.isBlank() || it.title.contains(s.query, true) || it.summary.contains(s.query, true))
        }
        _uiState.value = s.copy(filtered = list)
    }

    fun remove(id: String) {
        _uiState.value = _uiState.value.copy(all = _uiState.value.all.filterNot { it.id == id })
        applyFilter()
    }

    /** 导出为文本。 */
    fun export(): String {
        return buildString {
            _uiState.value.filtered.forEach {
                appendLine("【${it.title}】")
                appendLine(it.summary)
                appendLine()
            }
        }
    }
}
