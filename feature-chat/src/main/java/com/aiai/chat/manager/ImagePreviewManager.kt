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
 * 消息图片预览管理器
 *
 * 管理聊天中图片消息的预览和缓存。
 */
class ImagePreviewManager(private val context: Context) {

    data class PreviewConfig(
        val showPreview: Boolean = true,
        val previewQuality: Int = 80,
        val cachePreview: Boolean = true
    )

    private val _config = MutableStateFlow(PreviewConfig())
    val config: StateFlow<PreviewConfig> = _config.asStateFlow()

    private val _previewUrls = MutableStateFlow<Map<String, String>>(emptyMap())
    val previewUrls: StateFlow<Map<String, String>> = _previewUrls.asStateFlow()

    fun setPreviewUrl(originalUrl: String, previewUrl: String) {
        _previewUrls.value = _previewUrls.value + (originalUrl to previewUrl)
    }

    fun getPreviewUrl(originalUrl: String): String? {
        return _previewUrls.value[originalUrl]
    }

    fun updateConfig(config: PreviewConfig) {
        _config.value = config
    }
}
