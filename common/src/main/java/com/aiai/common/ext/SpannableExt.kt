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

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
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

/**
 * Spannable 相关扩展函数集合。
 *
 * 提供富文本构建、颜色/大小/点击/下划线/删除线等能力。
 */

// region 构建器

/** 富文本构建 DSL。 */
class SpannableBuilder {
    private val builder = SpannableStringBuilder()

    /** 追加普通文本。 */
    fun text(text: String): SpannableBuilder {
        builder.append(text)
        return this
    }

    /** 追加带颜色的文本。 */
    fun colored(text: String, @ColorInt color: Int): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        builder.setSpan(ForegroundColorSpan(color), start, builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 追加带大小的文本。 */
    fun sized(text: String, proportion: Float): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        builder.setSpan(RelativeSizeSpan(proportion), start, builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 追加粗体文本。 */
    fun bold(text: String): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        builder.setSpan(StyleSpan(Typeface.BOLD), start, builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 追加下划线文本。 */
    fun underline(text: String): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        builder.setSpan(UnderlineSpan(), start, builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 追加删除线文本。 */
    fun strikethrough(text: String): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        builder.setSpan(StrikethroughSpan(), start, builder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 追加可点击文本。 */
    fun clickable(text: String, @ColorInt color: Int, onClick: (View) -> Unit): SpannableBuilder {
        val start = builder.length
        builder.append(text)
        val span = object : ClickableSpan() {
            override fun onClick(widget: View) = onClick(widget)
            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = false
            }
        }
        builder.setSpan(span, start, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return this
    }

    /** 构建为 Spannable。 */
    fun build(): Spannable = builder
}

/** 启动富文本构建 DSL。 */
fun buildSpannable(block: SpannableBuilder.() -> Unit): Spannable {
    return SpannableBuilder().apply(block).build()
}

// endregion

// region TextView 快捷设置

/** 给 TextView 设置富文本（自动启用点击）。 */
fun TextView.setSpannable(block: SpannableBuilder.() -> Unit) {
    text = buildSpannable(block)
    movementMethod = LinkMovementMethod.getInstance()
}

/** 给 TextView 设置局部颜色。 */
fun TextView.setPartialColor(text: String, start: Int, end: Int, @ColorInt color: Int) {
    val sp = SpannableStringBuilder(text)
    sp.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = sp
}

/** 给 TextView 设置局部粗体。 */
fun TextView.setPartialBold(text: String, start: Int, end: Int) {
    val sp = SpannableStringBuilder(text)
    sp.setSpan(StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = sp
}

/** 给 TextView 设置局部下划线。 */
fun TextView.setPartialUnderline(text: String, start: Int, end: Int) {
    val sp = SpannableStringBuilder(text)
    sp.setSpan(UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = sp
}

/** 给 TextView 设置局部删除线。 */
fun TextView.setPartialStrike(text: String, start: Int, end: Int) {
    val sp = SpannableStringBuilder(text)
    sp.setSpan(StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.text = sp
}

// endregion
