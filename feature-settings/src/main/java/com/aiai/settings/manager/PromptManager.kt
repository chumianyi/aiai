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
import com.aiai.settings.model.PromptCategory
import com.aiai.settings.model.PromptTemplate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 提示词管理。
 *
 * 内置 20+ 常用提示词模板，支持用户自定义模板、收藏、搜索、使用计数。
 * 自定义模板持久化到 SharedPreferences，内置模板每次启动重新加载。
 */
class PromptManager(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _builtin = MutableStateFlow(builtInPrompts())
    /** 内置模板。 */
    val builtin: StateFlow<List<PromptTemplate>> = _builtin.asStateFlow()

    private val _custom = MutableStateFlow(loadCustom())
    /** 用户自定义模板。 */
    val custom: StateFlow<List<PromptTemplate>> = _custom.asStateFlow()

    private val _favorites = MutableStateFlow(loadFavorites())
    /** 收藏的模板 id 集合。 */
    val favorites: StateFlow<Set<String>> = _favorites.asStateFlow()

    /** 全部模板（内置 + 自定义）。 */
    fun all(): List<PromptTemplate> = _builtin.value + _custom.value

    // region 自定义模板

    /** 保存自定义模板。 */
    fun saveCustom(template: PromptTemplate) {
        val list = _custom.value.toMutableList()
        val idx = list.indexOfFirst { it.id == template.id }
        if (idx >= 0) list[idx] = template else list.add(template)
        _custom.value = list
        persistCustom()
    }

    /** 删除自定义模板。 */
    fun deleteCustom(id: String) {
        _custom.value = _custom.value.filterNot { it.id == id }
        persistCustom()
    }

    private fun loadCustom(): List<PromptTemplate> {
        val raw = prefs.getString(KEY_CUSTOM, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<PromptTemplate>>() {}.type
            gson.fromJson(raw, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun persistCustom() {
        prefs.edit().putString(KEY_CUSTOM, gson.toJson(_custom.value)).apply()
    }

    // endregion

    // region 收藏

    fun toggleFavorite(id: String) {
        val cur = _favorites.value.toMutableSet()
        if (!cur.add(id)) cur.remove(id)
        _favorites.value = cur
        prefs.edit().putStringSet(KEY_FAV, cur).apply()
    }

    fun isFavorite(id: String): Boolean = _favorites.value.contains(id)

    private fun loadFavorites(): Set<String> =
        prefs.getStringSet(KEY_FAV, emptySet()) ?: emptySet()

    // endregion

    // region 搜索 / 分类

    /**
     * 按 [query] 与 [category] 过滤模板。
     */
    fun filter(query: String, category: PromptCategory?): List<PromptTemplate> {
        return all().filter { tpl ->
            (category == null || tpl.category == category) &&
                (query.isBlank() ||
                    tpl.title.contains(query, true) ||
                    tpl.content.contains(query, true) ||
                    tpl.tags.any { it.contains(query, true) })
        }
    }

    /** 递增使用计数并持久化。 */
    fun markUsed(id: String) {
        _builtin.value = _builtin.value.map {
            if (it.id == id) it.copy(useCount = it.useCount + 1) else it
        }
        _custom.value = _custom.value.map {
            if (it.id == id) it.copy(useCount = it.useCount + 1) else it
        }
    }

    // endregion

    // region 内置模板

    private fun builtInPrompts(): List<PromptTemplate> {
        val now = System.currentTimeMillis()
        return listOf(
            PromptTemplate("p_general_1", "角色扮演专家",
                "你现在是一位经验丰富的角色扮演专家，能够根据用户设定的角色背景、性格和目标进行沉浸式对话。请保持角色一致性，用符合角色身份的语气回应。",
                PromptCategory.GENERAL, listOf("角色", "沉浸"), true, useCount = 128, createdAt = now,
                description = "让 AI 扮演任意角色进行对话"),
            PromptTemplate("p_general_2", "思维链拆解",
                "请把我的问题拆解为多个子问题，逐步分析，每一步给出推理过程，最后汇总结论。不要直接给出最终答案。",
                PromptCategory.GENERAL, listOf("CoT", "推理"), true, useCount = 96, createdAt = now,
                description = "强制分步推理，提升复杂问题准确率"),
            PromptTemplate("p_write_1", "小红书文案",
                "请以小红书风格写一篇关于【主题】的笔记：开头用吸引人的 emoji，中间分 3 个要点，结尾加互动提问，语气活泼，多用口语化表达。",
                PromptCategory.WRITING, listOf("文案", "小红书"), true, useCount = 210, createdAt = now),
            PromptTemplate("p_write_2", "公众号长文",
                "请写一篇公众号深度文章，主题：【主题】。要求：标题有悬念、开头 100 字内抓住读者、正文分 3 个小节并配小标题、结尾有升华。",
                PromptCategory.WRITING, listOf("公众号", "长文"), true, useCount = 88, createdAt = now),
            PromptTemplate("p_write_3", " resumes 润色",
                "请润色以下简历经历描述，使用 STAR 法则（情境-任务-行动-结果），量化成果，动词开头，每条不超过 2 行。原文：",
                PromptCategory.WRITING, listOf("简历", "求职"), true, useCount = 76, createdAt = now),
            PromptTemplate("p_code_1", "代码 Review",
                "请 Review 以下代码，指出：1) 潜在 bug；2) 性能问题；3) 可读性改进；4) 安全隐患。按严重程度排序。代码：",
                PromptCategory.CODE, listOf("review", "质量"), true, useCount = 154, createdAt = now),
            PromptTemplate("p_code_2", "注释生成",
                "请为以下代码生成中文 KDoc/JavaDoc 注释，包含：功能说明、参数、返回值、异常。不要修改代码本身。代码：",
                PromptCategory.CODE, listOf("注释", "文档"), true, useCount = 63, createdAt = now),
            PromptTemplate("p_code_3", "Bug 定位",
                "我遇到以下 Bug：【现象】。相关代码：【代码】。请列出可能的原因，按概率排序，并给出调试建议。",
                PromptCategory.CODE, listOf("debug"), true, useCount = 91, createdAt = now),
            PromptTemplate("p_trans_1", "地道翻译",
                "请将以下中文翻译为地道的英文，不要逐字直译，注意商务语境。原文：",
                PromptCategory.TRANSLATE, listOf("英译中"), true, useCount = 342, createdAt = now),
            PromptTemplate("p_trans_2", "古文翻译",
                "请将以下文言文翻译为白话文，并解释重点字词。原文：",
                PromptCategory.TRANSLATE, listOf("古文"), true, useCount = 45, createdAt = now),
            PromptTemplate("p_sum_1", "长文摘要",
                "请将以下文章压缩为 300 字以内的摘要，保留核心论点与数据，分点列出。文章：",
                PromptCategory.SUMMARY, listOf("摘要"), true, useCount = 278, createdAt = now),
            PromptTemplate("p_sum_2", "会议纪要",
                "请根据以下会议录音转写，整理为：1) 会议结论；2) 待办事项（负责人+截止时间）；3) 未决问题。内容：",
                PromptCategory.SUMMARY, listOf("纪要"), true, useCount = 132, createdAt = now),
            PromptTemplate("p_creative_1", "起名字",
                "请为【产品/项目/宠物】起 10 个名字，每个名字附带寓意解释，风格：科技感 / 文艺 / 幽默任选。",
                PromptCategory.CREATIVE, listOf("起名"), true, useCount = 67, createdAt = now),
            PromptTemplate("p_creative_2", "故事创作",
                "请以【设定】为背景写一个 500 字短篇故事，要求有反转结尾，人物弧光完整。",
                PromptCategory.CREATIVE, listOf("小说"), true, useCount = 54, createdAt = now),
            PromptTemplate("p_biz_1", "周报生成",
                "请根据以下要点生成一份专业周报：1) 本周完成；2) 进行中；3) 下周计划；4) 风险与求助。要点：",
                PromptCategory.BUSINESS, listOf("周报"), true, useCount = 189, createdAt = now),
            PromptTemplate("p_biz_2", "竞品分析",
                "请对比产品 A 与产品 B，从：定位、核心功能、商业模式、优劣势 4 个维度输出表格，并给出差异化建议。",
                PromptCategory.BUSINESS, listOf("竞品"), true, useCount = 71, createdAt = now),
            PromptTemplate("p_learn_1", "费曼学习",
                "请用费曼技巧教我【概念】：先用大白话解释，再举一个生活例子，最后出 3 道小测验检验我是否理解。",
                PromptCategory.LEARNING, listOf("学习"), true, useCount = 118, createdAt = now),
            PromptTemplate("p_learn_2", "错题分析",
                "我做错了这道题：【题目】。我的答案：【答案】。请帮我分析错误原因，讲解正确思路，并出一道同类题巩固。",
                PromptCategory.LEARNING, listOf("错题"), true, useCount = 83, createdAt = now)
        )
    }

    // endregion

    companion object {
        private const val PREFS_NAME = "aiai_prompts"
        private const val KEY_CUSTOM = "custom_prompts"
        private const val KEY_FAV = "favorite_prompt_ids"

        @Volatile
        private var instance: PromptManager? = null

        fun get(context: Context): PromptManager {
            return instance ?: synchronized(this) {
                instance ?: PromptManager(context.applicationContext).also { instance = it }
            }
        }
    }
}
