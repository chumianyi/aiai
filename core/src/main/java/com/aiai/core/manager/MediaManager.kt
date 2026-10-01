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
package com.aiai.core.manager

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log

/**
 * 媒体管理器。
 *
 * 提供图片/视频/音频扫描、缩略图、元数据等功能。
 */
class MediaManager(private val context: Context) {

    companion object {
        private const val TAG = "MediaManager"
    }

    /**
     * 获取视频缩略图。
     *
     * @param videoUri 视频 Uri
     * @param frameTime 帧时间（微秒）
     * @return 缩略图 Bitmap
     */
    fun getVideoThumbnail(videoUri: Uri, frameTime: Long = 0): Bitmap? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, videoUri)
            retriever.getFrameAtTime(frameTime, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        } catch (e: Exception) {
            Log.e(TAG, "Get video thumbnail failed", e)
            null
        } finally {
            retriever.release()
        }
    }

    /**
     * 获取视频元数据。
     *
     * @param videoUri 视频 Uri
     * @return 元数据 Map
     */
    fun getVideoMetadata(videoUri: Uri): Map<String, String> {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, videoUri)
            mapOf(
                "duration" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""),
                "width" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH) ?: ""),
                "height" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT) ?: ""),
                "mimeType" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: ""),
                "bitrate" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE) ?: "")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Get video metadata failed", e)
            emptyMap()
        } finally {
            retriever.release()
        }
    }

    /**
     * 获取音频元数据。
     *
     * @param audioUri 音频 Uri
     * @return 元数据 Map
     */
    fun getAudioMetadata(audioUri: Uri): Map<String, String> {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, audioUri)
            mapOf(
                "title" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) ?: ""),
                "artist" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ?: ""),
                "album" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) ?: ""),
                "duration" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: ""),
                "mimeType" to (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE) ?: "")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Get audio metadata failed", e)
            emptyMap()
        } finally {
            retriever.release()
        }
    }

    /**
     * 保存图片到相册。
     *
     * @param bitmap 位图
     * @param title 标题
     * @param description 描述
     * @return 保存后的 Uri
     */
    fun saveImageToGallery(bitmap: Bitmap, title: String, description: String = ""): Uri? {
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
            resolver.openOutputStream(it).use { stream ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
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
     * 压缩图片。
     *
     * @param bitmap 原始位图
     * @param quality 质量（0-100）
     * @param maxWidth 最大宽度
     * @param maxHeight 最大高度
     * @return 压缩后的位图
     */
    fun compressBitmap(
        bitmap: Bitmap,
        quality: Int = 80,
        maxWidth: Int = 1080,
        maxHeight: Int = 1920
    ): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width <= maxWidth && height <= maxHeight) {
            return bitmap
        }

        val ratio = minOf(maxWidth.toFloat() / width, maxHeight.toFloat() / height)
        width = (width * ratio).toInt()
        height = (height * ratio).toInt()

        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    /**
     * 获取图片尺寸。
     *
     * @param uri 图片 Uri
     * @return Pair(width, height)
     */
    fun getImageSize(uri: Uri): Pair<Int, Int> {
        val options = android.graphics.BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        context.contentResolver.openInputStream(uri)?.use {
            android.graphics.BitmapFactory.decodeStream(it, null, options)
        }
        return Pair(options.outWidth, options.outHeight)
    }

    /**
     * 格式化时长。
     *
     * @param durationMs 时长（毫秒）
     * @return 格式化后的时长
     */
    fun formatDuration(durationMs: Long): String {
        val seconds = durationMs / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60)
        } else {
            String.format("%d:%02d", minutes, seconds % 60)
        }
    }
}
