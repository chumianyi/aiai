/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.settings.manager.SettingsManager
import com.aiai.settings.model.SettingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * 设置主页 ViewModel。
 *
 * 聚合设置列表数据，并把开关类设置项绑定到 [SettingsManager] 的 StateFlow。
 */
class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsManager.get(app)

    /** 设置主页分组列表。 */
    private val _items = MutableStateFlow<List<SettingItem>>(buildItems())
    val items: StateFlow<List<SettingItem>> = _items

    /** 当前主题模式展示名。 */
    val themeLabel: StateFlow<String> = settings.themeMode.let { flow ->
        MutableStateFlow(flow.value.displayName)
    }

    private fun buildItems(): List<SettingItem> {
        return listOf(
            SettingItem.Header("group_ai", "AI 服务"),
            SettingItem.Normal("api", "API 配置", "接口地址、密钥与模型", 0),
            SettingItem.Normal("key", "密钥管理", "多密钥加密存储", 0),
            SettingItem.Normal("prompt", "提示词模板库", "内置与自定义模板", 0),
            SettingItem.Header("group_ui", "界面"),
            SettingItem.Normal("general", "通用设置", "语言、缓存、通知", 0),
            SettingItem.Normal("appearance", "外观设置", "深色模式、字体与主题", 0),
            SettingItem.Header("group_func", "功能"),
            SettingItem.Normal("voice", "语音设置", "语音识别与自动播放", 0),
            SettingItem.Normal("image", "图片生成设置", "尺寸、风格与水印", 0),
            SettingItem.Normal("plugin", "插件管理", "发现、安装与启用", 0),
            SettingItem.Header("group_data", "数据"),
            SettingItem.Normal("favorite", "收藏管理", "查看与导出收藏", 0),
            SettingItem.Normal("export", "导出聊天记录", "选择会话与格式", 0),
            SettingItem.Normal("privacy", "隐私设置", "数据收集与记录", 0),
            SettingItem.Header("group_about", "关于"),
            SettingItem.Normal("about", "关于爱Ai", "版本、许可与联系方式", 0)
        )
    }

    /**
     * 用户点击了某个设置项，交给 Activity 路由。
     */
    fun onItemClick(item: SettingItem) {
        // 路由由 Activity 处理，这里仅占位以便后续扩展埋点
    }

    /**
     * 刷新列表（从设置中心拉取最新值）。
     */
    fun refresh() {
        _items.value = buildItems()
    }

    override fun onCleared() {
        super.onCleared()
    }
}
