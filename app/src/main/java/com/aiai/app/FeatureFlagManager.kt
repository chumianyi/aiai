/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.content.Context
import com.aiai.core.util.LogUtil
import com.tencent.mmkv.MMKV
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 功能开关管理器
 *
 * 控制实验性功能开关，支持远程配置覆盖。
 */
@Singleton
class FeatureFlagManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val TAG = "FeatureFlagManager"
        // 功能开关key
        const val FLAG_STREAM_RESPONSE = "ff_stream_response"
        const val FLAG_MARKDOWN_RENDER = "ff_markdown_render"
        const val FLAG_CODE_HIGHLIGHT = "ff_code_highlight"
        const val FLAG_VOICE_INPUT = "ff_voice_input"
        const val FLAG_IMAGE_INPUT = "ff_image_input"
        const val FLAG_PLUGIN_SYSTEM = "ff_plugin_system"
        const val FLAG_EXPORT_CHAT = "ff_export_chat"
        const val FLAG_CLOUD_SYNC = "ff_cloud_sync"
        const val FLAG_DYNAMIC_THEME = "ff_dynamic_theme"
        const val FLAG_PROMPT_LIBRARY = "ff_prompt_library"
        const val FLAG_SEARCH_HISTORY = "ff_search_history"
        const val FLAG_WIDGET = "ff_widget"
        const val FLAG_SHORTCUTS = "ff_shortcuts"
        const val FLAG_BACKUP = "ff_backup"
        const val FLAG_SHARE = "ff_share"
    }

    private val mmkv: MMKV = MMKV.mmkvWithID(AppConstants.MMKV_ID)

    /**
     * 加载默认功能开关
     */
    fun loadFeatureFlags() {
        // 默认开启的功能
        setDefault(FLAG_STREAM_RESPONSE, true)
        setDefault(FLAG_MARKDOWN_RENDER, true)
        setDefault(FLAG_CODE_HIGHLIGHT, true)
        setDefault(FLAG_VOICE_INPUT, true)
        setDefault(FLAG_IMAGE_INPUT, false) // 默认关闭，需要OCR能力
        setDefault(FLAG_PLUGIN_SYSTEM, false) // 实验性
        setDefault(FLAG_EXPORT_CHAT, true)
        setDefault(FLAG_CLOUD_SYNC, false)
        setDefault(FLAG_DYNAMIC_THEME, true)
        setDefault(FLAG_PROMPT_LIBRARY, true)
        setDefault(FLAG_SEARCH_HISTORY, true)
        setDefault(FLAG_WIDGET, true)
        setDefault(FLAG_SHORTCUTS, true)
        setDefault(FLAG_BACKUP, true)
        setDefault(FLAG_SHARE, true)
        LogUtil.d(TAG, "Feature flags loaded")
    }

    private fun setDefault(key: String, default: Boolean) {
        if (!mmkv.containsKey(key)) {
            mmkv.putBoolean(key, default)
        }
    }

    /**
     * 查询功能开关是否开启
     */
    fun isEnabled(key: String): Boolean {
        return mmkv.getBoolean(key, false)
    }

    /**
     * 设置功能开关
     */
    fun setEnabled(key: String, enabled: Boolean) {
        mmkv.putBoolean(key, enabled)
        LogUtil.d(TAG, "Feature flag $key = $enabled")
    }

    /**
     * 获取所有开关状态
     */
    fun getAllFlags(): Map<String, Boolean> {
        return mapOf(
            FLAG_STREAM_RESPONSE to isEnabled(FLAG_STREAM_RESPONSE),
            FLAG_MARKDOWN_RENDER to isEnabled(FLAG_MARKDOWN_RENDER),
            FLAG_CODE_HIGHLIGHT to isEnabled(FLAG_CODE_HIGHLIGHT),
            FLAG_VOICE_INPUT to isEnabled(FLAG_VOICE_INPUT),
            FLAG_IMAGE_INPUT to isEnabled(FLAG_IMAGE_INPUT),
            FLAG_PLUGIN_SYSTEM to isEnabled(FLAG_PLUGIN_SYSTEM),
            FLAG_EXPORT_CHAT to isEnabled(FLAG_EXPORT_CHAT),
            FLAG_CLOUD_SYNC to isEnabled(FLAG_CLOUD_SYNC),
            FLAG_DYNAMIC_THEME to isEnabled(FLAG_DYNAMIC_THEME),
            FLAG_PROMPT_LIBRARY to isEnabled(FLAG_PROMPT_LIBRARY)
        )
    }
}
