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
 * 链接预览管理器
 *
 * 管理消息中链接的预览卡片。
 */
class LinkPreviewManager(private val context: Context) {

    data class LinkPreview(
        val url: String,
        val title: String,
        val description: String,
        val imageUrl: String? = null,
        val siteName: String = ""
    )

    private val _previews = MutableStateFlow<Map<String, LinkPreview>>(emptyMap())
    val previews: StateFlow<Map<String, LinkPreview>> = _previews.asStateFlow()

    suspend fun fetchPreview(url: String): LinkPreview? {
        // 模拟获取链接预览
        val preview = LinkPreview(
            url = url,
            title = "示例网站",
            description = "这是一个示例网站的描述",
            siteName = "example.com"
        )
        _previews.value = _previews.value + (url to preview)
        return preview
    }

    fun getPreview(url: String): LinkPreview? {
        return _previews.value[url]
    }

    fun clearPreview(url: String) {
        _previews.value = _previews.value - url
    }
}
