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
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 文件上传响应模型。
 *
 * @property fileId 文件ID
 * @property fileName 文件名
 * @property fileUrl 文件URL
 * @property fileType 文件类型
 * @property mimeType MIME类型
 * @property fileSize 文件大小（字节）
 * @property width 图片宽度
 * @property height 图片高度
 * @property duration 音视频时长（毫秒）
 * @property createdAt 上传时间
 */
data class FileUploadResponse(
    @SerializedName("file_id")
    val fileId: String = "",

    @SerializedName("file_name")
    val fileName: String = "",

    @SerializedName("file_url")
    val fileUrl: String = "",

    @SerializedName("file_type")
    val fileType: String = "",

    @SerializedName("mime_type")
    val mimeType: String = "",

    @SerializedName("file_size")
    val fileSize: Long = 0L,

    @SerializedName("width")
    val width: Int = 0,

    @SerializedName("height")
    val height: Int = 0,

    @SerializedName("duration")
    val duration: Long = 0L,

    @SerializedName("created_at")
    val createdAt: String = "",
)
