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
package com.aiai.chat.data.model

import com.aiai.chat.data.enums.AttachmentType

/**
 * 附件数据模型
 *
 * 描述用户在聊天中附加的文件、图片等信息。
 *
 * @property id 附件唯一标识
 * @property type 附件类型
 * @property uri 附件URI（内容提供者路径）
 * @property localPath 本地文件路径
 * @property url 远程URL
 * @property fileName 文件名
 * @property fileSize 文件大小（字节）
 * @property mimeType MIME类型
 * @property thumbnailPath 缩略图路径
 * @property duration 媒体时长（毫秒，音频/视频）
 * @property width 图片/视频宽度
 * @property height 图片/视频高度
 * @property progress 上传/下载进度（0-100）
 * @property isUploaded 是否已上传
 */
data class Attachment(
    val id: String,
    val type: AttachmentType,
    val uri: String? = null,
    val localPath: String? = null,
    val url: String? = null,
    val fileName: String = "",
    val fileSize: Long = 0,
    val mimeType: String = "",
    val thumbnailPath: String? = null,
    val duration: Long = 0,
    val width: Int = 0,
    val height: Int = 0,
    val progress: Int = 0,
    val isUploaded: Boolean = false
) {
    /**
     * 获取格式化的文件大小
     */
    fun getFormattedSize(): String {
        return when {
            fileSize < 1024 -> "${fileSize}B"
            fileSize < 1024 * 1024 -> "${fileSize / 1024}KB"
            fileSize < 1024 * 1024 * 1024 -> "${fileSize / (1024 * 1024)}MB"
            else -> String.format("%.2fGB", fileSize / (1024.0 * 1024.0 * 1024.0))
        }
    }

    /**
     * 获取格式化的时长
     */
    fun getFormattedDuration(): String {
        val totalSeconds = duration / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}
