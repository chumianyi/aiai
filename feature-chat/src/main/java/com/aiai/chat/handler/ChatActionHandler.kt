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
package com.aiai.chat.handler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.aiai.chat.data.model.ChatMessage

/**
 * 聊天操作处理器
 *
 * 处理消息的各种操作：复制、转发、收藏、删除、重新生成等。
 */
class ChatActionHandler(private val context: Context) {

    /**
     * 复制消息内容到剪贴板
     */
    fun copyMessage(message: ChatMessage) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("message", message.content)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }

    /**
     * 转发消息
     */
    fun forwardMessage(message: ChatMessage, targetConversationId: String) {
        // 实际项目中会调用Repository插入新消息
        Toast.makeText(context, "已转发", Toast.LENGTH_SHORT).show()
    }

    /**
     * 收藏/取消收藏消息
     */
    fun toggleFavorite(message: ChatMessage): Boolean {
        val newFav = !message.isFavorite
        // 实际项目中更新数据库
        Toast.makeText(
            context,
            if (newFav) "已收藏" else "已取消收藏",
            Toast.LENGTH_SHORT
        ).show()
        return newFav
    }

    /**
     * 分享消息
     */
    fun shareMessage(message: ChatMessage) {
        val sendIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(android.content.Intent.EXTRA_TEXT, message.content)
            type = "text/plain"
        }
        context.startActivity(android.content.Intent.createChooser(sendIntent, "分享消息"))
    }

    /**
     * 重新生成AI回复
     */
    fun regenerateResponse(messageId: String) {
        // 实际项目中调用ChatManager重新发送
        Toast.makeText(context, "正在重新生成...", Toast.LENGTH_SHORT).show()
    }

    /**
     * 引用消息
     */
    fun quoteMessage(message: ChatMessage): String {
        val preview = if (message.content.length > 50) {
            message.content.take(50) + "..."
        } else {
            message.content
        }
        return "[引用] $preview"
    }

    /**
     * 删除消息
     */
    fun deleteMessage(messageId: String) {
        // 实际项目中调用Repository删除
        Toast.makeText(context, "消息已删除", Toast.LENGTH_SHORT).show()
    }
}
