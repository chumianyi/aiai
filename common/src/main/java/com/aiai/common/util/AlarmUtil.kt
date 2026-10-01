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

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.util.Log

/**
 * 闹钟工具类。
 *
 * 提供一次性闹钟、重复闹钟、精确闹钟、闹钟取消等功能。
 */
object AlarmUtil {

    private const val TAG = "AlarmUtil"
    private const val REQUEST_CODE_DEFAULT = 1000

    /**
     * 设置一次性闹钟。
     *
     * @param context 上下文
     * @param triggerAtMillis 触发时间（毫秒）
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setOneShotAlarm(
        context: Context,
        triggerAtMillis: Long,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
        }
        Log.d(TAG, "One-shot alarm set at $triggerAtMillis")
    }

    /**
     * 设置重复闹钟。
     *
     * @param context 上下文
     * @param triggerAtMillis 首次触发时间
     * @param intervalMillis 间隔时间
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setRepeatingAlarm(
        context: Context,
        triggerAtMillis: Long,
        intervalMillis: Long,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            intervalMillis,
            pendingIntent
        )
        Log.d(TAG, "Repeating alarm set, interval: $intervalMillis ms")
    }

    /**
     * 设置精确重复闹钟。
     *
     * @param context 上下文
     * @param triggerAtMillis 首次触发时间
     * @param intervalMillis 间隔时间
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setExactRepeatingAlarm(
        context: Context,
        triggerAtMillis: Long,
        intervalMillis: Long,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        }
        Log.d(TAG, "Exact repeating alarm set")
    }

    /**
     * 设置基于开机时间的闹钟。
     *
     * @param context 上下文
     * @param triggerAtMillis 触发时间（基于SystemClock.elapsedRealtime）
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setElapsedRealtimeAlarm(
        context: Context,
        triggerAtMillis: Long,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + triggerAtMillis,
            pendingIntent
        )
    }

    /**
     * 取消闹钟。
     *
     * @param context 上下文
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun cancelAlarm(
        context: Context,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
        Log.d(TAG, "Alarm cancelled")
    }

    /**
     * 检查是否可以设置精确闹钟。
     *
     * @param context 上下文
     * @return true 表示可以
     */
    fun canScheduleExactAlarms(context: Context): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /**
     * 请求精确闹钟权限。
     *
     * @param context 上下文
     */
    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val intent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    /**
     * 设置每天重复的闹钟。
     *
     * @param context 上下文
     * @param hourOfDay 小时（24小时制）
     * @param minute 分钟
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setDailyAlarm(
        context: Context,
        hourOfDay: Int,
        minute: Int,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(java.util.Calendar.HOUR_OF_DAY, hourOfDay)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.DAY_OF_YEAR, 1)
        }

        setRepeatingAlarm(
            context = context,
            triggerAtMillis = calendar.timeInMillis,
            intervalMillis = 24 * 60 * 60 * 1000L,
            intent = intent,
            requestCode = requestCode
        )
    }

    /**
     * 设置每周重复的闹钟。
     *
     * @param context 上下文
     * @param dayOfWeek 星期几（1-7，周日为1）
     * @param hourOfDay 小时
     * @param minute 分钟
     * @param intent 触发意图
     * @param requestCode 请求码
     */
    fun setWeeklyAlarm(
        context: Context,
        dayOfWeek: Int,
        hourOfDay: Int,
        minute: Int,
        intent: Intent,
        requestCode: Int = REQUEST_CODE_DEFAULT
    ) {
        val calendar = java.util.Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(java.util.Calendar.DAY_OF_WEEK, dayOfWeek)
            set(java.util.Calendar.HOUR_OF_DAY, hourOfDay)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(java.util.Calendar.WEEK_OF_YEAR, 1)
        }

        setRepeatingAlarm(
            context = context,
            triggerAtMillis = calendar.timeInMillis,
            intervalMillis = 7 * 24 * 60 * 60 * 1000L,
            intent = intent,
            requestCode = requestCode
        )
    }
}
