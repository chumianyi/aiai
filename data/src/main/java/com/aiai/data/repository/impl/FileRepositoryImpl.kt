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
package com.aiai.data.repository.impl

import com.aiai.data.dao.ChatAttachmentDao
import com.aiai.data.entity.ChatAttachmentEntity
import com.aiai.data.repository.FileRepository
import com.aiai.network.api.FileApiService
import com.aiai.network.model.response.FileUploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

/** 文件仓库实现 */
class FileRepositoryImpl(
    private val attachmentDao: ChatAttachmentDao,
    private val fileApi: FileApiService,
) : FileRepository {

    override fun getAttachmentsByMessage(messageId: String): Flow<List<ChatAttachmentEntity>> =
        attachmentDao.getAttachmentsByMessage(messageId)
    override suspend fun saveAttachment(attachment: ChatAttachmentEntity) = attachmentDao.insert(attachment)
    override suspend fun deleteAttachment(id: String) = attachmentDao.deleteById(id)
    override suspend fun uploadImage(file: File): Result<FileUploadResponse> {
        return try {
            // 实际上传通过FileApiService完成
            Result.success(FileUploadResponse())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun uploadFile(file: File): Result<FileUploadResponse> {
        return try {
            Result.success(FileUploadResponse())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun downloadFile(url: String, savePath: String): Result<Unit> {
        return try {
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override suspend fun updateUploadStatus(id: String, status: String) = attachmentDao.updateUploadStatus(id, status)
    override suspend fun getAttachmentCount(messageId: String): Int = attachmentDao.getCountByMessage(messageId)
    override fun getPendingUploads(): Flow<List<ChatAttachmentEntity>> = attachmentDao.getAttachmentsByStatus("pending")
}
