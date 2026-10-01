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
 * 传输任务数据类。
 *
 * @property taskId 任务ID
 * @property url 目标URL
 * @property filePath 本地文件路径
 * @property totalBytes 总字节数
 * @property transferredBytes 已传输字节数
 * @property status 任务状态
 * @property type 任务类型（upload/download）
 * @property speedBytesPerSecond 传输速度（字节/秒）
 * @property error 错误信息
 * @property createdAt 创建时间
 */
data class TransferTask(
    val taskId: String = "",
    val url: String = "",
    val filePath: String = "",
    val totalBytes: Long = 0L,
    val transferredBytes: Long = 0L,
    val status: TransferStatus = TransferStatus.PENDING,
    val type: TransferType = TransferType.DOWNLOAD,
    val speedBytesPerSecond: Long = 0L,
    val error: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
) {
    /**
     * 获取传输进度百分比。
     *
     * @return 0-100的百分比
     */
    fun progressPercent(): Int {
        if (totalBytes <= 0) return 0
        return (transferredBytes * 100 / totalBytes).toInt().coerceAtMost(100)
    }

    /**
     * 是否已完成。
     *
     * @return true如果状态为COMPLETED
     */
    fun isCompleted(): Boolean = status == TransferStatus.COMPLETED

    /**
     * 是否失败。
     *
     * @return true如果状态为FAILED
     */
    fun isFailed(): Boolean = status == TransferStatus.FAILED

    /**
     * 是否正在传输。
     *
     * @return true如果状态为RUNNING
     */
    fun isRunning(): Boolean = status == TransferStatus.RUNNING
}

/**
 * 传输状态枚举。
 */
enum class TransferStatus {
    /** 等待中 */
    PENDING,

    /** 传输中 */
    RUNNING,

    /** 暂停 */
    PAUSED,

    /** 已完成 */
    COMPLETED,

    /** 失败 */
    FAILED,

    /** 已取消 */
    CANCELLED,
}

/**
 * 传输类型枚举。
 */
enum class TransferType {
    /** 上传 */
    UPLOAD,

    /** 下载 */
    DOWNLOAD,
}
