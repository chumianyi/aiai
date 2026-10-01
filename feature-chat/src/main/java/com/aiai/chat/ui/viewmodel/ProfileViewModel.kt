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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 个人中心ViewModel
 *
 * 管理个人中心页面：用户信息、设置入口、使用统计、关于。
 */
class ProfileViewModel : ViewModel() {

    private val _userInfo = MutableStateFlow(UserInfo())
    val userInfo: StateFlow<UserInfo> = _userInfo.asStateFlow()

    private val _usageStats = MutableStateFlow(UsageStats())
    val usageStats: StateFlow<UsageStats> = _usageStats.asStateFlow()

    private val _settingsItems = MutableStateFlow<List<SettingsItem>>(emptyList())
    val settingsItems: StateFlow<List<SettingsItem>> = _settingsItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadProfileData()
    }

    /**
     * 加载个人中心数据
     */
    fun loadProfileData() {
        viewModelScope.launch {
            _isLoading.value = true

            _userInfo.value = UserInfo(
                nickname = "爱Ai用户",
                avatarUrl = null,
                email = "user@aiai.app",
                membershipType = "免费版"
            )

            _usageStats.value = UsageStats(
                totalChats = 128,
                totalMessages = 3562,
                totalTokens = 452000,
                favoriteModel = "GPT-4o mini",
                storageUsed = 128.5
            )

            _settingsItems.value = listOf(
                SettingsItem("api_config", "API配置", "设置API地址和密钥"),
                SettingsItem("model_manage", "模型管理", "添加和管理模型"),
                SettingsItem("privacy", "隐私设置", "数据与隐私"),
                SettingsItem("notification", "通知设置", "消息通知"),
                SettingsItem("about", "关于爱Ai", "版本信息", "v1.0.0")
            )

            _isLoading.value = false
        }
    }

    /**
     * 用户信息数据类
     */
    data class UserInfo(
        val nickname: String = "",
        val avatarUrl: String? = null,
        val email: String = "",
        val membershipType: String = "",
        val memberExpireDate: String = ""
    )

    /**
     * 使用统计数据类
     */
    data class UsageStats(
        val totalChats: Int = 0,
        val totalMessages: Int = 0,
        val totalTokens: Int = 0,
        val favoriteModel: String = "",
        val storageUsed: Double = 0.0
    )

    /**
     * 设置项数据类
     */
    data class SettingsItem(
        val id: String,
        val title: String,
        val subtitle: String,
        val badge: String? = null
    )
}
