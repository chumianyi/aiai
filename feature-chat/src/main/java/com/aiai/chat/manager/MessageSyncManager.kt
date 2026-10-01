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
import com.aiai.chat.data.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 消息同步管理器
 *
 * 管理多设备间的消息同步状态和冲突解决。
 */
class MessageSyncManager(private val context: Context) {

    enum class SyncStatus {
        IDLE,
        SYNCING,
        SUCCESS,
        FAILED
    }

    data class SyncProgress(
        val totalMessages: Int,
        val syncedMessages: Int,
        val percentage: Float
    )

    private val _syncStatus = MutableStateFlow(SyncStatus.IDLE)
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _syncProgress = MutableStateFlow(SyncProgress(0, 0, 0f))
    val syncProgress: StateFlow<SyncProgress> = _syncProgress.asStateFlow()

    private val _lastSyncTime = MutableStateFlow(0L)
    val lastSyncTime: StateFlow<Long> = _lastSyncTime.asStateFlow()

    fun startSync(totalMessages: Int) {
        _syncStatus.value = SyncStatus.SYNCING
        _syncProgress.value = SyncProgress(totalMessages, 0, 0f)
    }

    fun updateProgress(synced: Int) {
        val total = _syncProgress.value.totalMessages
        val percentage = if (total > 0) synced.toFloat() / total else 0f
        _syncProgress.value = _syncProgress.value.copy(
            syncedMessages = synced,
            percentage = percentage
        )
    }

    fun finishSync(success: Boolean) {
        _syncStatus.value = if (success) SyncStatus.SUCCESS else SyncStatus.FAILED
        if (success) {
            _lastSyncTime.value = System.currentTimeMillis()
        }
    }

    fun reset() {
        _syncStatus.value = SyncStatus.IDLE
        _syncProgress.value = SyncProgress(0, 0, 0f)
    }

    fun needsSync(): Boolean {
        val hour = 60 * 60 * 1000L
        return System.currentTimeMillis() - _lastSyncTime.value > hour
    }
}
