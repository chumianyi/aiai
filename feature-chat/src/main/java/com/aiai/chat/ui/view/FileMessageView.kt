/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

/**
 * 文件消息View
 *
 * 显示文件类型的聊天消息，包含文件图标、文件名、大小和下载进度。
 *
 * XML属性：
 * - fileName: 文件名
 * - fileSize: 文件大小（字节）
 * - fileType: 文件类型
 * - downloadProgress: 下载进度（0-100）
 * - isDownloaded: 是否已下载
 */
class FileMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#F5F5F5")
    }

    private val iconBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#6C63FF")
    }

    private val fileNamePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#333333")
        textSize = sp2px(14f)
    }

    private val fileSizePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#999999")
        textSize = sp2px(11f)
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.parseColor("#6C63FF")
    }

    private var fileName: String = "filename.pdf"
    private var fileSize: Long = 0
    private var fileType: String = "pdf"
    private var downloadProgress: Int = 0
    private var isDownloaded: Boolean = false

    private var cornerRadius: Float = dp2px(12f)
    private var iconSize: Float = dp2px(40f)

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.FileMessageView, defStyleAttr, 0).apply {
            fileName = getString(com.aiai.chat.R.styleable.FileMessageView_fileName) ?: fileName
            fileSize = getInt(com.aiai.chat.R.styleable.FileMessageView_fileSize, 0).toLong()
            recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = dp2px(240f).toInt()
        val height = dp2px(64f).toInt()
        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        // 绘制背景
        val bgRect = RectF(0f, 0f, w, h)
        canvas.drawRoundRect(bgRect, cornerRadius, cornerRadius, bgPaint)

        // 绘制文件图标背景
        val iconRect = RectF(dp2px(12f), (h - iconSize) / 2, dp2px(12f) + iconSize, (h + iconSize) / 2)
        canvas.drawRoundRect(iconRect, cornerRadius, cornerRadius, iconBgPaint)

        // 绘制文件类型标签（简化）
        val typeText = fileType.uppercase().take(4)
        val typeWidth = fileNamePaint.measureText(typeText)
        canvas.drawText(typeText, dp2px(12f) + iconSize / 2 - typeWidth / 2, h / 2f + sp2px(5f),
            Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE; textSize = sp2px(10f); textAlign = Paint.Align.CENTER })

        // 绘制文件名
        canvas.drawText(fileName.take(20), dp2px(64f), h / 2f - sp2px(2f), fileNamePaint)

        // 绘制文件大小
        val sizeText = formatSize(fileSize)
        canvas.drawText(sizeText, dp2px(64f), h / 2f + sp2px(14f), fileSizePaint)

        // 绘制下载进度条（如果未下载完成）
        if (!isDownloaded && downloadProgress > 0) {
            val progressWidth = (w - dp2px(24f)) * downloadProgress / 100f
            val progressRect = RectF(dp2px(12f), h - dp2px(4f), dp2px(12f) + progressWidth, h - dp2px(2f))
            canvas.drawRoundRect(progressRect, dp2px(1f), dp2px(1f), progressPaint)
        }
    }

    private fun formatSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> String.format("%.1f KB", bytes / 1024.0)
            else -> String.format("%.1f MB", bytes / (1024.0 * 1024.0))
        }
    }

    /**
     * 设置文件信息
     */
    fun setFileInfo(name: String, size: Long, type: String = "file") {
        fileName = name
        fileSize = size
        fileType = type
        invalidate()
    }

    /**
     * 设置下载进度
     */
    fun setDownloadProgress(progress: Int) {
        downloadProgress = progress.coerceIn(0, 100)
        isDownloaded = progress >= 100
        invalidate()
    }

    private fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }

    private fun sp2px(sp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, resources.displayMetrics)
    }
}
