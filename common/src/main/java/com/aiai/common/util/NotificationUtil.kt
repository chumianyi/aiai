/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat

/**
 * 通知工具类。
 *
 * 提供渠道创建、通知发送、进度通知、大文本通知、图片通知等功能。
 */
object NotificationUtil {

    private const val CHANNEL_ID_DEFAULT = "default"
    private const val CHANNEL_NAME_DEFAULT = "默认通知"
    private const val CHANNEL_ID_CHAT = "chat"
    private const val CHANNEL_NAME_CHAT = "聊天消息"
    private const val CHANNEL_ID_SERVICE = "service"
    private const val CHANNEL_NAME_SERVICE = "后台服务"
    private const val CHANNEL_ID_PROGRESS = "progress"
    private const val CHANNEL_NAME_PROGRESS = "进度通知"

    /**
     * 初始化通知渠道。
     *
     * @param context 上下文
     */
    fun initChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

            val channels = listOf(
                NotificationChannel(CHANNEL_ID_DEFAULT, CHANNEL_NAME_DEFAULT, NotificationManager.IMPORTANCE_DEFAULT),
                NotificationChannel(CHANNEL_ID_CHAT, CHANNEL_NAME_CHAT, NotificationManager.IMPORTANCE_HIGH),
                NotificationChannel(CHANNEL_ID_SERVICE, CHANNEL_NAME_SERVICE, NotificationManager.IMPORTANCE_LOW),
                NotificationChannel(CHANNEL_ID_PROGRESS, CHANNEL_NAME_PROGRESS, NotificationManager.IMPORTANCE_LOW)
            )

            notificationManager.createNotificationChannels(channels)
        }
    }

    /**
     * 创建自定义通知渠道。
     *
     * @param context 上下文
     * @param channelId 渠道ID
     * @param channelName 渠道名称
     * @param importance 重要性级别
     * @param description 渠道描述
     */
    fun createChannel(
        context: Context,
        channelId: String,
        channelName: String,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        description: String = ""
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                this.description = description
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * 发送普通通知。
     *
     * @param context 上下文
     * @param title 通知标题
     * @param content 通知内容
     * @param notificationId 通知ID
     * @param channelId 渠道ID
     * @param smallIcon 小图标
     * @param intent 点击意图
     */
    fun sendNotification(
        context: Context,
        title: String,
        content: String,
        notificationId: Int = System.currentTimeMillis().toInt(),
        channelId: String = CHANNEL_ID_DEFAULT,
        smallIcon: Int = context.applicationInfo.icon,
        intent: Intent? = null
    ) {
        val pendingIntent = intent?.let {
            PendingIntent.getActivity(
                context, 0, it,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    /**
     * 发送大文本通知。
     *
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param bigText 大文本内容
     * @param notificationId 通知ID
     */
    fun sendBigTextNotification(
        context: Context,
        title: String,
        content: String,
        bigText: String,
        notificationId: Int = System.currentTimeMillis().toInt(),
        channelId: String = CHANNEL_ID_CHAT
    ) {
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(bigText)
            .setBigContentTitle(title)
            .setSummaryText(content)

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    /**
     * 发送图片通知。
     *
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param bitmap 图片
     * @param notificationId 通知ID
     */
    fun sendBigPictureNotification(
        context: Context,
        title: String,
        content: String,
        bitmap: Bitmap,
        notificationId: Int = System.currentTimeMillis().toInt()
    ) {
        val bigPictureStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .setBigContentTitle(title)
            .setSummaryText(content)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_CHAT)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(bigPictureStyle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    /**
     * 发送进度通知。
     *
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param max 最大进度
     * @param progress 当前进度
     * @param notificationId 通知ID
     * @param indeterminate 是否不确定进度
     */
    fun sendProgressNotification(
        context: Context,
        title: String,
        content: String,
        max: Int = 100,
        progress: Int = 0,
        notificationId: Int = 1001,
        indeterminate: Boolean = false
    ) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_PROGRESS)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(content)
            .setProgress(max, progress, indeterminate)
            .setOngoing(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    /**
     * 更新进度通知。
     *
     * @param context 上下文
     * @param progress 当前进度
     * @param content 内容
     * @param notificationId 通知ID
     */
    fun updateProgressNotification(
        context: Context,
        progress: Int,
        content: String = "下载中... $progress%",
        notificationId: Int = 1001
    ) {
        sendProgressNotification(
            context = context,
            title = "下载进度",
            content = content,
            progress = progress,
            notificationId = notificationId
        )
    }

    /**
     * 取消通知。
     *
     * @param context 上下文
     * @param notificationId 通知ID
     */
    fun cancelNotification(context: Context, notificationId: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.cancel(notificationId)
    }

    /**
     * 取消所有通知。
     *
     * @param context 上下文
     */
    fun cancelAllNotifications(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager
        notificationManager.cancelAll()
    }

    /**
     * 发送前台服务通知。
     *
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param notificationId 通知ID
     * @return Notification 对象
     */
    fun createForegroundNotification(
        context: Context,
        title: String,
        content: String,
        notificationId: Int = 1
    ): Notification {
        return NotificationCompat.Builder(context, CHANNEL_ID_SERVICE)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    /**
     * 检查通知权限是否已授予。
     *
     * @param context 上下文
     * @return true 表示已授予
     */
    fun hasNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) ==
                    android.content.pm.PackageManager.PERMISSION_GRANTED
        }
        return true
    }

    /**
     * 检查渠道是否已被禁用。
     *
     * @param context 上下文
     * @param channelId 渠道ID
     * @return true 表示已禁用
     */
    fun isChannelBlocked(context: Context, channelId: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager
            val channel = notificationManager.getNotificationChannel(channelId)
            return channel?.importance == NotificationManager.IMPORTANCE_NONE
        }
        return false
    }
}
