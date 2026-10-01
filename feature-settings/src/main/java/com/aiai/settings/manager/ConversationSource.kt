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

import android.content.Context
import com.aiai.settings.model.ExportConversation

/**
 * 会话数据源：模拟从 data 模块读取会话列表。
 */
class ConversationSource(context: Context) {

    /** 返回所有会话（实际项目从 Room 数据库读取）。 */
    fun loadConversations(): List<ExportConversation> {
        val now = System.currentTimeMillis()
        return listOf(
            ExportConversation("c1", "翻译助手会话", 42, now - 1000),
            ExportConversation("c2", "代码 Review 记录", 128, now - 5000),
            ExportConversation("c3", "周报生成讨论", 15, now - 9000),
            ExportConversation("c4", "读书笔记：深入理解计算机系统", 67, now - 20000),
            ExportConversation("c5", "菜谱研发：番茄牛腩", 23, now - 30000),
            ExportConversation("c6", "面试模拟：Android 高级岗", 89, now - 40000),
            ExportConversation("c7", "旅行规划：云南 7 日游", 56, now - 50000),
            ExportConversation("c8", "宠物起名", 8, now - 60000)
        )
    }
}
