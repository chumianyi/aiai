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
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * 自适应高度输入框
 *
 * 随输入内容自动调整高度，最大不超过maxLines设定的行数。
 * 超过最大高度后滚动显示。
 *
 * XML属性：
 * - maxLines: 最大行数
 * - minLines: 最小行数
 */
class AutoResizeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var maxCollapsedLines: Int = 4

    init {
        context.obtainStyledAttributes(attrs, com.aiai.chat.R.styleable.AutoResizeEditText, defStyleAttr, 0).apply {
            maxCollapsedLines = getInt(com.aiai.chat.R.styleable.AutoResizeEditText_maxLines, 4)
            recycle()
        }
        setMaxLines(maxCollapsedLines)
        isSingleLine = false
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        // 触发重新布局以适应内容高度
        requestLayout()
    }

    /**
     * 设置最大行数
     */
    fun setMaxCollapsedLines(lines: Int) {
        maxCollapsedLines = lines
        setMaxLines(lines)
    }

    /**
     * 获取输入的纯文本
     */
    fun getInputText(): String = text?.toString()?.trim() ?: ""

    /**
     * 清空输入
     */
    fun clearInput() {
        setText("")
    }

    /**
     * 是否有输入内容
     */
    fun hasText(): Boolean = !text.isNullOrBlank()
}
