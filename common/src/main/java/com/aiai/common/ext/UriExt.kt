/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.common.ext

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap

/**
 * Uri 相关扩展函数集合。
 *
 * 提供真实路径获取、文件大小、MIME 类型、权限申请等能力。
 */

// region 文件信息

/** 通过 Uri 查询文件大小（字节）。 */
fun Uri.fileSize(context: Context): Long {
    val resolver: ContentResolver = context.contentResolver
    return try {
        resolver.query(this, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) cursor.getLong(idx) else 0L
        } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

/** 通过 Uri 查询文件名。 */
fun Uri.fileName(context: Context): String? {
    val resolver: ContentResolver = context.contentResolver
    return try {
        resolver.query(this, null, null, null, null)?.use { cursor ->
            val idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) cursor.getString(idx) else null
        }
    } catch (e: Exception) {
        null
    }
}

/** 获取 Uri 对应的 MIME 类型。 */
fun Uri.mimeType(context: Context): String? {
    return when (scheme) {
        ContentResolver.SCHEME_CONTENT -> context.contentResolver.getType(this)
        else -> {
            val ext = MimeTypeMap.getFileExtensionFromUrl(toString())
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext?.lowercase())
        }
    }
}

// endregion

// region 权限

/** 为 Uri 授予临时读权限。 */
fun Uri.grantReadPermission(context: Context) {
    try {
        context.grantUriPermission(
            context.packageName, this,
            android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
        )
    } catch (e: Exception) {
        // 忽略
    }
}

/** 判断是否为 content Uri。 */
fun Uri.isContent(): Boolean = scheme == ContentResolver.SCHEME_CONTENT

/** 判断是否为 file Uri。 */
fun Uri.isFile(): Boolean = scheme == "file"

// endregion
