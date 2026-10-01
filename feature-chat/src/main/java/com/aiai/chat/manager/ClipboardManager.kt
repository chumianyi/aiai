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
 * 剪贴板管理器
 *
 * 管理消息复制到剪贴板的行为，支持历史记录和自动清理。
 */
class ClipboardManager(private val context: Context) {

    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager

    private val _clipHistory = MutableStateFlow<List<String>>(emptyList())
    val clipHistory: StateFlow<List<String>> = _clipHistory.asStateFlow()

    private val maxHistorySize = 20

    /**
     * 复制文本到剪贴板
     */
    fun copyText(label: String, text: String) {
        val clip = android.content.ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        addToHistory(text)
    }

    /**
     * 获取剪贴板文本
     */
    fun getClipText(): String? {
        val clip = clipboard.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        return clip.getItemAt(0).text?.toString()
    }

    private fun addToHistory(text: String) {
        val current = _clipHistory.value.toMutableList()
        current.remove(text)
        current.add(0, text)
        _clipHistory.value = current.take(maxHistorySize)
    }

    fun clearHistory() {
        _clipHistory.value = emptyList()
    }
}
