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
package com.aiai.network.transfer

/**
 * 传输进度监听器接口。
 */
interface TransferListener {

    /**
     * 传输开始。
     *
     * @param task 传输任务
     */
    fun onStart(task: TransferTask)

    /**
     * 进度更新。
     *
     * @param task 传输任务
     * @param progress 进度百分比（0-100）
     * @param speedBytesPerSecond 速度（字节/秒）
     */
    fun onProgress(task: TransferTask, progress: Int, speedBytesPerSecond: Long)

    /**
     * 传输完成。
     *
     * @param task 传输任务
     * @param resultPath 结果文件路径
     */
    fun onComplete(task: TransferTask, resultPath: String)

    /**
     * 传输失败。
     *
     * @param task 传输任务
     * @param error 错误信息
     */
    fun onError(task: TransferTask, error: String)

    /**
     * 传输暂停。
     *
     * @param task 传输任务
     */
    fun onPause(task: TransferTask)

    /**
     * 传输取消。
     *
     * @param task 传输任务
     */
    fun onCancel(task: TransferTask)
}
