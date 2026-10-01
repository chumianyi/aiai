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
import androidx.appcompat.widget.AppCompatTextView
import com.aiai.chat.renderer.MarkdownRenderer

/**
 * Markdown渲染TextView
 *
 * 基于Markwon库实现Markdown文本渲染，支持：
 * - 代码块语法高亮
 * - 表格渲染
 * - 任务列表
 * - 图片加载
 * - 链接识别
 * - 删除线
 */
class MarkdownTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var markdownRenderer: MarkdownRenderer? = null

    init {
        initRenderer()
    }

    private fun initRenderer() {
        markdownRenderer = MarkdownRenderer(context)
    }

    /**
     * 设置Markdown文本并渲染
     */
    fun setMarkdown(text: String) {
        markdownRenderer?.render(this, text)
    }

    /**
     * 追加流式Markdown内容（用于流式输出）
     */
    fun appendStream(delta: String) {
        val current = text?.toString() ?: ""
        setMarkdown(current + delta)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        markdownRenderer?.cleanup()
    }
}
