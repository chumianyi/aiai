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
import com.aiai.settings.manager.PromptManager
import com.aiai.settings.model.PromptCategory
import com.aiai.settings.model.PromptTemplate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 提示词库 UI 状态。 */
data class PromptLibraryUiState(
    val categories: List<PromptCategory> = PromptCategory.entries,
    val selectedCategory: PromptCategory? = null,
    val query: String = "",
    val prompts: List<PromptTemplate> = emptyList(),
    val showOnlyFav: Boolean = false
)

/**
 * 提示词库 ViewModel。
 */
class PromptLibraryViewModel(app: Application) : AndroidViewModel(app) {

    private val manager = PromptManager.get(app)

    private val _uiState = MutableStateFlow(PromptLibraryUiState())
    val uiState: StateFlow<PromptLibraryUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val s = _uiState.value
        val list = manager.filter(s.query, s.selectedCategory)
            .let { all -> if (s.showOnlyFav) all.filter { manager.isFavorite(it.id) } else all }
        _uiState.value = s.copy(prompts = list)
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        refresh()
    }

    fun selectCategory(category: PromptCategory?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        refresh()
    }

    fun toggleFavorite(id: String) {
        manager.toggleFavorite(id)
        refresh()
    }

    fun toggleShowFav() {
        _uiState.value = _uiState.value.copy(showOnlyFav = !_uiState.value.showOnlyFav)
        refresh()
    }

    /** 记录使用并返回模板内容。 */
    fun use(id: String): PromptTemplate? {
        manager.markUsed(id)
        refresh()
        return manager.all().firstOrNull { it.id == id }
    }

    /** 保存自定义模板。 */
    fun saveCustom(template: PromptTemplate) {
        manager.saveCustom(template)
        refresh()
    }

    fun deleteCustom(id: String) {
        manager.deleteCustom(id)
        refresh()
    }
}
