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

import com.aiai.chat.data.enums.ConversationStatus

/**
 * 会话数据模型
 *
 * 封装一个对话会话的完整信息，包括标题、消息列表、模型配置等。
 *
 * @property id 会话唯一标识
 * @property title 会话标题
 * @property modelName 当前使用的模型名称
 * @property systemPrompt 系统提示词
 * @property status 会话状态
 * @property createdAt 创建时间戳
 * @property updatedAt 最后更新时间戳
 * @property messageCount 消息总数
 * @property lastMessage 最后一条消息预览
 * @property unreadCount 未读消息数
 * @property isPinned 是否置顶
 * @property isFavorite 是否收藏
 * @property temperature 生成温度参数
 * @property maxTokens 最大生成token数
 * @property topP Top-P采样参数
 * @property frequencyPenalty 频率惩罚
 * @property presencePenalty 存在惩罚
 * @property tags 会话标签
 * @property folder 所在文件夹
 * @property coverImage 会话封面图
 */
data class Conversation(
    val id: String,
    val title: String = "新对话",
    val modelName: String = "gpt-3.5-turbo",
    val systemPrompt: String = "",
    val status: ConversationStatus = ConversationStatus.ACTIVE,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val messageCount: Int = 0,
    val lastMessage: String = "",
    val lastMessageTime: Long = System.currentTimeMillis(),
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val temperature: Double = 0.7,
    val maxTokens: Int = 2048,
    val topP: Double = 1.0,
    val frequencyPenalty: Double = 0.0,
    val presencePenalty: Double = 0.0,
    val tags: List<String> = emptyList(),
    val folder: String? = null,
    val coverImage: String? = null
) {
    /**
     * 判断是否为新会话（无消息）
     */
    fun isNew(): Boolean = messageCount == 0

    /**
     * 判断是否正在生成
     */
    fun isGenerating(): Boolean = status == ConversationStatus.GENERATING

    /**
     * 获取格式化的最后消息时间
     */
    fun getFormattedTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - lastMessageTime
        return when {
            diff < 60_000 -> "刚刚"
            diff < 3_600_000 -> "${diff / 60_000}分钟前"
            diff < 86_400_000 -> "${diff / 3_600_000}小时前"
            diff < 604_800_000 -> "${diff / 86_400_000}天前"
            else -> "${lastMessageTime / 86_400_000}天前"
        }
    }

    companion object {
        /**
         * 创建新会话
         */
        fun createNew(
            modelName: String = "gpt-3.5-turbo",
            systemPrompt: String = ""
        ): Conversation {
            return Conversation(
                id = "conv_${System.currentTimeMillis()}",
                modelName = modelName,
                systemPrompt = systemPrompt
            )
        }
    }
}
