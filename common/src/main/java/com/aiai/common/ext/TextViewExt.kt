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

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.FontRes

/**
 * TextView 扩展函数集合。
 *
 * 提供富文本、可点击 Span、自动调整大小、跑马灯、复制功能等
 * 常用 TextView 操作。
 */

// region 富文本

/**
 * 设置富文本。
 *
 * @param builder 构建器函数
 */
fun TextView.spannable(builder: SpannableStringBuilder.() -> Unit) {
    val ssb = SpannableStringBuilder()
    builder(ssb)
    text = ssb
}

/**
 * 追加文本并设置颜色。
 *
 * @param text 文本内容
 * @param color 颜色值
 */
fun SpannableStringBuilder.appendColored(text: String, @ColorInt color: Int) {
    val start = length
    append(text)
    setSpan(ForegroundColorSpan(color), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/**
 * 追加文本并设置加粗。
 *
 * @param text 文本内容
 */
fun SpannableStringBuilder.appendBold(text: String) {
    val start = length
    append(text)
    setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/**
 * 追加文本并设置斜体。
 *
 * @param text 文本内容
 */
fun SpannableStringBuilder.appendItalic(text: String) {
    val start = length
    append(text)
    setSpan(StyleSpan(android.graphics.Typeface.ITALIC), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/**
 * 追加文本并设置下划线。
 *
 * @param text 文本内容
 */
fun SpannableStringBuilder.appendUnderline(text: String) {
    val start = length
    append(text)
    setSpan(UnderlineSpan(), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/**
 * 追加文本并设置删除线。
 *
 * @param text 文本内容
 */
fun SpannableStringBuilder.appendStrikethrough(text: String) {
    val start = length
    append(text)
    setSpan(StrikethroughSpan(), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

/**
 * 追加文本并设置相对大小。
 *
 * @param text 文本内容
 * @param proportion 比例（1.0 为正常大小）
 */
fun SpannableStringBuilder.appendSize(text: String, proportion: Float) {
    val start = length
    append(text)
    setSpan(RelativeSizeSpan(proportion), start, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}

// endregion

// region 可点击 Span

/**
 * 设置可点击文本。
 *
 * @param text 可点击文本
 * @param color 文本颜色
 * @param onClick 点击回调
 */
fun TextView.setClickableText(
    text: String,
    @ColorInt color: Int = Color.BLUE,
    onClick: () -> Unit
) {
    val spannable = SpannableString(text)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }

        override fun updateDrawState(ds: android.text.TextPaint) {
            ds.color = color
            ds.isUnderlineText = true
        }
    }
    spannable.setSpan(clickableSpan, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}

/**
 * 在指定位置设置可点击 Span。
 *
 * @param fullText 完整文本
 * @param clickablePart 可点击部分文本
 * @param color 颜色
 * @param onClick 点击回调
 */
fun TextView.setClickablePart(
    fullText: String,
    clickablePart: String,
    @ColorInt color: Int = Color.BLUE,
    onClick: () -> Unit
) {
    val start = fullText.indexOf(clickablePart)
    if (start == -1) {
        text = fullText
        return
    }
    val end = start + clickablePart.length

    val spannable = SpannableString(fullText)
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick()
        }

        override fun updateDrawState(ds: android.text.TextPaint) {
            ds.color = color
            ds.isUnderlineText = true
        }
    }
    spannable.setSpan(clickableSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannable
    movementMethod = LinkMovementMethod.getInstance()
}

// endregion

// region 跑马灯效果

/**
 * 启用跑马灯效果。
 *
 * @param repeatLimit 重复次数，-1 表示无限
 */
fun TextView.enableMarquee(repeatLimit: Int = -1) {
    ellipsize = android.text.TextUtils.TruncateAt.MARQUEE
    marqueeRepeatLimit = repeatLimit
    isSingleLine = true
    isFocusable = true
    isFocusableInTouchMode = true
}

/**
 * 开始跑马灯。
 */
fun TextView.startMarquee() {
    isSelected = true
}

/**
 * 停止跑马灯。
 */
fun TextView.stopMarquee() {
    isSelected = false
}

// endregion

// region 自动调整大小

/**
 * 启用自动调整字体大小。
 *
 * @param minSize 最小字号
 * @param maxSize 最大字号
 * @param stepGranularity 步进
 */
fun TextView.setAutoSizeText(
    minSize: Float,
    maxSize: Float,
    stepGranularity: Float = 1f
) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        setAutoSizeTextTypeUniformWithConfiguration(
            minSize.toInt(),
            maxSize.toInt(),
            stepGranularity.toInt(),
            android.util.TypedValue.COMPLEX_UNIT_PX
        )
    }
}

// endregion

// region 复制功能

/**
 * 长按复制文本。
 */
fun TextView.enableLongPressCopy() {
    setOnLongClickListener {
        val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                as? android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("text", text)
        clipboard?.setPrimaryClip(clip)
        true
    }
}

/**
 * 复制文本到剪贴板。
 */
fun TextView.copyToClipboard() {
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as? android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("text", text)
    clipboard?.setPrimaryClip(clip)
}

// endregion

// region 其他

/**
 * 设置字体。
 *
 * @param fontRes 字体资源
 */
fun TextView.setTypefaceCompat(@FontRes fontRes: Int) {
    try {
        val typeface = androidx.core.content.res.ResourcesCompat.getFont(context, fontRes)
        setTypeface(typeface)
    } catch (e: Exception) {
        // 忽略字体加载失败
    }
}

/**
 * 是否有文本内容。
 */
fun TextView.hasText(): Boolean = !text.isNullOrEmpty()

/**
 * 是否为空白文本。
 */
fun TextView.isBlank(): Boolean = text?.isBlank() ?: true

/**
 * 获取文本长度。
 */
fun TextView.textLength(): Int = text?.length ?: 0

/**
 * 清空文本。
 */
fun TextView.clearText() {
    text = ""
}

/**
 * 设置文本并监听变化。
 *
 * @param text 文本内容
 * @param animate 是否动画过渡
 */
fun TextView.setTextAnimated(text: String, animate: Boolean = true) {
    if (animate) {
        alpha = 0f
        setText(text)
        animate().alpha(1f).setDuration(200).start()
    } else {
        setText(text)
    }
}

/**
 * 添加前缀。
 *
 * @param prefix 前缀文本
 * @param color 前缀颜色
 */
fun TextView.addPrefix(prefix: String, @ColorInt color: Int = Color.GRAY) {
    val spannable = SpannableStringBuilder()
    spannable.appendColored(prefix, color)
    spannable.append(text)
    text = spannable
}

/**
 * 添加后缀。
 *
 * @param suffix 后缀文本
 * @param color 后缀颜色
 */
fun TextView.addSuffix(suffix: String, @ColorInt color: Int = Color.GRAY) {
    val spannable = SpannableStringBuilder(text)
    spannable.appendColored(suffix, color)
    text = spannable
}

/**
 * 设置行间距。
 *
 * @param spacing 行间距倍数
 */
fun TextView.setLineSpacingMultiplier(spacing: Float) {
    setLineSpacing(0f, spacing)
}

/**
 * 设置首行缩进。
 *
 * @param indent 缩进像素值
 */
fun TextView.setFirstLineIndent(indent: Float) {
    val extra = android.text.style.LeadingMarginSpan.Standard(indent.toInt(), 0)
    val spannable = SpannableString.valueOf(text)
    spannable.setSpan(extra, 0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    text = spannable
}

/**
 * 文本渐隐效果（超出部分渐变淡出）。
 *
 * @param fadeLength 渐变长度
 */
fun TextView.setFadeEffect(fadeLength: Int = 50) {
    // 简化实现，实际可通过 ForegroundColorSpan 渐变实现
    ellipsize = android.text.TextUtils.TruncateAt.END
}

/**
 * 数字滚动动画。
 *
 * @param targetNumber 目标数字
 * @param duration 动画时长
 */
fun TextView.animateNumber(targetNumber: Int, duration: Long = 1000) {
    val startNumber = text.toString().toIntOrNull() ?: 0
    val animator = android.animation.ValueAnimator.ofInt(startNumber, targetNumber)
    animator.duration = duration
    animator.addUpdateListener { animation ->
        text = animation.animatedValue.toString()
    }
    animator.start()
}

/**
 * 多行文本收起/展开。
 *
 * @param collapsedLines 收起时显示的行数
 * @param expandText 展开按钮文本
 * @param collapseText 收起按钮文本
 */
fun TextView.setupCollapsible(
    collapsedLines: Int = 3,
    expandText: String = "展开",
    collapseText: String = "收起"
) {
    var isExpanded = false
    maxLines = collapsedLines
    ellipsize = android.text.TextUtils.TruncateAt.END

    setOnClickListener {
        isExpanded = !isExpanded
        maxLines = if (isExpanded) Int.MAX_VALUE else collapsedLines
    }
}

/**
 * 设置文本是否可复制。
 *
 * @param selectable 是否可选
 */
fun TextView.setTextSelectable(selectable: Boolean) {
    setTextIsSelectable(selectable)
}

/**
 * 计算文本宽度。
 *
 * @return 文本宽度像素值
 */
fun TextView.measureTextWidth(): Float {
    return paint.measureText(text.toString())
}

/**
 * 计算文本高度。
 *
 * @return 文本高度像素值
 */
fun TextView.measureTextHeight(): Float {
    return paint.descent() - paint.ascent()
}

// endregion
