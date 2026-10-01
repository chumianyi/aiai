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
 * 消息长按菜单管理器
 *
 * 管理消息长按弹出的操作菜单项。
 */
class LongPressMenuManager(private val context: Context) {

    data class MenuItem(
        val id: String,
        val title: String,
        val iconRes: Int = 0,
        val visible: Boolean = true,
        val enabled: Boolean = true
    )

    private val _defaultMenuItems = MutableStateFlow<List<MenuItem>>(emptyList())
    val defaultMenuItems: StateFlow<List<MenuItem>> = _defaultMenuItems.asStateFlow()

    init {
        loadDefaultMenuItems()
    }

    private fun loadDefaultMenuItems() {
        _defaultMenuItems.value = listOf(
            MenuItem("copy", "复制", visible = true),
            MenuItem("share", "分享", visible = true),
            MenuItem("forward", "转发", visible = true),
            MenuItem("favorite", "收藏", visible = true),
            MenuItem("quote", "引用", visible = true),
            MenuItem("delete", "删除", visible = true),
            MenuItem("regenerate", "重新生成", visible = true),
            MenuItem("translate", "翻译", visible = true)
        )
    }

    fun getMenuItemsForMessage(isUser: Boolean, hasError: Boolean): List<MenuItem> {
        return _defaultMenuItems.value.map { item ->
            when (item.id) {
                "regenerate" -> item.copy(visible = !isUser && !hasError)
                "delete" -> item
                else -> item
            }
        }.filter { it.visible }
    }

    fun addCustomMenuItem(item: MenuItem) {
        _defaultMenuItems.value = _defaultMenuItems.value + item
    }

    fun removeMenuItem(id: String) {
        _defaultMenuItems.value = _defaultMenuItems.value.filterNot { it.id == id }
    }
}
