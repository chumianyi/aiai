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
 * 提示词模板管理器。
 *
 * 提供模板管理、模板分类、模板搜索等功能。
 */
object TemplateManager {

    private const val TAG = "TemplateManager"

    /**
     * 模板分类枚举。
     */
    enum class TemplateCategory(val displayName: String) {
        /** 写作 */
        WRITING("写作"),
        /** 编程 */
        PROGRAMMING("编程"),
        /** 翻译 */
        TRANSLATION("翻译"),
        /** 学习 */
        STUDY("学习"),
        /** 生活 */
        LIFE("生活"),
        /** 工作 */
        WORK("工作"),
        /** 其他 */
        OTHER("其他")
    }

    /**
     * 模板数据类。
     *
     * @property id 模板ID
     * @property title 模板标题
     * @property content 模板内容
     * @property category 分类
     * @property icon 图标
     * @property isBuiltin 是否内置
     * @property usageCount 使用次数
     */
    data class PromptTemplate(
        val id: String,
        val title: String,
        val content: String,
        val category: TemplateCategory,
        val icon: String = "",
        val isBuiltin: Boolean = true,
        var usageCount: Int = 0
    )

    private val templates = mutableListOf<PromptTemplate>()

    init {
        // 初始化内置模板
        addBuiltinTemplates()
    }

    private fun addBuiltinTemplates() {
        templates.add(PromptTemplate(
            id = "writing_essay",
            title = "写一篇文章",
            content = "请帮我写一篇关于{主题}的文章，要求{要求}",
            category = TemplateCategory.WRITING,
            icon = "📝"
        ))
        templates.add(PromptTemplate(
            id = "writing_poem",
            title = "写一首诗",
            content = "请以{主题}为题，写一首{风格}风格的诗",
            category = TemplateCategory.WRITING,
            icon = "📜"
        ))
        templates.add(PromptTemplate(
            id = "code_explain",
            title = "解释代码",
            content = "请解释以下代码的功能和原理：\n```\n{代码}\n```",
            category = TemplateCategory.PROGRAMMING,
            icon = "💻"
        ))
        templates.add(PromptTemplate(
            id = "code_debug",
            title = "调试代码",
            content = "请帮我调试以下代码，找出问题并修复：\n```\n{代码}\n```\n错误信息：{错误信息}",
            category = TemplateCategory.PROGRAMMING,
            icon = "🔧"
        ))
        templates.add(PromptTemplate(
            id = "translate",
            title = "翻译",
            content = "请将以下内容翻译成{目标语言}：\n{内容}",
            category = TemplateCategory.TRANSLATION,
            icon = "🌐"
        ))
        templates.add(PromptTemplate(
            id = "study_summary",
            title = "学习总结",
            content = "请帮我总结以下学习内容的重点：\n{内容}",
            category = TemplateCategory.STUDY,
            icon = "📚"
        ))
        templates.add(PromptTemplate(
            id = "life_recipe",
            title = "菜谱推荐",
            content = "请推荐一道{食材}的家常菜谱",
            category = TemplateCategory.LIFE,
            icon = "🍳"
        ))
        templates.add(PromptTemplate(
            id = "work_email",
            title = "写邮件",
            content = "请帮我写一封关于{主题}的工作邮件，收件人是{收件人}",
            category = TemplateCategory.WORK,
            icon = "📧"
        ))
    }

    /**
     * 获取所有模板。
     *
     * @return 模板列表
     */
    fun getAllTemplates(): List<PromptTemplate> {
        return templates.toList()
    }

    /**
     * 按分类获取模板。
     *
     * @param category 分类
     * @return 模板列表
     */
    fun getTemplatesByCategory(category: TemplateCategory): List<PromptTemplate> {
        return templates.filter { it.category == category }
    }

    /**
     * 搜索模板。
     *
     * @param keyword 关键词
     * @return 模板列表
     */
    fun searchTemplates(keyword: String): List<PromptTemplate> {
        if (keyword.isBlank()) return getAllTemplates()
        return templates.filter {
            it.title.contains(keyword, ignoreCase = true) ||
                    it.content.contains(keyword, ignoreCase = true)
        }
    }

    /**
     * 添加自定义模板。
     *
     * @param template 模板
     */
    fun addCustomTemplate(template: PromptTemplate) {
        templates.add(template.copy(isBuiltin = false))
        Log.d(TAG, "Custom template added: ${template.title}")
    }

    /**
     * 删除模板。
     *
     * @param templateId 模板ID
     * @return 是否成功
     */
    fun deleteTemplate(templateId: String): Boolean {
        val removed = templates.removeIf { it.id == templateId && !it.isBuiltin }
        Log.d(TAG, "Template deleted: $templateId, success=$removed")
        return removed
    }

    /**
     * 使用模板。
     *
     * @param templateId 模板ID
     */
    fun useTemplate(templateId: String) {
        templates.find { it.id == templateId }?.let {
            it.usageCount++
            Log.d(TAG, "Template used: ${it.title}, count=${it.usageCount}")
        }
    }

    /**
     * 获取热门模板。
     *
     * @param limit 数量限制
     * @return 热门模板列表
     */
    fun getPopularTemplates(limit: Int = 5): List<PromptTemplate> {
        return templates.sortedByDescending { it.usageCount }.take(limit)
    }

    /**
     * 模板数量。
     */
    fun templateCount(): Int = templates.size
}
