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
package com.aiai.chat.renderer

import android.content.Context
import android.widget.TextView
import io.noties.markwon.Markwon
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tasklist.TaskListPlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin
import io.noties.markwon.linkify.LinkifyPlugin
import io.noties.markwon.syntax.Prism4jTheme
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.markwon.tasklist.TaskListPlugin as MarkwonTaskListPlugin

/**
 * Markdown渲染器配置
 *
 * 基于Markwon库配置Markdown渲染，支持：
 * - 代码块语法高亮
 * - 图片加载
 * - 表格渲染
 * - 任务列表
 * - 删除线
 * - 链接自动识别
 * - HTML子集解析
 */
class MarkdownRenderer(private val context: Context) {

    private var markwon: Markwon? = null

    init {
        initMarkwon()
    }

    private fun initMarkwon() {
        markwon = Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())
            .usePlugin(ImagesPlugin.create())
            .usePlugin(LinkifyPlugin.create())
            .usePlugin(StrikethroughPlugin.create())
            .usePlugin(TaskListPlugin.create(context))
            .usePlugin(SyntaxHighlightPlugin.create(Prism4jTheme.createDefault()))
            .build()
    }

    /**
     * 渲染Markdown文本到TextView
     */
    fun render(textView: TextView, markdown: String) {
        markwon?.setMarkdown(textView, markdown)
    }

    /**
     * 渲染Markdown并返回CharSequence
     */
    fun renderMarkdown(markdown: String): CharSequence {
        return markwon?.toMarkdown(markdown) ?: markdown
    }

    /**
     * 清理资源
     */
    fun cleanup() {
        markwon = null
    }
}
