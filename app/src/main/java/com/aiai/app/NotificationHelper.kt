/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aiai.core.util.LogUtil

/**
 * 通知帮助类
 *
 * - 创建通知渠道
 * - 发送聊天回复通知
 * - 发送后台服务通知
 */
object NotificationHelper {

    private const val TAG = "NotificationHelper"

    /**
     * 创建所有通知渠道（Android O+）
     */
    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val chatChannel = NotificationChannel(
            AppConstants.NOTIFICATION_CHANNEL_CHAT,
            context.getString(R.string.notification_channel_chat_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_chat_desc)
            enableVibration(true)
            enableLights(true)
        }

        val defaultChannel = NotificationChannel(
            AppConstants.NOTIFICATION_CHANNEL_DEFAULT,
            context.getString(R.string.notification_channel_default_name),
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = context.getString(R.string.notification_channel_default_desc)
        }

        manager.createNotificationChannels(listOf(chatChannel, defaultChannel))
        LogUtil.d(TAG, "Notification channels created")
    }

    /**
     * 发送聊天消息通知
     */
    fun sendChatNotification(
        context: Context,
        title: String,
        content: String,
        conversationId: Long
    ) {
        val intent = Intent(context, AppEntryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, AppConstants.NOTIFICATION_CHANNEL_CHAT)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(AppConstants.NOTIFICATION_ID_CHAT + conversationId.toInt(), notification)
    }

    /**
     * 取消通知
     */
    fun cancelNotification(context: Context, id: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(id)
    }

    /**
     * 取消所有通知
     */
    fun cancelAll(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }
}
