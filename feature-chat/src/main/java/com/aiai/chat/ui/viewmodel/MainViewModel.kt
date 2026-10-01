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
 * 主界面ViewModel
 *
 * 管理主界面的底部导航状态、Fragment切换、权限申请等。
 */
class MainViewModel : ViewModel() {

    /**
     * 底部导航Tab枚举
     */
    enum class TabItem(val position: Int) {
        CHAT(0),
        DISCOVER(1),
        PROFILE(2);

        companion object {
            fun fromPosition(position: Int): TabItem {
                return entries.find { it.position == position } ?: CHAT
            }
        }
    }

    private val _currentTab = MutableStateFlow(TabItem.CHAT)
    val currentTab: StateFlow<TabItem> = _currentTab.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()

    private val _permissionGranted = MutableStateFlow(false)
    val permissionGranted: StateFlow<Boolean> = _permissionGranted.asStateFlow()

    /**
     * 切换Tab
     */
    fun switchTab(tab: TabItem) {
        _currentTab.value = tab
    }

    /**
     * 初始化完成
     */
    fun onInitialized() {
        viewModelScope.launch {
            _isInitialized.value = true
        }
    }

    /**
     * 权限申请结果
     */
    fun onPermissionResult(granted: Boolean) {
        _permissionGranted.value = granted
    }
}
