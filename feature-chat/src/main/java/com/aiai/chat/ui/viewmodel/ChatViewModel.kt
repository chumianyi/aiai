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
package com.aiai.chat.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.chat.data.enums.MessageStatus
import com.aiai.chat.data.enums.MessageType
import com.aiai.chat.data.model.ChatMessage
import com.aiai.chat.data.model.Conversation
import com.aiai.chat.data.model.StreamChunk
import com.aiai.chat.manager.ChatManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 聊天ViewModel
 *
 * 管理聊天页面的完整状态，包括：
 * - 发送消息
 * - 流式接收
 * - 重试、停止、删除消息
 * - 清空对话
 * - 模型切换
 */
class ChatViewModel : ViewModel() {

    private val chatManager = ChatManager.getInstance()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _currentModelName = MutableStateFlow("gpt-4o-mini")
    val currentModelName: StateFlow<String> = _currentModelName.asStateFlow()

    /**
     * 初始化会话
     */
    fun initConversation(conversation: Conversation) {
        viewModelScope.launch {
            chatManager.initConversation(conversation.id)
            _uiState.value = _uiState.value.copy(
                conversationId = conversation.id,
                title = conversation.title,
                modelName = conversation.modelName
            )
            _currentModelName.value = conversation.modelName
            // 加载历史消息
            chatManager.messages.collect { msgs ->
                _messages.value = msgs
            }
        }
    }

    /**
     * 发送文本消息
     */
    fun sendMessage(content: String) {
        if (content.isBlank() || _isGenerating.value) return

        viewModelScope.launch {
            // 1. 添加用户消息
            val userMessage = chatManager.sendUserMessage(content)
            _inputText.value = ""

            // 2. 创建AI占位消息
            val aiMessage = chatManager.createAiPlaceholder(_currentModelName.value)
            _isGenerating.value = true

            // 3. 模拟流式接收（实际项目中调用StreamHandler）
            simulateStreamResponse(aiMessage.id, content)
        }
    }

    /**
     * 模拟流式响应（演示用）
     */
    private suspend fun simulateStreamResponse(messageId: String, userQuery: String) {
        val response = "收到你的问题：\"$userQuery\"\n\n这是AI的回复内容。在实际应用中，这里会通过SSE流式接收模型生成的回复。回复会逐字显示，提供更好的用户体验。\n\n**主要特点：**\n- 实时流式输出\n- 支持Markdown渲染\n- 代码块高亮\n- 表格和任务列表"

        response.chunked(5).forEach { chunk ->
            kotlinx.coroutines.delay(50)
            chatManager.updateStreamChunk(messageId, StreamChunk(deltaContent = chunk))
        }
        chatManager.updateStreamChunk(messageId, StreamChunk(isFinished = true, finishReason = "stop"))
        _isGenerating.value = false
    }

    /**
     * 停止生成
     */
    fun stopGenerating() {
        viewModelScope.launch {
            val lastAiMessage = _messages.value.lastOrNull { it.status == MessageStatus.STREAMING }
            lastAiMessage?.let {
                chatManager.stopGeneration(it.id)
            }
            _isGenerating.value = false
        }
    }

    /**
     * 重试失败的消息
     */
    fun retryMessage(messageId: String) {
        viewModelScope.launch {
            chatManager.retryMessage(messageId)
        }
    }

    /**
     * 删除消息
     */
    fun deleteMessage(messageId: String) {
        viewModelScope.launch {
            chatManager.deleteMessage(messageId)
        }
    }

    /**
     * 清空对话
     */
    fun clearConversation() {
        viewModelScope.launch {
            chatManager.clearMessages()
        }
    }

    /**
     * 更新输入框内容
     */
    fun onInputTextChanged(text: String) {
        _inputText.value = text
    }

    /**
     * 切换模型
     */
    fun switchModel(modelName: String) {
        _currentModelName.value = modelName
    }

    /**
     * UI状态数据类
     */
    data class ChatUiState(
        val conversationId: String = "",
        val title: String = "新对话",
        val modelName: String = "gpt-4o-mini",
        val isLoading: Boolean = false,
        val error: String? = null
    )
}
