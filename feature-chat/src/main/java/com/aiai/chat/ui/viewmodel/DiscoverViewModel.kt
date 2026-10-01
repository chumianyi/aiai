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
package com.aiai.chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.chat.data.model.PromptSuggestion
import com.aiai.chat.manager.PromptSuggestionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 发现页ViewModel
 *
 * 管理发现页内容：功能入口网格、提示词推荐、插件推荐。
 */
class DiscoverViewModel : ViewModel() {

    private val promptManager = PromptSuggestionManager()

    private val _featureGrid = MutableStateFlow<List<FeatureItem>>(emptyList())
    val featureGrid: StateFlow<List<FeatureItem>> = _featureGrid.asStateFlow()

    private val _recommendedPrompts = MutableStateFlow<List<PromptSuggestion>>(emptyList())
    val recommendedPrompts: StateFlow<List<PromptSuggestion>> = _recommendedPrompts.asStateFlow()

    private val _plugins = MutableStateFlow<List<PluginItem>>(emptyList())
    val plugins: StateFlow<List<PluginItem>> = _plugins.asStateFlow()

    private val _selectedCategory = MutableStateFlow("全部")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    init {
        loadDiscoverData()
    }

    /**
     * 加载发现页数据
     */
    fun loadDiscoverData() {
        viewModelScope.launch {
            // 功能入口网格
            _featureGrid.value = listOf(
                FeatureItem("chat", "AI对话", "与智能助手聊天"),
                FeatureItem("image", "AI画图", "文字生成图片"),
                FeatureItem("code", "编程助手", "代码生成与审查"),
                FeatureItem("write", "写作助手", "邮件/文案/论文"),
                FeatureItem("translate", "翻译", "多语言互译"),
                FeatureItem("summary", "总结", "文章/视频总结"),
                FeatureItem("learn", "学习辅导", "知识点讲解"),
                FeatureItem("more", "更多", "查看全部功能")
            )

            // 推荐提示词
            _recommendedPrompts.value = PromptSuggestion.defaultSuggestions()

            // 分类
            _categories.value = listOf("全部") + promptManager.run { getCategories() }

            // 插件推荐
            _plugins.value = listOf(
                PluginItem("web_search", "联网搜索", "获取实时网络信息", true),
                PluginItem("code_interpreter", "代码解释器", "运行代码并生成图表", true),
                PluginItem("dalle", "DALL-E", "高质量图片生成", false),
                PluginItem("google", "Google搜索", "搜索网页结果", false)
            )
        }
    }

    /**
     * 选择分类
     */
    fun selectCategory(category: String) {
        _selectedCategory.value = category
        _recommendedPrompts.value = if (category == "全部") {
            PromptSuggestion.defaultSuggestions()
        } else {
            PromptSuggestion.defaultSuggestions().filter { it.category == category }
        }
    }

    /**
     * 切换插件启用状态
     */
    fun togglePlugin(pluginId: String) {
        _plugins.value = _plugins.value.map { plugin ->
            if (plugin.id == pluginId) plugin.copy(isEnabled = !plugin.isEnabled) else plugin
        }
    }

    /**
     * 功能入口数据类
     */
    data class FeatureItem(
        val id: String,
        val title: String,
        val description: String
    )

    /**
     * 插件数据类
     */
    data class PluginItem(
        val id: String,
        val name: String,
        val description: String,
        val isEnabled: Boolean
    )
}

/**
 * 扩展函数获取分类
 */
private fun PromptSuggestionManager.getCategories(): List<String> {
    return PromptSuggestion.defaultSuggestions().map { it.category }.distinct()
}
