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
package com.aiai.network.model.request

import com.google.gson.annotations.SerializedName

/**
 * 文件上传请求模型。
 *
 * 用于文件上传的API请求。
 *
 * @property fileName 文件名
 * @property fileType 文件类型（image/document/audio/video）
 * @property mimeType MIME类型
 * @property fileSize 文件大小（字节）
 * @property conversationId 关联会话ID
 * @property compress 是否压缩
 * @property width 图片宽度
 * @property height 图片高度
 * @property duration 音频/视频时长（毫秒）
 */
data class FileUploadRequest(
    @SerializedName("file_name")
    val fileName: String = "",

    @SerializedName("file_type")
    val fileType: String = "document",

    @SerializedName("mime_type")
    val mimeType: String = "application/octet-stream",

    @SerializedName("file_size")
    val fileSize: Long = 0L,

    @SerializedName("conversation_id")
    val conversationId: String = "",

    @SerializedName("compress")
    val compress: Boolean = true,

    @SerializedName("width")
    val width: Int = 0,

    @SerializedName("height")
    val height: Int = 0,

    @SerializedName("duration")
    val duration: Long = 0L,
) {
    init {
        require(fileName.isNotBlank()) { "file_name must not be blank" }
        require(fileSize >= 0) { "file_size must be non-negative" }
    }
}
