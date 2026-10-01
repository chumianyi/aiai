/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import com.aiai.chat.data.model.PromptSuggestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 提示词收藏管理器
 *
 * 管理用户收藏的提示词模板。
 */
class PromptFavoriteManager(private val context: Context) {

    private val _favoritePrompts = MutableStateFlow<List<PromptSuggestion>>(emptyList())
    val favoritePrompts: StateFlow<List<PromptSuggestion>> = _favoritePrompts.asStateFlow()

    fun addFavorite(prompt: PromptSuggestion) {
        val current = _favoritePrompts.value
        if (current.none { it.id == prompt.id }) {
            _favoritePrompts.value = current + prompt
        }
    }

    fun removeFavorite(promptId: String) {
        _favoritePrompts.value = _favoritePrompts.value.filterNot { it.id == promptId }
    }

    fun isFavorite(promptId: String): Boolean {
        return _favoritePrompts.value.any { it.id == promptId }
    }

    fun toggleFavorite(prompt: PromptSuggestion): Boolean {
        return if (isFavorite(prompt.id)) {
            removeFavorite(prompt.id)
            false
        } else {
            addFavorite(prompt)
            true
        }
    }

    fun clearAll() {
        _favoritePrompts.value = emptyList()
    }
}
