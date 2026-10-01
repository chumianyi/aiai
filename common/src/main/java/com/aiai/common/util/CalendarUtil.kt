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
package com.aiai.common.util

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log

/**
 * 日历工具类。
 *
 * 提供事件添加、查询、删除、提醒等功能。
 */
object CalendarUtil {

    private const val TAG = "CalendarUtil"

    /**
     * 日历事件数据模型。
     */
    data class CalendarEvent(
        val id: Long,
        val title: String,
        val description: String,
        val startTime: Long,
        val endTime: Long,
        val location: String,
        val allDay: Boolean
    )

    /**
     * 添加日历事件。
     *
     * @param context 上下文
     * @param title 事件标题
     * @param description 事件描述
     * @param startTime 开始时间戳
     * @param endTime 结束时间戳
     * @param location 地点
     * @param allDay 是否全天
     * @param reminderMinutes 提醒提前分钟数
     * @return 事件ID
     */
    fun addEvent(
        context: Context,
        title: String,
        description: String = "",
        startTime: Long,
        endTime: Long,
        location: String = "",
        allDay: Boolean = false,
        reminderMinutes: Int = 15
    ): Long {
        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.DTSTART, startTime)
            put(CalendarContract.Events.DTEND, endTime)
            put(CalendarContract.Events.EVENT_LOCATION, location)
            put(CalendarContract.Events.ALL_DAY, if (allDay) 1 else 0)
            put(CalendarContract.Events.HAS_ALARM, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(CalendarContract.Events.CONTENT_URI, values)
        val eventId = uri?.lastPathSegment?.toLong() ?: -1

        // 添加提醒
        if (eventId != -1L) {
            val reminderValues = ContentValues().apply {
                put(CalendarContract.Reminders.EVENT_ID, eventId)
                put(CalendarContract.Reminders.MINUTES, reminderMinutes)
                put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
            }
            resolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
        }

        Log.d(TAG, "Event added: $title, id=$eventId")
        return eventId
    }

    /**
     * 查询日历事件。
     *
     * @param context 上下文
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 事件列表
     */
    fun queryEvents(
        context: Context,
        startDate: Long = System.currentTimeMillis(),
        endDate: Long = startDate + 7 * 24 * 60 * 60 * 1000L
    ): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            CalendarContract.Events.CONTENT_URI,
            arrayOf(
                CalendarContract.Events._ID,
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DTEND,
                CalendarContract.Events.EVENT_LOCATION,
                CalendarContract.Events.ALL_DAY
            ),
            "${CalendarContract.Events.DTSTART} >= ? AND ${CalendarContract.Events.DTEND} <= ?",
            arrayOf(startDate.toString(), endDate.toString()),
            "${CalendarContract.Events.DTSTART} ASC"
        )

        cursor?.use {
            val idIndex = it.getColumnIndex(CalendarContract.Events._ID)
            val titleIndex = it.getColumnIndex(CalendarContract.Events.TITLE)
            val descIndex = it.getColumnIndex(CalendarContract.Events.DESCRIPTION)
            val startIndex = it.getColumnIndex(CalendarContract.Events.DTSTART)
            val endIndex = it.getColumnIndex(CalendarContract.Events.DTEND)
            val locIndex = it.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)
            val allDayIndex = it.getColumnIndex(CalendarContract.Events.ALL_DAY)

            while (it.moveToNext()) {
                events.add(
                    CalendarEvent(
                        id = it.getLong(idIndex),
                        title = it.getString(titleIndex) ?: "",
                        description = it.getString(descIndex) ?: "",
                        startTime = it.getLong(startIndex),
                        endTime = it.getLong(endIndex),
                        location = it.getString(locIndex) ?: "",
                        allDay = it.getInt(allDayIndex) == 1
                    )
                )
            }
        }

        return events
    }

    /**
     * 删除日历事件。
     *
     * @param context 上下文
     * @param eventId 事件ID
     * @return true 表示成功
     */
    fun deleteEvent(context: Context, eventId: Long): Boolean {
        return try {
            val resolver = context.contentResolver
            resolver.delete(
                CalendarContract.Events.CONTENT_URI,
                "${CalendarContract.Events._ID} = ?",
                arrayOf(eventId.toString())
            ) > 0
        } catch (e: Exception) {
            Log.e(TAG, "Delete event failed", e)
            false
        }
    }

    /**
     * 检查日历权限。
     *
     * @param context 上下文
     * @return true 表示有权限
     */
    fun hasCalendarPermission(context: Context): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.READ_CALENDAR) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 获取今天的事件。
     *
     * @param context 上下文
     * @return 今天的事件列表
     */
    fun getTodayEvents(context: Context): List<CalendarEvent> {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis
        val endOfDay = startOfDay + 24 * 60 * 60 * 1000L - 1

        return queryEvents(context, startOfDay, endOfDay)
    }

    /**
     * 获取即将到来的事件。
     *
     * @param context 上下文
     * @param hours 未来小时数
     * @return 事件列表
     */
    fun getUpcomingEvents(context: Context, hours: Int = 24): List<CalendarEvent> {
        val now = System.currentTimeMillis()
        val future = now + hours * 60 * 60 * 1000L
        return queryEvents(context, now, future)
    }
}
