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
package com.aiai.chat.factory

import com.aiai.chat.data.enums.MessageRole
import com.aiai.chat.data.enums.MessageStatus
import com.aiai.chat.data.enums.MessageType
import com.aiai.chat.data.model.ChatMessage

/**
 * 消息工厂
 *
 * 负责创建各种类型的聊天消息对象，统一消息创建逻辑。
 */
object MessageFactory {

    /**
     * 创建文本消息
     */
    fun createTextMessage(
        conversationId: String,
        content: String,
        role: MessageRole
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = role,
            type = MessageType.TEXT,
            content = content,
            status = if (role == MessageRole.USER) MessageStatus.SENDING else MessageStatus.STREAMING
        )
    }

    /**
     * 创建图片消息
     */
    fun createImageMessage(
        conversationId: String,
        imageUrl: String,
        role: MessageRole = MessageRole.USER
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = role,
            type = MessageType.IMAGE,
            attachmentUrl = imageUrl,
            status = MessageStatus.SENDING
        )
    }

    /**
     * 创建文件消息
     */
    fun createFileMessage(
        conversationId: String,
        filePath: String,
        fileName: String,
        fileSize: Long,
        mimeType: String
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = MessageRole.USER,
            type = MessageType.FILE,
            attachmentLocalPath = filePath,
            fileName = fileName,
            fileSize = fileSize,
            mimeType = mimeType,
            status = MessageStatus.SENDING
        )
    }

    /**
     * 创建语音消息
     */
    fun createVoiceMessage(
        conversationId: String,
        filePath: String,
        duration: Long,
        waveform: List<Int>
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = MessageRole.USER,
            type = MessageType.VOICE,
            attachmentLocalPath = filePath,
            duration = duration,
            waveform = waveform,
            status = MessageStatus.SENDING
        )
    }

    /**
     * 创建系统消息
     */
    fun createSystemMessage(
        conversationId: String,
        content: String
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = MessageRole.SYSTEM,
            type = MessageType.SYSTEM,
            content = content,
            status = MessageStatus.SENT
        )
    }

    /**
     * 创建错误消息
     */
    fun createErrorMessage(
        conversationId: String,
        error: String
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = MessageRole.ASSISTANT,
            type = MessageType.ERROR,
            content = error,
            status = MessageStatus.FAILED,
            errorMessage = error
        )
    }

    /**
     * 创建代码消息
     */
    fun createCodeMessage(
        conversationId: String,
        code: String,
        language: String = "kotlin"
    ): ChatMessage {
        return ChatMessage(
            id = generateId(),
            conversationId = conversationId,
            role = MessageRole.ASSISTANT,
            type = MessageType.CODE,
            content = code,
            status = MessageStatus.SENT,
            metadata = mapOf("language" to language)
        )
    }

    /**
     * 生成唯一ID
     */
    private fun generateId(): String {
        return "msg_${System.currentTimeMillis()}_${(0..9999).random()}"
    }
}
