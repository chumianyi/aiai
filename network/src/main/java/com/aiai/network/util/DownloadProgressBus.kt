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
package com.aiai.network.util

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * 下载进度事件。
 */
sealed class DownloadProgress {
    /** 开始下载 */
    data class Started(val url: String, val totalBytes: Long) : DownloadProgress()

    /** 进度更新 */
    data class Progress(val percent: Int, val downloadedBytes: Long, val totalBytes: Long) : DownloadProgress()

    /** 下载完成 */
    data class Completed(val url: String, val filePath: String) : DownloadProgress()

    /** 下载失败 */
    data class Failed(val url: String, val error: String) : DownloadProgress()

    /** 暂停 */
    data class Paused(val url: String, val downloadedBytes: Long) : DownloadProgress()
}

/**
 * 下载进度事件总线。
 *
 * 全局分发下载进度事件。
 */
object DownloadProgressBus {

    private val _events = MutableSharedFlow<DownloadProgress>(extraBufferCapacity = 64)
    val events: SharedFlow<DownloadProgress> = _events.asSharedFlow()

    /**
     * 发送下载进度事件。
     */
    fun emit(event: DownloadProgress) {
        _events.tryEmit(event)
    }
}
