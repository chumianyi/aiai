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

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aiai.core.util.LogUtil

/**
 * 深链接处理器
 *
 * 支持 aiai://  scheme，路由到对应页面。
 */
object DeepLinkHandler {

    private const val TAG = "DeepLinkHandler"

    /**
     * 解析Intent，返回对应页面的Intent；无法识别时返回null
     */
    fun parseIntent(intent: Intent): Intent? {
        val data: Uri = intent.data ?: return null
        LogUtil.d(TAG, "Parsing deep link: $data")

        return when (data.scheme) {
            AppConstants.DEEPLINK_SCHEME -> handleAppScheme(data)
            "https", "http" -> handleWebLink(data)
            else -> null
        }
    }

    private fun handleAppScheme(uri: Uri): Intent? {
        return when (uri.host) {
            "chat" -> {
                val convId = uri.getQueryParameter("conversation_id")?.toLongOrNull()
                Intent().apply {
                    setClassName(AppConstants.APP_PACKAGE, "com.aiai.chat.ui.ChatActivity")
                    convId?.let { putExtra("conversation_id", it) }
                }
            }
            "settings" -> {
                Intent().apply {
                    setClassName(AppConstants.APP_PACKAGE, "com.aiai.app.ui.settings.SettingsActivity")
                }
            }
            "about" -> {
                Intent().apply {
                    setClassName(AppConstants.APP_PACKAGE, "com.aiai.app.ui.about.AboutActivity")
                }
            }
            else -> null
        }
    }

    private fun handleWebLink(uri: Uri): Intent? {
        // 支持通用链接（App Links）
        return if (uri.host?.contains("aiai.com") == true) {
            handleAppScheme(uri)
        } else {
            null
        }
    }
}
