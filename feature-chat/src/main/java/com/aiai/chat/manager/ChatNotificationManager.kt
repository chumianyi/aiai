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
package com.aiai.chat.manager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aiai.chat.R
import com.aiai.chat.data.model.ChatMessage

/**
 * 聊天通知管理器
 *
 * 管理AI回复完成等事件的通知推送。
 * 支持通知渠道管理、点击跳转、取消通知。
 */
class ChatNotificationManager(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "chat_messages"
        private const val CHANNEL_NAME = "聊天消息"
        private const val NOTIFICATION_ID_BASE = 10000
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    /**
     * 创建通知渠道（Android O+）
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "AI回复完成通知"
                enableVibration(true)
                enableLights(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 显示AI回复完成通知
     */
    fun showAiReplyNotification(
        conversationId: String,
        conversationTitle: String,
        message: ChatMessage,
        clickIntent: Intent? = null
    ) {
        val notificationId = NOTIFICATION_ID_BASE + conversationId.hashCode()

        val contentText = if (message.content.length > 100) {
            message.content.take(100) + "..."
        } else {
            message.content
        }

        val pendingIntent = clickIntent?.let {
            PendingIntent.getActivity(
                context,
                0,
                it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_chat)
            .setContentTitle(conversationTitle)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.content))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(notificationId, notification)
    }

    /**
     * 取消指定会话的通知
     */
    fun cancelNotification(conversationId: String) {
        val notificationId = NOTIFICATION_ID_BASE + conversationId.hashCode()
        notificationManager.cancel(notificationId)
    }

    /**
     * 取消所有通知
     */
    fun cancelAll() {
        notificationManager.cancelAll()
    }
}
