/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.data.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * 数据同步器。
 *
 * 提供本地/远程同步、冲突解决、增量同步等功能。
 */
class DataSynchronizer {

    companion object {
        private const val TAG = "DataSynchronizer"
    }

    /**
     * 同步状态。
     */
    sealed class SyncState {
        /** 空闲状态。 */
        object Idle : SyncState()

        /** 正在同步。 */
        object Syncing : SyncState()

        /**
         * 同步成功。
         *
         * @property syncedCount 同步的条目数
         */
        data class Success(val syncedCount: Int) : SyncState()

        /**
         * 同步错误。
         *
         * @property message 错误信息
         */
        data class Error(val message: String) : SyncState()
    }

    /**
     * 同步策略枚举。
     */
    enum class SyncStrategy {
        /** 远程优先。 */
        REMOTE_WINS,
        /** 本地优先。 */
        LOCAL_WINS,
        /** 手动解决冲突。 */
        MANUAL
    }

    /**
     * 增量同步。
     *
     * @param lastSyncTime 上次同步时间
     * @param fetchRemote 从远程获取增量数据
     * @param saveLocal 保存到本地
     * @param strategy 冲突解决策略
     * @return 同步状态流
     */
    fun <T> incrementalSync(
        lastSyncTime: Long,
        fetchRemote: suspend (Long) -> List<T>,
        saveLocal: suspend (List<T>) -> Unit,
        strategy: SyncStrategy = SyncStrategy.REMOTE_WINS
    ): Flow<SyncState> = flow {
        emit(SyncState.Syncing)

        try {
            Log.d(TAG, "Starting incremental sync from $lastSyncTime")

            // 从远程获取增量数据
            val remoteData = fetchRemote(lastSyncTime)
            Log.d(TAG, "Fetched ${remoteData.size} items from remote")

            // 保存到本地
            saveLocal(remoteData)
            Log.d(TAG, "Saved ${remoteData.size} items to local")

            emit(SyncState.Success(remoteData.size))
        } catch (e: Exception) {
            Log.e(TAG, "Sync failed", e)
            emit(SyncState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 全量同步。
     *
     * @param fetchRemote 从远程获取全量数据
     * @param clearLocal 清除本地数据
     * @param saveLocal 保存到本地
     * @return 同步状态流
     */
    fun <T> fullSync(
        fetchRemote: suspend () -> List<T>,
        clearLocal: suspend () -> Unit,
        saveLocal: suspend (List<T>) -> Unit
    ): Flow<SyncState> = flow {
        emit(SyncState.Syncing)

        try {
            Log.d(TAG, "Starting full sync")

            // 从远程获取全量数据
            val remoteData = fetchRemote()
            Log.d(TAG, "Fetched ${remoteData.size} items from remote")

            // 清除本地数据
            clearLocal()
            Log.d(TAG, "Cleared local data")

            // 保存到本地
            saveLocal(remoteData)
            Log.d(TAG, "Saved ${remoteData.size} items to local")

            emit(SyncState.Success(remoteData.size))
        } catch (e: Exception) {
            Log.e(TAG, "Full sync failed", e)
            emit(SyncState.Error(e.message ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)
}
