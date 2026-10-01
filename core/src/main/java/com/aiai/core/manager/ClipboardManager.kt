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
package com.aiai.core.manager

import android.content.Context
import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log

/**
 * 剪贴板管理器。
 *
 * 提供文本复制、监听、清空等功能。
 */
object ClipboardManager {

    private const val TAG = "ClipboardManager"
    private const val MAX_HISTORY = 20

    private var clipboardManager: ClipboardManager? = null
    private val history = mutableListOf<String>()
    private var onPrimaryClipChangedListener: (() -> Unit)? = null

    /**
     * 初始化。
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager?.addPrimaryClipChangedListener {
            val text = getText()
            if (!text.isNullOrBlank() && !history.contains(text)) {
                history.add(0, text)
                if (history.size > MAX_HISTORY) {
                    history.removeAt(history.size - 1)
                }
                onPrimaryClipChangedListener?.invoke()
            }
        }
        Log.d(TAG, "ClipboardManager initialized")
    }

    /**
     * 复制文本。
     *
     * @param text 文本
     * @param label 标签
     */
    fun copyText(text: String, label: String = "text") {
        val clip = ClipData.newPlainText(label, text)
        clipboardManager?.setPrimaryClip(clip)
        Log.d(TAG, "Text copied: ${text.take(20)}...")
    }

    /**
     * 获取剪贴板文本。
     *
     * @return 剪贴板文本
     */
    fun getText(): String? {
        val clip = clipboardManager?.primaryClip ?: return null
        if (clip.itemCount == 0) return null
        return clip.getItemAt(0).text?.toString()
    }

    /**
     * 是否有文本。
     *
     * @return 是否有文本
     */
    fun hasText(): Boolean {
        return !getText().isNullOrBlank()
    }

    /**
     * 清空剪贴板。
     */
    fun clear() {
        clipboardManager?.setPrimaryClip(ClipData.newPlainText("", ""))
        Log.d(TAG, "Clipboard cleared")
    }

    /**
     * 获取历史记录。
     *
     * @return 历史记录列表
     */
    fun getHistory(): List<String> {
        return history.toList()
    }

    /**
     * 清除历史记录。
     */
    fun clearHistory() {
        history.clear()
        Log.d(TAG, "Clipboard history cleared")
    }

    /**
     * 设置剪贴板变化监听。
     *
     * @param listener 监听器
     */
    fun setOnPrimaryClipChangedListener(listener: () -> Unit) {
        onPrimaryClipChangedListener = listener
    }

    /**
     * 历史记录数量。
     */
    fun historyCount(): Int = history.size
}
