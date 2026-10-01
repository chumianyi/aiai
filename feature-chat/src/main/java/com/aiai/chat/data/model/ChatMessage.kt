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

import com.aiai.chat.data.enums.MessageRole
import com.aiai.chat.data.enums.MessageStatus
import com.aiai.chat.data.enums.MessageType

/**
 * 聊天消息数据模型
 *
 * 封装单条聊天消息的所有信息，包括内容、状态、元数据等。
 * 支持多种消息类型：文本、图片、文件、语音、代码等。
 *
 * @property id 消息唯一标识
 * @property conversationId 所属会话ID
 * @property role 消息发送角色
 * @property type 消息内容类型
 * @property content 消息文本内容
 * @property status 消息发送/接收状态
 * @property modelName 生成该消息使用的模型名称
 * @property createdAt 创建时间戳
 * @property updatedAt 更新时间戳
 * @property attachmentUrl 附件URL（图片/文件/语音）
 * @property attachmentLocalPath 附件本地路径
 * @property fileName 附件文件名
 * @property fileSize 附件文件大小（字节）
 * @property mimeType 附件MIME类型
 * @property duration 语音消息时长（毫秒）
 * @property waveform 语音波形数据
 * @property quotedMessageId 引用的消息ID
 * @property quotedMessagePreview 引用消息预览
 * @property isPinned 是否置顶
 * @property isFavorite 是否收藏
 * @property tokenCount 消耗的token数量
 * @property errorMessage 错误信息（当状态为FAILED时）
 * @property metadata 扩展元数据
 */
data class ChatMessage(
    val id: String,
    val conversationId: String,
    val role: MessageRole,
    val type: MessageType,
    val content: String = "",
    val status: MessageStatus = MessageStatus.SENDING,
    val modelName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val attachmentUrl: String? = null,
    val attachmentLocalPath: String? = null,
    val fileName: String? = null,
    val fileSize: Long = 0,
    val mimeType: String? = null,
    val duration: Long = 0,
    val waveform: List<Int> = emptyList(),
    val quotedMessageId: String? = null,
    val quotedMessagePreview: String? = null,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val tokenCount: Int = 0,
    val errorMessage: String? = null,
    val metadata: Map<String, String> = emptyMap()
) {
    /**
     * 判断是否为用户消息
     */
    fun isUserMessage(): Boolean = role == MessageRole.USER

    /**
     * 判断是否为AI消息
     */
    fun isAiMessage(): Boolean = role == MessageRole.ASSISTANT

    /**
     * 判断是否可重试
     */
    fun canRetry(): Boolean = status == MessageStatus.FAILED && isUserMessage().not()

    /**
     * 判断是否可编辑
     */
    fun canEdit(): Boolean = isUserMessage() && status == MessageStatus.SENT

    /**
     * 判断是否有附件
     */
    fun hasAttachment(): Boolean = !attachmentUrl.isNullOrEmpty() || !attachmentLocalPath.isNullOrEmpty()

    /**
     * 获取附件预览文本（用于会话列表预览）
     */
    fun getPreviewText(): String {
        return when (type) {
            MessageType.IMAGE -> if (isUserMessage()) "[图片]" else "[AI生成图片]"
            MessageType.FILE -> "[文件] $fileName"
            MessageType.VOICE -> "[语音] ${duration / 1000}秒"
            MessageType.CODE -> "[代码]"
            MessageType.SYSTEM -> content
            MessageType.ERROR -> "[错误] $content"
            MessageType.LOADING -> "正在输入..."
            else -> content.take(100)
        }
    }

    companion object {
        /**
         * 创建用户文本消息
         */
        fun createUserText(
            conversationId: String,
            content: String,
            messageId: String = System.currentTimeMillis().toString()
        ): ChatMessage {
            return ChatMessage(
                id = messageId,
                conversationId = conversationId,
                role = MessageRole.USER,
                type = MessageType.TEXT,
                content = content,
                status = MessageStatus.SENDING
            )
        }

        /**
         * 创建AI文本消息（占位）
         */
        fun createAiPlaceholder(
            conversationId: String,
            modelName: String
        ): ChatMessage {
            return ChatMessage(
                id = "ai_${System.currentTimeMillis()}",
                conversationId = conversationId,
                role = MessageRole.ASSISTANT,
                type = MessageType.TEXT,
                content = "",
                status = MessageStatus.STREAMING,
                modelName = modelName
            )
        }

        /**
         * 创建系统消息
         */
        fun createSystem(
            conversationId: String,
            content: String
        ): ChatMessage {
            return ChatMessage(
                id = "sys_${System.currentTimeMillis()}",
                conversationId = conversationId,
                role = MessageRole.SYSTEM,
                type = MessageType.SYSTEM,
                content = content,
                status = MessageStatus.SENT
            )
        }
    }
}
