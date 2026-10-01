/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.manager

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 数据同步管理器。
 *
 * 管理本地数据与服务器之间的同步。
 */
class DataSyncManager {

    companion object {
        private const val TAG = "DataSyncManager"
        private const val SYNC_INTERVAL_MS = 15 * 60 * 1000L // 15分钟
    }

    private var lastSyncTime: Long = 0L

    /**
     * 检查是否需要同步。
     *
     * @return true如果需要同步
     */
    fun shouldSync(): Boolean {
        return System.currentTimeMillis() - lastSyncTime > SYNC_INTERVAL_MS
    }

    /**
     * 标记同步完成。
     */
    fun markSynced() {
        lastSyncTime = System.currentTimeMillis()
        Log.d(TAG, "Sync completed at ${SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())}")
    }

    /**
     * 同步状态。
     */
    sealed class SyncState {
        /** 空闲 */
        object Idle : SyncState()
        /** 同步中 */
        object Syncing : SyncState()
        /** 同步完成 */
        data class Success(val itemCount: Int) : SyncState()
        /** 同步失败 */
        data class Failed(val error: String) : SyncState()
    }

    /**
     * 获取最后同步时间。
     */
    fun getLastSyncTime(): Long = lastSyncTime

    /**
     * 格式化最后同步时间。
     */
    fun getLastSyncTimeFormatted(): String {
        if (lastSyncTime == 0L) return "从未同步"
        return SimpleDateFormat("MM-dd HH:mm", Locale.US).format(Date(lastSyncTime))
    }
}
