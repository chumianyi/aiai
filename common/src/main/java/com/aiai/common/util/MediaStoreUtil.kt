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
package com.aiai.common.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log

/**
 * 媒体库工具类。
 *
 * 提供图片/视频/音频查询、插入、删除等功能。
 */
object MediaStoreUtil {

    private const val TAG = "MediaStoreUtil"

    /**
     * 媒体文件数据模型。
     */
    data class MediaFile(
        val id: Long,
        val displayName: String,
        val size: Long,
        val duration: Long,
        val mimeType: String,
        val uri: Uri,
        val dateAdded: Long
    )

    /**
     * 获取所有图片。
     *
     * @param context 上下文
     * @param limit 数量限制
     * @return 图片列表
     */
    fun getImages(context: Context, limit: Int = 100): List<MediaFile> {
        return queryMedia(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, limit)
    }

    /**
     * 获取所有视频。
     *
     * @param context 上下文
     * @param limit 数量限制
     * @return 视频列表
     */
    fun getVideos(context: Context, limit: Int = 100): List<MediaFile> {
        return queryMedia(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, limit)
    }

    /**
     * 获取所有音频。
     *
     * @param context 上下文
     * @param limit 数量限制
     * @return 音频列表
     */
    fun getAudios(context: Context, limit: Int = 100): List<MediaFile> {
        return queryMedia(context, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, limit)
    }

    /**
     * 查询媒体文件。
     */
    private fun queryMedia(context: Context, collection: Uri, limit: Int): List<MediaFile> {
        val files = mutableListOf<MediaFile>()
        val resolver = context.contentResolver

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.SIZE,
            MediaStore.MediaColumns.DURATION,
            MediaStore.MediaColumns.MIME_TYPE,
            MediaStore.MediaColumns.DATE_ADDED
        )

        val cursor = resolver.query(
            collection,
            projection,
            null, null,
            "${MediaStore.MediaColumns.DATE_ADDED} DESC LIMIT $limit"
        )

        cursor?.use {
            val idIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)
            val sizeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            val durationIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DURATION)
            val mimeIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)
            val dateIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)

            while (it.moveToNext()) {
                val id = it.getLong(idIndex)
                files.add(
                    MediaFile(
                        id = id,
                        displayName = it.getString(nameIndex) ?: "",
                        size = it.getLong(sizeIndex),
                        duration = it.getLong(durationIndex),
                        mimeType = it.getString(mimeIndex) ?: "",
                        uri = Uri.withAppendedPath(collection, id.toString()),
                        dateAdded = it.getLong(dateIndex)
                    )
                )
            }
        }

        return files
    }

    /**
     * 保存图片到相册。
     *
     * @param context 上下文
     * @param bitmap 位图
     * @param title 标题
     * @param description 描述
     * @return 保存后的 Uri
     */
    fun saveImageToGallery(
        context: Context,
        bitmap: android.graphics.Bitmap,
        title: String,
        description: String = ""
    ): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, title)
            put(MediaStore.Images.Media.DESCRIPTION, description)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            resolver.openOutputStream(it).use { outputStream ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, outputStream)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.clear()
                values.put(MediaStore.Images.Media.IS_PENDING, 0)
                resolver.update(it, values, null, null)
            }
        }

        return uri
    }

    /**
     * 删除媒体文件。
     *
     * @param context 上下文
     * @param uri 要删除的 Uri
     * @return true 表示成功
     */
    fun deleteMedia(context: Context, uri: Uri): Boolean {
        return try {
            context.contentResolver.delete(uri, null, null) > 0
        } catch (e: Exception) {
            Log.e(TAG, "Delete media failed", e)
            false
        }
    }

    /**
     * 获取文件大小格式化。
     *
     * @param size 文件大小（字节）
     * @return 格式化后的大小
     */
    fun formatFileSize(size: Long): String {
        return when {
            size < 1024 -> "${size}B"
            size < 1024 * 1024 -> "${"%.1f".format(size / 1024.0)}KB"
            size < 1024 * 1024 * 1024 -> "${"%.1f".format(size / (1024.0 * 1024.0))}MB"
            else -> "${"%.1f".format(size / (1024.0 * 1024.0 * 1024.0))}GB"
        }
    }

    /**
     * 获取时长格式化。
     *
     * @param duration 时长（毫秒）
     * @return 格式化后的时长
     */
    fun formatDuration(duration: Long): String {
        val seconds = duration / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60)
        } else {
            String.format("%d:%02d", minutes, seconds % 60)
        }
    }
}
