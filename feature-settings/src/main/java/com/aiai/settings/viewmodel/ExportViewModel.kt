/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.settings.manager.ExportManager
import com.aiai.settings.model.ExportConversation
import com.aiai.settings.model.ExportFormat
import com.aiai.settings.model.ExportProgress
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

/** 导出聊天 UI 状态。 */
data class ExportChatUiState(
    val conversations: List<ExportConversation> = emptyList(),
    val format: ExportFormat = ExportFormat.TXT,
    val progress: ExportProgress = ExportProgress(),
    val resultFiles: List<File> = emptyList()
)

/**
 * 导出聊天记录 ViewModel。
 */
class ExportViewModel(app: Application) : AndroidViewModel(app) {

    private val manager = ExportManager.get(app)

    private val _uiState = MutableStateFlow(
        ExportChatUiState(conversations = mockConversations())
    )
    val uiState: StateFlow<ExportChatUiState> = _uiState.asStateFlow()

    init {
        // 订阅导出进度
        viewModelScope.launch {
            manager.progress.collect { p ->
                _uiState.value = _uiState.value.copy(progress = p)
            }
        }
    }

    private fun mockConversations(): List<ExportConversation> {
        val now = System.currentTimeMillis()
        return listOf(
            ExportConversation("c1", "翻译助手会话", 42, now - 1000),
            ExportConversation("c2", "代码 Review 记录", 128, now - 5000),
            ExportConversation("c3", "周报生成讨论", 15, now - 9000),
            ExportConversation("c4", "读书笔记：深入理解计算机系统", 67, now - 20000)
        )
    }

    fun toggleSelect(id: String) {
        _uiState.value = _uiState.value.copy(
            conversations = _uiState.value.conversations.map {
                if (it.conversationId == id) it.copy(checked = !it.checked) else it
            }
        )
    }

    fun selectAll() {
        _uiState.value = _uiState.value.copy(
            conversations = _uiState.value.conversations.map { it.copy(checked = true) }
        )
    }

    fun selectNone() {
        _uiState.value = _uiState.value.copy(
            conversations = _uiState.value.conversations.map { it.copy(checked = false) }
        )
    }

    fun setFormat(format: ExportFormat) {
        _uiState.value = _uiState.value.copy(format = format)
    }

    /** 开始导出。 */
    fun startExport() {
        val selected = _uiState.value.conversations.filter { it.checked }
        if (selected.isEmpty()) return
        viewModelScope.launch {
            val files = manager.export(selected, _uiState.value.format)
            _uiState.value = _uiState.value.copy(resultFiles = files)
        }
    }

    fun cancel() {
        manager.cancel()
    }
}
