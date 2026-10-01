/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.NotificationDao
import com.aiai.data.entity.NotificationEntity

/**
 * 通知管理器。
 *
 * 管理应用通知的显示、标记已读等功能。
 */
class NotificationManager(
    private val notificationDao: NotificationDao,
) {

    companion object {
        private const val TAG = "NotificationManager"

        const val TYPE_INFO = "info"
        const val TYPE_WARNING = "warning"
        const val TYPE_ERROR = "error"
        const val TYPE_SUCCESS = "success"
    }

    /**
     * 获取所有通知。
     */
    fun getAllNotifications(): List<NotificationEntity> {
        return notificationDao.getAllNotificationsSync()
    }

    /**
     * 获取未读通知。
     */
    fun getUnreadNotifications(): List<NotificationEntity> {
        return notificationDao.getUnreadNotificationsSync()
    }

    /**
     * 获取未读通知数。
     */
    fun getUnreadCount(): Int {
        return notificationDao.getUnreadCountSync()
    }

    /**
     * 标记通知为已读。
     */
    suspend fun markAsRead(notificationId: String) {
        notificationDao.markAsRead(notificationId)
        Log.d(TAG, "Marked as read: $notificationId")
    }

    /**
     * 标记所有通知为已读。
     */
    suspend fun markAllAsRead() {
        notificationDao.markAllAsRead()
        Log.d(TAG, "All notifications marked as read")
    }

    /**
     * 删除通知。
     */
    suspend fun deleteNotification(notificationId: String) {
        notificationDao.deleteById(notificationId)
        Log.d(TAG, "Deleted notification: $notificationId")
    }

    /**
     * 清除所有通知。
     */
    suspend fun clearAll() {
        notificationDao.deleteAll()
        Log.d(TAG, "All notifications cleared")
    }

    /**
     * 按类型获取通知。
     */
    fun getNotificationsByType(type: String): List<NotificationEntity> {
        return notificationDao.getByTypeSync(type)
    }

    /**
     * 创建新通知。
     */
    suspend fun createNotification(
        title: String,
        content: String,
        type: String = TYPE_INFO,
    ): Long {
        val notification = NotificationEntity(
            id = java.util.UUID.randomUUID().toString(),
            title = title,
            content = content,
            type = type,
            isRead = false,
            createdAt = System.currentTimeMillis(),
            actionData = "{}",
        )
        return notificationDao.insert(notification)
    }
}
