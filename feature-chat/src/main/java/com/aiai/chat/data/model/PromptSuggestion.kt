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
package com.aiai.chat.data.model

/**
 * 提示词建议数据模型
 *
 * 用于首页和发现页推荐的提示词模板。
 *
 * @property id 建议唯一标识
 * @property title 提示词标题
 * @property content 提示词内容/模板
 * @property category 分类
 * @property iconRes 图标资源
 * @property tags 标签
 * @property useCount 使用次数
 * @property isHot 是否热门
 * @property isNew 是否新上线
 * @property sortOrder 排序权重
 */
data class PromptSuggestion(
    val id: String,
    val title: String,
    val content: String,
    val category: String = "通用",
    val iconRes: Int = 0,
    val tags: List<String> = emptyList(),
    val useCount: Int = 0,
    val isHot: Boolean = false,
    val isNew: Boolean = false,
    val sortOrder: Int = 0
) {
    companion object {
        /**
         * 预设推荐提示词
         */
        fun defaultSuggestions(): List<PromptSuggestion> {
            return listOf(
                PromptSuggestion(
                    id = "p1",
                    title = "写一封求职信",
                    content = "请帮我写一封应聘[职位名称]的求职信，我有[X]年[行业]经验，核心优势是[列出3个优势]。",
                    category = "写作",
                    tags = listOf("职场", "邮件"),
                    useCount = 12580,
                    isHot = true,
                    sortOrder = 1
                ),
                PromptSuggestion(
                    id = "p2",
                    title = "学习新概念",
                    content = "请用通俗易懂的方式向我解释[概念名称]，包括定义、原理、生活中的例子和常见应用场景。",
                    category = "学习",
                    tags = listOf("教育", "科普"),
                    useCount = 9876,
                    isHot = true,
                    sortOrder = 2
                ),
                PromptSuggestion(
                    id = "p3",
                    title = "代码审查",
                    content = "请帮我审查以下代码，指出潜在的bug、性能问题和改进建议：\n```\n[粘贴代码]\n```",
                    category = "编程",
                    tags = listOf("开发", "效率"),
                    useCount = 8543,
                    sortOrder = 3
                ),
                PromptSuggestion(
                    id = "p4",
                    title = "旅行规划",
                    content = "请帮我规划一次去[目的地]的[天数]天旅行，预算[金额]，偏好[美食/自然风光/历史文化]，需要详细的每日行程安排。",
                    category = "生活",
                    tags = listOf("旅行", "攻略"),
                    useCount = 7654,
                    sortOrder = 4
                ),
                PromptSuggestion(
                    id = "p5",
                    title = "健身计划",
                    content = "我今年[年龄]岁，体重[X]kg，目标是[减脂/增肌/塑形]，请帮我制定一份每周[X]次的健身计划，包含具体动作和组数。",
                    category = "健康",
                    tags = listOf("运动", "健身"),
                    useCount = 6543,
                    sortOrder = 5
                ),
                PromptSuggestion(
                    id = "p6",
                    title = "翻译润色",
                    content = "请帮我将以下[语言]文本翻译成[目标语言]，并保持原文的语气和风格：\n[粘贴文本]",
                    category = "语言",
                    tags = listOf("翻译", "学习"),
                    useCount = 5432,
                    isNew = true,
                    sortOrder = 6
                ),
                PromptSuggestion(
                    id = "p7",
                    title = "产品创意",
                    content = "我想做一个面向[目标用户]的[产品类型]，核心功能是[主要功能]，请帮我头脑风暴10个创新的产品亮点和差异化卖点。",
                    category = "创业",
                    tags = listOf("产品", "创意"),
                    useCount = 4321,
                    sortOrder = 7
                ),
                PromptSuggestion(
                    id = "p8",
                    title = "读书笔记",
                    content = "请帮我整理《[书名]》的读书笔记，包括核心观点、重要章节摘要、金句摘录和我的行动清单。",
                    category = "阅读",
                    tags = listOf("读书", "总结"),
                    useCount = 3210,
                    isNew = true,
                    sortOrder = 8
                )
            )
        }
    }
}
