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
import android.content.Intent
import android.util.Log

/**
 * 分享管理器。
 *
 * 提供文本分享、图片分享、文件分享等功能。
 */
object ShareManager {

    private const val TAG = "ShareManager"

    /**
     * 分享类型枚举。
     */
    enum class ShareType {
        /** 文本 */
        TEXT,
        /** 图片 */
        IMAGE,
        /** 文件 */
        FILE,
        /** 链接 */
        LINK
    }

    /**
     * 分享结果回调。
     */
    interface ShareCallback {
        /**
         * 分享成功。
         */
        fun onSuccess()

        /**
         * 分享失败。
         *
         * @param message 错误信息
         */
        fun onFailed(message: String)

        /**
         * 分享取消。
         */
        fun onCancel() {}
    }

    /**
     * 分享文本。
     *
     * @param context 上下文
     * @param text 分享文本
     * @param title 分享标题
     */
    fun shareText(context: Context, text: String, title: String = "分享") {
        Log.d(TAG, "Sharing text: ${text.take(50)}...")
        try {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            context.startActivity(Intent.createChooser(intent, title))
        } catch (e: Exception) {
            Log.e(TAG, "Share text failed", e)
        }
    }

    /**
     * 分享链接。
     *
     * @param context 上下文
     * @param url 链接地址
     * @param title 分享标题
     * @param description 描述
     */
    fun shareLink(context: Context, url: String, title: String = "", description: String = "") {
        Log.d(TAG, "Sharing link: $url")
        val text = buildString {
            if (title.isNotBlank()) appendLine(title)
            if (description.isNotBlank()) appendLine(description)
            append(url)
        }
        shareText(context, text)
    }

    /**
     * 分享应用。
     *
     * @param context 上下文
     */
    fun shareApp(context: Context) {
        val appName = "爱Ai"
        val url = "https://aiai.app"
        val description = "推荐你使用$appName - 智能对话助手，随时随地与AI交流"
        shareLink(context, url, appName, description)
    }

    /**
     * 分享对话记录。
     *
     * @param context 上下文
     * @param messages 对话消息列表
     */
    fun shareConversation(context: Context, messages: List<Pair<String, String>>) {
        Log.d(TAG, "Sharing conversation: ${messages.size} messages")
        val text = buildString {
            appendLine("=== 对话记录 ===")
            messages.forEach { (role, content) ->
                appendLine("【$role】")
                appendLine(content)
                appendLine()
            }
        }
        shareText(context, text, "分享对话")
    }
}
