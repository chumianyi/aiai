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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView

/**
 * TextView 工具类。
 */
object TextViewUtil {

    /** 设置部分文字颜色。 */
    fun setTextColor(textView: TextView, fullText: String, start: Int, end: Int, color: Int) {
        val spannable = SpannableString(fullText)
        spannable.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    /** 设置部分文字粗体。 */
    fun setTextBold(textView: TextView, fullText: String, start: Int, end: Int) {
        val spannable = SpannableString(fullText)
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    /** 设置部分文字颜色和粗体。 */
    fun setTextColorAndBold(textView: TextView, fullText: String, start: Int, end: Int, color: Int) {
        val spannable = SpannableString(fullText)
        spannable.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.text = spannable
    }

    /** 设置文字大小（sp）。 */
    fun setTextSizeSp(textView: TextView, size: Float) {
        textView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, size)
    }
}
