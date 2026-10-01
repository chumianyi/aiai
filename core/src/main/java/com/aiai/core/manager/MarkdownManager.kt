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

import android.util.Log

/**
 * Markdown 管理器。
 *
 * 提供 Markdown 渲染配置、扩展支持等功能。
 */
object MarkdownManager {

    private const val TAG = "MarkdownManager"

    /**
     * Markdown 配置数据类。
     *
     * @property enableCodeHighlight 是否启用代码高亮
     * @property enableTable 是否启用表格
     * @property enableLatex 是否启用 LaTeX
     * @property enableTaskList 是否启用任务列表
     * @property enableStrikethrough 是否启用删除线
     * @property underline 是否启用下划线
     * @property autoLink 是否自动链接
     */
    data class MarkdownConfig(
        var enableCodeHighlight: Boolean = true,
        var enableTable: Boolean = true,
        var enableLatex: Boolean = false,
        var enableTaskList: Boolean = true,
        var enableStrikethrough: Boolean = true,
        var underline: Boolean = false,
        var autoLink: Boolean = true
    )

    private var config = MarkdownConfig()

    /**
     * 初始化。
     */
    fun init() {
        Log.d(TAG, "MarkdownManager initialized")
    }

    /**
     * 获取配置。
     *
     * @return Markdown 配置
     */
    fun getConfig(): MarkdownConfig = config.copy()

    /**
     * 更新配置。
     *
     * @param newConfig 新配置
     */
    fun updateConfig(newConfig: MarkdownConfig) {
        config = newConfig
        Log.d(TAG, "Markdown config updated")
    }

    /**
     * 是否启用代码高亮。
     *
     * @return 是否启用
     */
    fun isCodeHighlightEnabled(): Boolean = config.enableCodeHighlight

    /**
     * 设置代码高亮。
     *
     * @param enabled 是否启用
     */
    fun setCodeHighlightEnabled(enabled: Boolean) {
        config.enableCodeHighlight = enabled
        Log.d(TAG, "Code highlight: $enabled")
    }

    /**
     * 是否启用表格。
     *
     * @return 是否启用
     */
    fun isTableEnabled(): Boolean = config.enableTable

    /**
     * 设置表格。
     *
     * @param enabled 是否启用
     */
    fun setTableEnabled(enabled: Boolean) {
        config.enableTable = enabled
        Log.d(TAG, "Table: $enabled")
    }

    /**
     * 是否启用 LaTeX。
     *
     * @return 是否启用
     */
    fun isLatexEnabled(): Boolean = config.enableLatex

    /**
     * 设置 LaTeX。
     *
     * @param enabled 是否启用
     */
    fun setLatexEnabled(enabled: Boolean) {
        config.enableLatex = enabled
        Log.d(TAG, "LaTeX: $enabled")
    }

    /**
     * 重置为默认配置。
     */
    fun resetToDefault() {
        config = MarkdownConfig()
        Log.d(TAG, "Markdown config reset to default")
    }
}
