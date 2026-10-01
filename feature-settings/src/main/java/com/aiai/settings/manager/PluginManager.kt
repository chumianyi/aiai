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
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.PluginInfo
import com.aiai.settings.model.PluginStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 插件管理。
 *
 * 内置一批插件（文本处理、图像工具、知识库等），支持：
 * - 发现列表；
 * - 安装 / 卸载（模拟进度）；
 * - 启用 / 禁用；
 * - 调用入口占位。
 */
class PluginManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _plugins = MutableStateFlow(buildBuiltin())
    /** 全部插件流。 */
    val plugins: StateFlow<List<PluginInfo>> = _plugins.asStateFlow()

    private fun buildBuiltin(): List<PluginInfo> {
        val enabledSet = prefs.getStringSet(KEY_ENABLED, emptySet()) ?: emptySet()
        return listOf(
            PluginInfo("p_search", "联网搜索", "为 AI 接入实时网页搜索能力", "1.0.0", "AiAi",
                downloadSize = 120L * 1024, isBuiltin = true,
                status = if (enabledSet.contains("p_search")) PluginStatus.ENABLED else PluginStatus.INSTALLED),
            PluginInfo("p_translate", "实时翻译", "对话中自动翻译多语言内容", "1.2.0", "AiAi",
                downloadSize = 256L * 1024, isBuiltin = true,
                status = if (enabledSet.contains("p_translate")) PluginStatus.ENABLED else PluginStatus.INSTALLED),
            PluginInfo("p_code", "代码解释器", "运行沙箱代码，做数据分析", "0.9.5", "AiAi Lab",
                downloadSize = 8L * 1024 * 1024, isBuiltin = true,
                status = if (enabledSet.contains("p_code")) PluginStatus.ENABLED else PluginStatus.INSTALLED),
            PluginInfo("p_ocr", "图片文字识别", "识别截图、照片中的文字", "1.1.0", "AiAi",
                downloadSize = 4L * 1024 * 1024,
                status = PluginStatus.NOT_INSTALLED),
            PluginInfo("p_tts", "语音合成", "把回复朗读出来", "1.0.3", "Voice Team",
                downloadSize = 16L * 1024 * 1024,
                status = PluginStatus.NOT_INSTALLED),
            PluginInfo("p_math", "数学公式", "渲染 LaTeX 公式", "2.0.1", "OpenSource",
                downloadSize = 512L * 1024,
                status = PluginStatus.NOT_INSTALLED)
        )
    }

    /**
     * 模拟安装 [id]，依次切换状态。
     */
    suspend fun install(id: String) {
        updateStatus(id, PluginStatus.INSTALLING)
        delay(800) // 模拟下载
        updateStatus(id, PluginStatus.INSTALLED)
    }

    /** 卸载。 */
    fun uninstall(id: String) {
        updateStatus(id, PluginStatus.NOT_INSTALLED)
        setEnabled(id, false)
    }

    /** 启用 / 禁用。 */
    fun setEnabled(id: String, enabled: Boolean) {
        val set = (prefs.getStringSet(KEY_ENABLED, emptySet()) ?: emptySet()).toMutableSet()
        if (enabled) set.add(id) else set.remove(id)
        prefs.edit().putStringSet(KEY_ENABLED, set).apply()
        updateStatus(id, if (enabled) PluginStatus.ENABLED else PluginStatus.INSTALLED)
    }

    private fun updateStatus(id: String, status: PluginStatus) {
        _plugins.value = _plugins.value.map {
            if (it.id == id) it.copy(status = status) else it
        }
    }

    companion object {
        private const val PREFS_NAME = "aiai_plugins"
        private const val KEY_ENABLED = "enabled_ids"

        @Volatile
        private var instance: PluginManager? = null

        fun get(context: Context): PluginManager {
            return instance ?: synchronized(this) {
                instance ?: PluginManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
