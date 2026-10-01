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
import com.aiai.settings.model.PromptTemplate
import com.aiai.settings.model.PromptCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 提示词使用统计：记录每个模板的使用频次，提供排行。
 */
class PromptStatsManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_prompt_stats", Context.MODE_PRIVATE)
    private val _top = MutableStateFlow<List<PromptTemplate>>(emptyList())
    val top: StateFlow<List<PromptTemplate>> = _top.asStateFlow()

    fun increment(id: String) {
        val count = prefs.getInt(id, 0) + 1
        prefs.edit().putInt(id, count).apply()
    }

    fun getCount(id: String): Int = prefs.getInt(id, 0)

    fun refresh(all: List<PromptTemplate>) {
        _top.value = all.sortedByDescending { getCount(it.id) }.take(10)
    }

    fun clear() = prefs.edit().clear().apply()
}
