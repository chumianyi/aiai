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
 * 附件面板管理器
 *
 * 管理附件选择面板的显示和配置。
 */
class AttachmentPanelConfigManager(private val context: Context) {

    data class PanelConfig(
        val showCamera: Boolean = true,
        val showGallery: Boolean = true,
        val showFile: Boolean = true,
        val showVoice: Boolean = true,
        val showLocation: Boolean = false
    )

    private val _config = MutableStateFlow(PanelConfig())
    val config: StateFlow<PanelConfig> = _config.asStateFlow()

    fun updateConfig(config: PanelConfig) {
        _config.value = config
    }

    fun shouldShowCamera(): Boolean {
        return _config.value.showCamera
    }

    fun shouldShowGallery(): Boolean {
        return _config.value.showGallery
    }
}
