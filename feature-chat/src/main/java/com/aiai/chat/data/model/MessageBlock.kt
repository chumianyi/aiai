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

import com.aiai.chat.data.enums.MessageType

/**
 * 消息内容块数据模型
 *
 * 表示一条消息中的单个内容块，支持混合内容类型。
 * 例如一条AI回复可以同时包含文本块、代码块和表格块。
 */
data class MessageBlock(
    val type: MessageType,
    val text: String = "",
    val language: String = "",
    val imageUrl: String? = null,
    val fileName: String? = null,
    val fileSize: Long = 0,
    val tableData: List<List<String>> = emptyList(),
    val order: Int = 0
) {
    companion object {
        /**
         * 创建文本块
         */
        fun text(content: String, order: Int = 0): MessageBlock {
            return MessageBlock(type = MessageType.TEXT, text = content, order = order)
        }

        /**
         * 创建代码块
         */
        fun code(code: String, language: String = "kotlin", order: Int = 0): MessageBlock {
            return MessageBlock(type = MessageType.CODE, text = code, language = language, order = order)
        }

        /**
         * 创建图片块
         */
        fun image(url: String, order: Int = 0): MessageBlock {
            return MessageBlock(type = MessageType.IMAGE, imageUrl = url, order = order)
        }

        /**
         * 创建表格块
         */
        fun table(rows: List<List<String>>, order: Int = 0): MessageBlock {
            return MessageBlock(type = MessageType.TABLE, tableData = rows, order = order)
        }
    }
}
