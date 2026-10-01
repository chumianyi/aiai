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
import com.aiai.data.dao.ChatAttachmentDao
import com.aiai.data.entity.ChatAttachmentEntity

/**
 * 附件管理器。
 *
 * 管理聊天中的附件上传、下载、状态跟踪等功能。
 */
class AttachmentManager(
    private val attachmentDao: ChatAttachmentDao,
) {

    companion object {
        private const val TAG = "AttachmentManager"

        const val STATUS_PENDING = "pending"
        const val STATUS_UPLOADING = "uploading"
        const val STATUS_UPLOADED = "uploaded"
        const val STATUS_FAILED = "failed"
    }

    /**
     * 添加附件。
     */
    suspend fun addAttachment(
        messageId: String,
        fileName: String,
        filePath: String,
        fileType: String,
        fileSize: Long,
        mimeType: String,
    ): Long {
        val attachment = ChatAttachmentEntity(
            id = java.util.UUID.randomUUID().toString(),
            messageId = messageId,
            fileName = fileName,
            filePath = filePath,
            fileType = fileType,
            fileSize = fileSize,
            mimeType = mimeType,
            width = 0,
            height = 0,
            duration = 0,
            uploadStatus = STATUS_PENDING,
        )
        return attachmentDao.insert(attachment)
    }

    /**
     * 更新上传状态。
     */
    suspend fun updateUploadStatus(attachmentId: String, status: String) {
        attachmentDao.updateStatus(attachmentId, status)
        Log.d(TAG, "Updated status for $attachmentId: $status")
    }

    /**
     * 获取消息的所有附件。
     */
    fun getAttachmentsForMessage(messageId: String): List<ChatAttachmentEntity> {
        return attachmentDao.getByMessageIdSync(messageId)
    }

    /**
     * 获取上传失败的附件。
     */
    fun getFailedAttachments(): List<ChatAttachmentEntity> {
        return attachmentDao.getByStatusSync(STATUS_FAILED)
    }

    /**
     * 删除附件。
     */
    suspend fun deleteAttachment(attachmentId: String) {
        attachmentDao.deleteById(attachmentId)
        Log.d(TAG, "Deleted attachment: $attachmentId")
    }

    /**
     * 删除消息的所有附件。
     */
    suspend fun deleteAttachmentsForMessage(messageId: String) {
        attachmentDao.deleteByMessageId(messageId)
        Log.d(TAG, "Deleted attachments for message: $messageId")
    }

    /**
     * 获取附件总数。
     */
    fun getTotalAttachmentCount(): Int {
        return attachmentDao.getCountSync()
    }

    /**
     * 获取总文件大小。
     */
    fun getTotalFileSize(): Long {
        return attachmentDao.getTotalFileSizeSync()
    }
}
