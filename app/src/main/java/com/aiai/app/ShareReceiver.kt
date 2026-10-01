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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.aiai.core.util.LogUtil

/**
 * 分享接收者
 *
 * 接收外部应用分享的文本/图片，打开对话页并填充内容。
 */
class ShareReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ShareReceiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        LogUtil.d(TAG, "Received share intent: ${intent.action}")

        when (intent.action) {
            Intent.ACTION_SEND -> handleSend(context, intent)
            Intent.ACTION_SEND_MULTIPLE -> handleSendMultiple(context, intent)
        }
    }

    private fun handleSend(context: Context, intent: Intent) {
        val text = intent.getStringExtra(Intent.EXTRA_TEXT)
        val subject = intent.getStringExtra(Intent.EXTRA_SUBJECT)
        val stream = intent.getParcelableExtra<android.net.Uri>(Intent.EXTRA_STREAM)

        val target = Intent(context, AppEntryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            text?.let { putExtra(Intent.EXTRA_TEXT, it) }
            subject?.let { putExtra(Intent.EXTRA_SUBJECT, it) }
            stream?.let { putExtra(Intent.EXTRA_STREAM, it) }
        }
        context.startActivity(target)
    }

    private fun handleSendMultiple(context: Context, intent: Intent) {
        val texts = intent.getStringArrayListExtra(Intent.EXTRA_TEXT)
        val streams = intent.getParcelableArrayListExtra<android.net.Uri>(Intent.EXTRA_STREAM)
        val target = Intent(context, AppEntryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            texts?.let { putStringArrayListExtra(Intent.EXTRA_TEXT, it) }
            streams?.let { putParcelableArrayListExtra(Intent.EXTRA_STREAM, it) }
        }
        context.startActivity(target)
    }
}
