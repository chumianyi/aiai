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
package com.aiai.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 聊天附件表实体。
 *
 * 存储消息中的附件（图片、文件、音频等）。
 *
 * @property id 附件ID
 * @property messageId 关联消息ID
 * @property fileName 文件名
 * @property filePath 本地文件路径
 * @property fileType 文件类型
 * @property fileSize 文件大小（字节）
 * @property mimeType MIME类型
 * @property width 图片宽度
 * @property height 图片高度
 * @property duration 音视频时长（毫秒）
 * @property uploadStatus 上传状态（pending/uploading/success/failed）
 */
@Entity(
    tableName = "chat_attachments",
    foreignKeys = [ForeignKey(
        entity = MessageEntity::class,
        parentColumns = ["id"],
        childColumns = ["messageId"],
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [
        Index(value = ["messageId"]),
        Index(value = ["uploadStatus"]),
    ],
)
data class ChatAttachmentEntity(
    @PrimaryKey
    val id: String,
    val messageId: String,
    val fileName: String = "",
    val filePath: String = "",
    val fileType: String = "file",
    val fileSize: Long = 0L,
    val mimeType: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val duration: Long = 0L,
    val uploadStatus: String = "pending",
)
