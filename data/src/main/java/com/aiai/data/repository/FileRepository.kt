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
package com.aiai.data.repository

import com.aiai.data.entity.ChatAttachmentEntity
import com.aiai.network.model.response.FileUploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

/** 文件仓库接口 */
interface FileRepository {
    fun getAttachmentsByMessage(messageId: String): Flow<List<ChatAttachmentEntity>>
    suspend fun saveAttachment(attachment: ChatAttachmentEntity)
    suspend fun deleteAttachment(id: String)
    suspend fun uploadImage(file: File): Result<FileUploadResponse>
    suspend fun uploadFile(file: File): Result<FileUploadResponse>
    suspend fun downloadFile(url: String, savePath: String): Result<Unit>
    suspend fun updateUploadStatus(id: String, status: String)
    suspend fun getAttachmentCount(messageId: String): Int
    fun getPendingUploads(): Flow<List<ChatAttachmentEntity>>
}
