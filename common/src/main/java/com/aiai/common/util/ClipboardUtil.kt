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

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.util.Log

/**
 * 剪贴板工具类。
 *
 * 提供文本复制、图片复制、监听、清空等功能。
 */
object ClipboardUtil {

    private const val TAG = "ClipboardUtil"
    private const val LABEL_TEXT = "text"
    private const val LABEL_IMAGE = "image"

    /**
     * 复制文本到剪贴板。
     *
     * @param context 上下文
     * @param text 要复制的文本
     * @param label 标签
     * @return true 表示成功
     */
    fun copyText(context: Context, text: String, label: String = LABEL_TEXT): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText(label, text)
            clipboard.setPrimaryClip(clip)
            Log.d(TAG, "Text copied: $text")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Copy text failed", e)
            false
        }
    }

    /**
     * 复制 HTML 文本到剪贴板。
     *
     * @param context 上下文
     * @param text 纯文本
     * @param htmlText HTML 文本
     * @param label 标签
     * @return true 表示成功
     */
    fun copyHtmlText(
        context: Context,
        text: String,
        htmlText: String,
        label: String = LABEL_TEXT
    ): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newHtmlText(label, text, htmlText)
            clipboard.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Copy HTML text failed", e)
            false
        }
    }

    /**
     * 复制 Uri 到剪贴板。
     *
     * @param context 上下文
     * @param uri 要复制的 Uri
     * @param label 标签
     * @return true 表示成功
     */
    fun copyUri(context: Context, uri: Uri, label: String = "uri"): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newUri(context.contentResolver, label, uri)
            clipboard.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Copy URI failed", e)
            false
        }
    }

    /**
     * 从剪贴板获取文本。
     *
     * @param context 上下文
     * @return 剪贴板文本，为空返回 null
     */
    fun getText(context: Context): String? {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip ?: return null
            if (clip.itemCount > 0) {
                clip.getItemAt(0).text?.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get text failed", e)
            null
        }
    }

    /**
     * 从剪贴板获取 Uri。
     *
     * @param context 上下文
     * @return 剪贴板 Uri，为空返回 null
     */
    fun getUri(context: Context): Uri? {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = clipboard.primaryClip ?: return null
            if (clip.itemCount > 0) {
                clip.getItemAt(0).uri
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get URI failed", e)
            null
        }
    }

    /**
     * 检查剪贴板是否有文本。
     *
     * @param context 上下文
     * @return true 表示有文本
     */
    fun hasText(context: Context): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检查剪贴板是否有 HTML 文本。
     *
     * @param context 上下文
     * @return true 表示有 HTML 文本
     */
    fun hasHtmlText(context: Context): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 清空剪贴板。
     *
     * @param context 上下文
     */
    fun clear(context: Context) {
        try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                clipboard.clearPrimaryClip()
            } else {
                clipboard.setPrimaryClip(ClipData.newPlainText("", ""))
            }
            Log.d(TAG, "Clipboard cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Clear clipboard failed", e)
        }
    }

    /**
     * 监听剪贴板变化。
     *
     * @param context 上下文
     * @param listener 变化监听
     * @return ClipboardManager.OnPrimaryClipChangedListener
     */
    fun addClipboardListener(
        context: Context,
        listener: (String?) -> Unit
    ): ClipboardManager.OnPrimaryClipChangedListener {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipListener = ClipboardManager.OnPrimaryClipChangedListener {
            val text = getText(context)
            listener(text)
        }
        clipboard.addPrimaryClipChangedListener(clipListener)
        return clipListener
    }

    /**
     * 移除剪贴板监听。
     *
     * @param context 上下文
     * @param listener 要移除的监听
     */
    fun removeClipboardListener(
        context: Context,
        listener: ClipboardManager.OnPrimaryClipChangedListener
    ) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.removePrimaryClipChangedListener(listener)
    }

    /**
     * 获取剪贴板内容类型。
     *
     * @param context 上下文
     * @return MIME 类型列表
     */
    fun getClipMimeTypes(context: Context): Array<String>? {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClipDescription?.filterMimeTypes("*/*")
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 检查剪贴板是否有图片。
     *
     * @param context 上下文
     * @return true 表示有图片
     */
    fun hasImage(context: Context): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val description = clipboard.primaryClipDescription ?: return false
            description.hasMimeType(ClipDescription.MIMETYPE_TEXT_URILIST) ||
                    description.hasMimeType("image/*")
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 复制意图（可用于分享）。
     *
     * @param context 上下文
     * @param text 分享文本
     */
    fun shareText(context: Context, text: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, "分享到")
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }
}
