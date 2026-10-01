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

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcEvent
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.Build
import android.util.Log
import java.nio.charset.Charset
import java.util.Locale

/**
 * NFC 工具类。
 *
 * 提供 NDEF 读写、标签检测、前台调度等功能。
 */
object NfcUtil {

    private const val TAG = "NfcUtil"

    /**
     * NFC 标签读取回调。
     */
    interface NfcReadCallback {
        /** 标签检测到。 */
        fun onTagDiscovered(tag: Tag) {}

        /** NDEF 消息读取成功。 */
        fun onNdefMessageRead(messages: List<NdefMessage>) {}

        /** 错误。 */
        fun onError(error: String) {}
    }

    /**
     * 获取 NFC 适配器。
     */
    fun getNfcAdapter(context: Context): NfcAdapter? {
        return NfcAdapter.getDefaultAdapter(context)
    }

    /**
     * 检查 NFC 是否可用。
     *
     * @param context 上下文
     * @return true 表示可用
     */
    fun isNfcSupported(context: Context): Boolean {
        return getNfcAdapter(context) != null
    }

    /**
     * 检查 NFC 是否已开启。
     *
     * @param context 上下文
     * @return true 表示已开启
     */
    fun isNfcEnabled(context: Context): Boolean {
        return getNfcAdapter(context)?.isEnabled == true
    }

    /**
     * 启用前台调度。
     *
     * @param activity Activity
     * @param callback 读取回调
     */
    fun enableForegroundDispatch(activity: Activity, callback: NfcReadCallback) {
        val adapter = getNfcAdapter(activity) ?: run {
            callback.onError("NFC 不可用")
            return
        }

        val pendingIntent = android.app.PendingIntent.getActivity(
            activity, 0,
            Intent(activity, activity.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            android.app.PendingIntent.FLAG_MUTABLE
        )

        val filters = arrayOf(
            android.content.IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED),
            android.content.IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED),
            android.content.IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        )

        val techList = arrayOf(
            arrayOf(Ndef::class.java.name),
            arrayOf(NdefFormatable::class.java.name)
        )

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    /**
     * 禁用前台调度。
     *
     * @param activity Activity
     */
    fun disableForegroundDispatch(activity: Activity) {
        getNfcAdapter(activity)?.disableForegroundDispatch(activity)
    }

    /**
     * 处理 NFC 意图。
     *
     * @param intent 意图
     * @param callback 读取回调
     */
    fun handleNfcIntent(intent: Intent, callback: NfcReadCallback) {
        when (intent.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED,
            NfcAdapter.ACTION_TECH_DISCOVERED,
            NfcAdapter.ACTION_TAG_DISCOVERED -> {
                val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                }

                tag?.let {
                    callback.onTagDiscovered(it)
                    readNdefMessage(it, callback)
                }
            }
        }
    }

    /**
     * 读取 NDEF 消息。
     */
    private fun readNdefMessage(tag: Tag, callback: NfcReadCallback) {
        val ndef = Ndef.get(tag) ?: run {
            callback.onError("标签不支持 NDEF")
            return
        }

        try {
            ndef.connect()
            val messages = try {
                ndef.javaClass.getMethod("getCachedNdefMessages").invoke(ndef) as? Array<*> ?: emptyArray<Any?>()
            } catch (e: Exception) { emptyArray<Any?>() }
            callback.onNdefMessageRead(messages.toList() as List<android.nfc.NdefMessage>)
            ndef.close()
        } catch (e: Exception) {
            Log.e(TAG, "Read NDEF failed", e)
            callback.onError("读取失败: ${e.message}")
        }
    }

    /**
     * 创建文本 NDEF 记录。
     *
     * @param text 文本内容
     * @param locale 语言区域
     * @return NdefRecord
     */
    fun createTextRecord(text: String, locale: Locale = Locale.getDefault()): NdefRecord {
        val language = locale.language
        val textBytes = text.toByteArray(Charset.forName("UTF-8"))
        val langBytes = language.toByteArray(Charset.forName("US-ASCII"))

        val payload = ByteArray(1 + langBytes.size + textBytes.size)
        payload[0] = langBytes.size.toByte()
        System.arraycopy(langBytes, 0, payload, 1, langBytes.size)
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.size, textBytes.size)

        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload)
    }

    /**
     * 创建 Uri NDEF 记录。
     *
     * @param uri Uri 字符串
     * @return NdefRecord
     */
    fun createUriRecord(uri: String): NdefRecord {
        return NdefRecord.createUri(uri)
    }

    /**
     * 创建 NDEF 消息。
     *
     * @param records NDEF 记录列表
     * @return NdefMessage
     */
    fun createNdefMessage(vararg records: NdefRecord): NdefMessage {
        return NdefMessage(records)
    }

    /**
     * 写入 NDEF 消息到标签。
     *
     * @param tag 标签
     * @param message 要写入的消息
     * @return true 表示成功
     */
    fun writeNdefMessage(tag: Tag, message: NdefMessage): Boolean {
        val ndef = Ndef.get(tag) ?: return false

        try {
            ndef.connect()
            if (ndef.isWritable) {
                ndef.writeNdefMessage(message)
                ndef.close()
                return true
            }
            ndef.close()
        } catch (e: Exception) {
            Log.e(TAG, "Write NDEF failed", e)
        }
        return false
    }

    /**
     * 解析 NDEF 文本记录。
     *
     * @param record NDEF 记录
     * @return 解析出的文本
     */
    fun parseTextRecord(record: NdefRecord): String? {
        val payload = record.payload
        if (payload == null || payload.size < 2) return null

        val languageLength = payload[0].toInt() and 0x3F
        if (payload.size < 1 + languageLength) return null

        return String(
            payload,
            1 + languageLength,
            payload.size - 1 - languageLength,
            Charset.forName("UTF-8")
        )
    }

    /**
     * 获取标签 UID。
     *
     * @param tag 标签
     * @return UID 十六进制字符串
     */
    fun getTagUid(tag: Tag): String {
        return tag.id?.joinToString(":") { "%02X".format(it) } ?: ""
    }
}
