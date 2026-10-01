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

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.View

/**
 * 截图管理器。
 *
 * 提供 View 截图、屏幕截图、截图分享等功能。
 */
object ScreenshotManager {

    private const val TAG = "ScreenshotManager"

    /**
     * 对 View 进行截图。
     *
     * @param view 要截图的 View
     * @return 截图 Bitmap
     */
    fun captureView(view: View): Bitmap? {
        return try {
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            Log.d(TAG, "View captured successfully")
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Capture view failed", e)
            null
        }
    }

    /**
     * 保存 Bitmap 到文件。
     *
     * @param bitmap 截图 Bitmap
     * @param context 上下文
     * @param fileName 文件名
     * @return 文件路径
     */
    fun saveBitmap(bitmap: Bitmap, context: Context, fileName: String): String? {
        return try {
            val file = java.io.File(context.externalCacheDir, "screenshots/$fileName.png")
            file.parentFile?.mkdirs()
            file.outputStream().use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            Log.d(TAG, "Screenshot saved: ${file.absolutePath}")
            file.absolutePath
        } catch (e: Exception) {
            Log.e(TAG, "Save bitmap failed", e)
            null
        }
    }

    /**
     * 检查是否可以截屏。
     *
     * @return 是否可以
     */
    fun canScreenshot(): Boolean {
        return true // 实际实现需要检查权限
    }

    /**
     * 获取截图目录。
     *
     * @param context 上下文
     * @return 截图目录
     */
    fun getScreenshotDir(context: Context): java.io.File {
        val dir = java.io.File(context.externalCacheDir, "screenshots")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    /**
     * 清除所有截图。
     *
     * @param context 上下文
     */
    fun clearScreenshots(context: Context) {
        try {
            getScreenshotDir(context).deleteRecursively()
            Log.d(TAG, "Screenshots cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Clear screenshots failed", e)
        }
    }
}
