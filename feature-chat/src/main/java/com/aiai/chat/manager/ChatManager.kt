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
package com.aiai.chat.manager

import android.util.Log
import com.aiai.chat.data.enums.MessageStatus
import com.aiai.chat.data.enums.MessageType
import com.aiai.chat.data.model.ChatMessage
import com.aiai.chat.data.model.StreamChunk
import com.aiai.chat.handler.StreamHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * 聊天管理器
 *
 * 负责管理消息队列、流式处理、状态管理。
 * 维护当前会话的消息列表状态，处理消息发送和接收的完整流程。
 * 采用单例模式，全局共享聊天状态。
 */
class ChatManager private constructor() {

    private val TAG = "ChatManager"

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _currentConversationId = MutableStateFlow<String?>(null)
    val currentConversationId: StateFlow<String?> = _currentConversationId.asStateFlow()

    private val messageMutex = Mutex()
    private var streamHandler: StreamHandler? = null

    /**
     * 初始化会话
     */
    suspend fun initConversation(conversationId: String) {
        messageMutex.withLock {
            _currentConversationId.value = conversationId
            _messages.value = emptyList()
            Log.d(TAG, "Initialized conversation: $conversationId")
        }
    }

    /**
     * 加载历史消息
     */
    suspend fun loadHistory(history: List<ChatMessage>) {
        messageMutex.withLock {
            _messages.value = history.sortedBy { it.createdAt }
            Log.d(TAG, "Loaded ${history.size} messages")
        }
    }

    /**
     * 发送用户消息
     */
    suspend fun sendUserMessage(content: String, type: MessageType = MessageType.TEXT): ChatMessage {
        val convId = _currentConversationId.value ?: throw IllegalStateException("No active conversation")
        val message = ChatMessage.createUserText(convId, content)
        messageMutex.withLock {
            _messages.value = _messages.value + message
        }
        Log.d(TAG, "Sent user message: ${message.id}")
        return message
    }

    /**
     * 创建AI占位消息（等待流式输出）
     */
    suspend fun createAiPlaceholder(modelName: String): ChatMessage {
        val convId = _currentConversationId.value ?: throw IllegalStateException("No active conversation")
        val placeholder = ChatMessage.createAiPlaceholder(convId, modelName)
        messageMutex.withLock {
            _messages.value = _messages.value + placeholder
        }
        _isGenerating.value = true
        Log.d(TAG, "Created AI placeholder: ${placeholder.id}")
        return placeholder
    }

    /**
     * 更新流式内容
     */
    suspend fun updateStreamChunk(messageId: String, chunk: StreamChunk) {
        messageMutex.withLock {
            val currentList = _messages.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == messageId }
            if (index >= 0) {
                val old = currentList[index]
                val updated = old.copy(
                    content = old.content + chunk.deltaContent,
                    status = if (chunk.isFinished) MessageStatus.STREAM_COMPLETE else MessageStatus.STREAMING,
                    updatedAt = System.currentTimeMillis()
                )
                currentList[index] = updated
                _messages.value = currentList
            }
        }

        if (chunk.isFinished) {
            _isGenerating.value = false
        }
    }

    /**
     * 停止生成
     */
    suspend fun stopGeneration(messageId: String) {
        streamHandler?.cancel()
        messageMutex.withLock {
            val currentList = _messages.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == messageId }
            if (index >= 0) {
                val old = currentList[index]
                val updated = old.copy(status = MessageStatus.STOPPED)
                currentList[index] = updated
                _messages.value = currentList
            }
        }
        _isGenerating.value = false
        Log.d(TAG, "Stopped generation: $messageId")
    }

    /**
     * 标记消息发送失败
     */
    suspend fun markMessageFailed(messageId: String, error: String) {
        messageMutex.withLock {
            val currentList = _messages.value.toMutableList()
            val index = currentList.indexOfFirst { it.id == messageId }
            if (index >= 0) {
                val old = currentList[index]
                val updated = old.copy(
                    status = MessageStatus.FAILED,
                    errorMessage = error
                )
                currentList[index] = updated
                _messages.value = currentList
            }
        }
        _isGenerating.value = false
    }

    /**
     * 删除消息
     */
    suspend fun deleteMessage(messageId: String) {
        messageMutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == messageId }
        }
        Log.d(TAG, "Deleted message: $messageId")
    }

    /**
     * 清空当前会话所有消息
     */
    suspend fun clearMessages() {
        messageMutex.withLock {
            _messages.value = emptyList()
        }
        _isGenerating.value = false
        Log.d(TAG, "Cleared all messages")
    }

    /**
     * 重试发送失败的消息
     */
    suspend fun retryMessage(messageId: String): ChatMessage? {
        val message = _messages.value.find { it.id == messageId } ?: return null
        return if (message.isUserMessage()) {
            sendUserMessage(message.content, message.type)
        } else {
            createAiPlaceholder(message.modelName)
        }
    }

    /**
     * 获取指定消息
     */
    fun getMessage(messageId: String): ChatMessage? {
        return _messages.value.find { it.id == messageId }
    }

    /**
     * 设置流式处理器
     */
    fun setStreamHandler(handler: StreamHandler) {
        streamHandler = handler
    }

    companion object {
        @Volatile
        private var INSTANCE: ChatManager? = null

        fun getInstance(): ChatManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ChatManager().also { INSTANCE = it }
            }
        }
    }
}
