/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import com.aiai.settings.model.PromptCategory
import com.aiai.settings.model.PromptTemplate

/**
 * 内置提示词模板数据（独立文件，便于维护）。
 */
object BuiltinPrompts {

    /** 返回全部内置模板。 */
    fun all(): List<PromptTemplate> {
        val now = System.currentTimeMillis()
        return listOf(
            PromptTemplate("b_cv_1", "简历优化",
                "请优化以下简历段落，使用 STAR 法则，量化成果：",
                PromptCategory.WRITING, listOf("简历"), true, useCount = 210, createdAt = now),
            PromptTemplate("b_cv_2", "求职信",
                "请写一封求职信，岗位：【岗位】，突出沟通能力与学习能力。",
                PromptCategory.WRITING, listOf("求职"), true, useCount = 88, createdAt = now),
            PromptTemplate("b_email_1", "商务邮件",
                "请写一封商务邮件，主题：【主题】，语气正式，结尾礼貌。",
                PromptCategory.BUSINESS, listOf("邮件"), true, useCount = 156, createdAt = now),
            PromptTemplate("b_email_2", "催款邮件",
                "请写一封委婉但明确的催款邮件，金额：【金额】，逾期：【天数】天。",
                PromptCategory.BUSINESS, listOf("邮件"), true, useCount = 45, createdAt = now),
            PromptTemplate("b_code_4", "SQL 优化",
                "请优化以下 SQL 查询，指出全表扫描、索引缺失问题：",
                PromptCategory.CODE, listOf("sql"), true, useCount = 67, createdAt = now),
            PromptTemplate("b_code_5", "正则生成",
                "请生成匹配以下规则的正则表达式：【规则描述】",
                PromptCategory.CODE, listOf("正则"), true, useCount = 92, createdAt = now),
            PromptTemplate("b_code_6", "单元测试",
                "请为以下函数生成 JUnit 测试用例，覆盖正常/边界/异常：",
                PromptCategory.CODE, listOf("test"), true, useCount = 134, createdAt = now),
            PromptTemplate("b_sum_3", "论文摘要",
                "请为以下论文写 200 字中文摘要，包含方法、结果、结论：",
                PromptCategory.SUMMARY, listOf("论文"), true, useCount = 73, createdAt = now),
            PromptTemplate("b_sum_4", "播客转纪要",
                "请把以下播客转写整理为要点列表，每条不超过 20 字：",
                PromptCategory.SUMMARY, listOf("播客"), true, useCount = 38, createdAt = now),
            PromptTemplate("b_trans_3", "法律翻译",
                "请翻译以下合同条款，使用标准法律术语：",
                PromptCategory.TRANSLATE, listOf("法律"), true, useCount = 29, createdAt = now),
            PromptTemplate("b_trans_4", "字幕翻译",
                "请将以下字幕翻译为口语化英文，每条不超过 42 字符：",
                PromptCategory.TRANSLATE, listOf("字幕"), true, useCount = 51, createdAt = now),
            PromptTemplate("b_cr_3", "品牌起名",
                "请为新咖啡品牌起 5 个名字，要求：2~3 字、易记、可注册商标。",
                PromptCategory.CREATIVE, listOf("品牌"), true, useCount = 112, createdAt = now),
            PromptTemplate("b_cr_4", "slogan 生成",
                "请为【产品】写 10 条 slogan，突出【卖点】，字数 8~15。",
                PromptCategory.CREATIVE, listOf("营销"), true, useCount = 87, createdAt = now),
            PromptTemplate("b_learn_3", "知识卡片",
                "请把【概念】制作成 Anki 知识卡片：正面问题，背面答案。",
                PromptCategory.LEARNING, listOf("anki"), true, useCount = 64, createdAt = now),
            PromptTemplate("b_learn_4", "考试复习",
                "请根据以下考纲生成一份复习提纲，按章节组织：",
                PromptCategory.LEARNING, listOf("考试"), true, useCount = 49, createdAt = now),
            PromptTemplate("b_general_3", "决策辅助",
                "我面临以下选择：【A】还是【B】？请列出利弊并给出建议。",
                PromptCategory.GENERAL, listOf("决策"), true, useCount = 143, createdAt = now),
            PromptTemplate("b_general_4", "情绪倾诉",
                "我最近感到【情绪】，因为【事件】。请做一个共情的倾听者并给建议。",
                PromptCategory.GENERAL, listOf("情感"), true, useCount = 76, createdAt = now)
        )
    }
}
