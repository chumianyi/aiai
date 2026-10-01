/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.core.manager

import android.content.Context
import android.util.Log

/**
 * 小组件管理器。
 *
 * 提供桌面小组件管理、更新、配置等功能。
 */
object WidgetManager {

    private const val TAG = "WidgetManager"

    /**
     * 小组件信息数据类。
     *
     * @property widgetId 小组件ID
     * @property widgetName 小组件名称
     * @property description 描述
     */
    data class WidgetInfo(
        val widgetId: Int,
        val widgetName: String,
        val description: String = ""
    )

    private val widgetList = mutableListOf<WidgetInfo>()

    /**
     * 初始化。
     *
     * @param context 上下文
     */
    fun init(context: Context) {
        Log.d(TAG, "WidgetManager initialized")
    }

    /**
     * 注册小组件。
     *
     * @param widgetId 小组件ID
     * @param widgetName 小组件名称
     * @param description 描述
     */
    fun registerWidget(widgetId: Int, widgetName: String, description: String = "") {
        val widget = WidgetInfo(widgetId, widgetName, description)
        widgetList.add(widget)
        Log.d(TAG, "Widget registered: $widgetName (id=$widgetId)")
    }

    /**
     * 注销小组件。
     *
     * @param widgetId 小组件ID
     */
    fun unregisterWidget(widgetId: Int) {
        widgetList.removeIf { it.widgetId == widgetId }
        Log.d(TAG, "Widget unregistered: id=$widgetId")
    }

    /**
     * 获取已注册的小组件列表。
     *
     * @return 小组件列表
     */
    fun getRegisteredWidgets(): List<WidgetInfo> {
        return widgetList.toList()
    }

    /**
     * 更新小组件。
     *
     * @param widgetId 小组件ID
     * @param data 更新数据
     */
    fun updateWidget(widgetId: Int, data: Map<String, String>) {
        Log.d(TAG, "Widget updated: id=$widgetId, data=$data")
    }

    /**
     * 检查是否有小组件。
     *
     * @return 是否有小组件
     */
    fun hasWidgets(): Boolean {
        return widgetList.isNotEmpty()
    }

    /**
     * 获取小组件数量。
     *
     * @return 小组件数量
     */
    fun widgetCount(): Int = widgetList.size
}
