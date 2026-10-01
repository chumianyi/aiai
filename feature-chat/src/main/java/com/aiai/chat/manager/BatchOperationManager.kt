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

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息批量操作管理器
 *
 * 管理消息的多选和批量操作。
 */
class BatchOperationManager(private val context: Context) {

    data class BatchState(
        val selectMode: Boolean = false,
        val selectedIds: Set<String> = emptySet()
    )

    private val _batchState = MutableStateFlow(BatchState())
    val batchState: StateFlow<BatchState> = _batchState.asStateFlow()

    fun enterSelectMode() {
        _batchState.value = _batchState.value.copy(selectMode = true)
    }

    fun exitSelectMode() {
        _batchState.value = BatchState()
    }

    fun toggleSelect(messageId: String) {
        val current = _batchState.value.selectedIds
        _batchState.value = if (messageId in current) {
            _batchState.value.copy(selectedIds = current - messageId)
        } else {
            _batchState.value.copy(selectedIds = current + messageId)
        }
    }

    fun selectAll(messageIds: List<String>) {
        _batchState.value = _batchState.value.copy(selectedIds = messageIds.toSet())
    }

    fun clearSelection() {
        _batchState.value = _batchState.value.copy(selectedIds = emptySet())
    }

    fun getSelectedCount(): Int {
        return _batchState.value.selectedIds.size
    }

    fun isSelected(messageId: String): Boolean {
        return messageId in _batchState.value.selectedIds
    }

    fun isSelectMode(): Boolean {
        return _batchState.value.selectMode
    }
}
