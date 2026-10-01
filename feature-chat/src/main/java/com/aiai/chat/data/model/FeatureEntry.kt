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
 * 聊天功能入口模型
 */
data class FeatureEntry(
    val id: String,
    val title: String,
    val description: String,
    val iconRes: Int,
    val gradientStart: Int,
    val gradientEnd: Int,
    val isNew: Boolean = false,
    val isBeta: Boolean = false,
    val sortOrder: Int = 0
) {
    companion object {
        /**
         * 默认功能列表
         */
        fun defaultFeatures(): List<FeatureEntry> {
            return listOf(
                FeatureEntry("chat", "AI对话", "与智能助手畅聊", 0, 0xFF667EEA.toInt(), 0xFF764BA2.toInt(), sortOrder = 1),
                FeatureEntry("image", "AI画图", "文字生成创意图片", 0, 0xFFF093FB.toInt(), 0xFFF5576C.toInt(), sortOrder = 2),
                FeatureEntry("code", "写代码", "编程辅助与审查", 0, 0xFF4FACFE.toInt(), 0xFF00F2FE.toInt(), sortOrder = 3),
                FeatureEntry("write", "写作", "文案/邮件/论文", 0, 0xFF43E97B.toInt(), 0xFF38F9D7.toInt(), sortOrder = 4),
                FeatureEntry("translate", "翻译", "多语言互译", 0, 0xFFFA709A.toInt(), 0xFFFEE140.toInt(), sortOrder = 5),
                FeatureEntry("summary", "总结", "文章/视频总结", 0, 0xFFA18CD1.toInt(), 0xFFFBC2EB.toInt(), sortOrder = 6),
                FeatureEntry("learn", "学习", "知识点讲解", 0, 0xFFFCCB90.toInt(), 0xFFD57EEB.toInt(), sortOrder = 7),
                FeatureEntry("analysis", "数据分析", "表格数据解读", 0, 0xFFE0C3FC.toInt(), 0xFF8EC5FC.toInt(), isBeta = true, sortOrder = 8)
            )
        }
    }
}
