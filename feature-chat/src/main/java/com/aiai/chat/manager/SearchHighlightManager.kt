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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息搜索高亮管理器
 *
 * 管理搜索结果中关键词的高亮显示。
 */
class SearchHighlightManager(private val context: Context) {

    data class HighlightRange(
        val start: Int,
        val end: Int
    )

    private val _currentKeyword = MutableStateFlow("")
    val currentKeyword: StateFlow<String> = _currentKeyword.asStateFlow()

    fun setKeyword(keyword: String) {
        _currentKeyword.value = keyword
    }

    fun clearKeyword() {
        _currentKeyword.value = ""
    }

    fun getHighlightRanges(text: String): List<HighlightRange> {
        val keyword = _currentKeyword.value
        if (keyword.isBlank() || text.isBlank()) return emptyList()
        val ranges = mutableListOf<HighlightRange>()
        var index = 0
        while (index < text.length) {
            val found = text.indexOf(keyword, index, ignoreCase = true)
            if (found < 0) break
            ranges.add(HighlightRange(found, found + keyword.length))
            index = found + keyword.length
        }
        return ranges
    }

    fun hasKeyword(): Boolean {
        return _currentKeyword.value.isNotBlank()
    }
}
