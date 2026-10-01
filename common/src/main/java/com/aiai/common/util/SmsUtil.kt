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

import android.content.Context
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log

/**
 * 短信工具类。
 *
 * 提供发送、接收、读取、备份等功能。
 */
object SmsUtil {

    private const val TAG = "SmsUtil"

    /**
     * 短信数据模型。
     */
    data class SmsMessage(
        val address: String,
        val body: String,
        val date: Long,
        val type: Int,
        val read: Boolean
    )

    /**
     * 发送短信。
     *
     * @param context 上下文
     * @param phoneNumber 电话号码
     * @param message 短信内容
     * @return true 表示发送成功
     */
    @Suppress("MissingPermission")
    fun sendSms(context: Context, phoneNumber: String, message: String): Boolean {
        return try {
            val smsManager = context.getSystemService("sms") as SmsManager
            val parts = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null)
            Log.d(TAG, "SMS sent to $phoneNumber")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Send SMS failed", e)
            false
        }
    }

    /**
     * 读取收件箱短信。
     *
     * @param context 上下文
     * @param limit 条数限制
     * @return 短信列表
     */
    fun getInboxSms(context: Context, limit: Int = 100): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            Uri.parse("content://sms/inbox"),
            arrayOf("address", "body", "date", "type", "read"),
            null, null,
            "date DESC LIMIT $limit"
        )

        cursor?.use {
            val addressIndex = it.getColumnIndex("address")
            val bodyIndex = it.getColumnIndex("body")
            val dateIndex = it.getColumnIndex("date")
            val typeIndex = it.getColumnIndex("type")
            val readIndex = it.getColumnIndex("read")

            while (it.moveToNext()) {
                smsList.add(
                    SmsMessage(
                        address = it.getString(addressIndex) ?: "",
                        body = it.getString(bodyIndex) ?: "",
                        date = it.getLong(dateIndex),
                        type = it.getInt(typeIndex),
                        read = it.getInt(readIndex) == 1
                    )
                )
            }
        }

        return smsList
    }

    /**
     * 读取已发送短信。
     *
     * @param context 上下文
     * @param limit 条数限制
     * @return 短信列表
     */
    fun getSentSms(context: Context, limit: Int = 100): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            Uri.parse("content://sms/sent"),
            arrayOf("address", "body", "date", "type", "read"),
            null, null,
            "date DESC LIMIT $limit"
        )

        cursor?.use {
            val addressIndex = it.getColumnIndex("address")
            val bodyIndex = it.getColumnIndex("body")
            val dateIndex = it.getColumnIndex("date")
            val typeIndex = it.getColumnIndex("type")
            val readIndex = it.getColumnIndex("read")

            while (it.moveToNext()) {
                smsList.add(
                    SmsMessage(
                        address = it.getString(addressIndex) ?: "",
                        body = it.getString(bodyIndex) ?: "",
                        date = it.getLong(dateIndex),
                        type = it.getInt(typeIndex),
                        read = it.getInt(readIndex) == 1
                    )
                )
            }
        }

        return smsList
    }

    /**
     * 搜索短信。
     *
     * @param context 上下文
     * @param query 搜索关键词
     * @return 匹配的短信列表
     */
    fun searchSms(context: Context, query: String): List<SmsMessage> {
        val smsList = mutableListOf<SmsMessage>()
        val resolver = context.contentResolver

        val cursor = resolver.query(
            Uri.parse("content://sms"),
            arrayOf("address", "body", "date", "type", "read"),
            "body LIKE ? OR address LIKE ?",
            arrayOf("%$query%", "%$query%"),
            "date DESC"
        )

        cursor?.use {
            val addressIndex = it.getColumnIndex("address")
            val bodyIndex = it.getColumnIndex("body")
            val dateIndex = it.getColumnIndex("date")
            val typeIndex = it.getColumnIndex("type")
            val readIndex = it.getColumnIndex("read")

            while (it.moveToNext()) {
                smsList.add(
                    SmsMessage(
                        address = it.getString(addressIndex) ?: "",
                        body = it.getString(bodyIndex) ?: "",
                        date = it.getLong(dateIndex),
                        type = it.getInt(typeIndex),
                        read = it.getInt(readIndex) == 1
                    )
                )
            }
        }

        return smsList
    }

    /**
     * 检查短信权限。
     *
     * @param context 上下文
     * @return true 表示有权限
     */
    fun hasSmsPermission(context: Context): Boolean {
        return context.checkSelfPermission(android.Manifest.permission.READ_SMS) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    /**
     * 获取短信总数。
     *
     * @param context 上下文
     * @return 短信总数
     */
    fun getSmsCount(context: Context): Int {
        var count = 0
        val resolver = context.contentResolver
        val cursor = resolver.query(
            Uri.parse("content://sms"),
            arrayOf("_id"),
            null, null, null
        )
        cursor?.use { count = it.count }
        return count
    }

    /**
     * 格式化短信时间。
     *
     * @param timestamp 时间戳
     * @return 格式化后的时间
     */
    fun formatSmsTime(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("MM-dd HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}
