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
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import com.aiai.app.ui.settings.SettingsActivity
import com.aiai.app.ui.settings.ApiConfigActivity
import com.aiai.app.ui.search.SearchActivity
import com.aiai.app.ui.about.AboutActivity
import com.aiai.app.ui.prompt.PromptLibraryActivity
import com.aiai.chat.ui.ChatActivity

/**
 * 应用全局导航器
 *
 * 统一管理所有页面跳转，避免在业务代码中直接构造Intent。
 */
object AppNavigator {

    // === Route paths ===
    const val ROUTE_CHAT = "aiai://chat"
    const val ROUTE_HISTORY = "aiai://history"
    const val ROUTE_SETTINGS = "aiai://settings"
    const val ROUTE_API_CONFIG = "aiai://settings/api"
    const val ROUTE_SEARCH = "aiai://search"
    const val ROUTE_ABOUT = "aiai://about"
    const val ROUTE_PROMPT_LIBRARY = "aiai://prompt/library"
    const val ROUTE_SHARE = "aiai://share"

    /** 打开聊天页 */
    fun openChat(context: Context, conversationId: Long? = null, prefillText: String? = null) {
        val intent = ChatActivity.newIntent(context, conversationId, prefillText)
        context.startActivity(intent)
    }

    /** 打开设置页 */
    fun openSettings(context: Context) {
        val intent = Intent(context, SettingsActivity::class.java)
        context.startActivity(intent)
    }

    /** 打开API配置页 */
    fun openApiConfig(context: Context) {
        val intent = Intent(context, ApiConfigActivity::class.java)
        context.startActivity(intent)
    }

    /** 打开搜索页 */
    fun openSearch(context: Context) {
        val intent = Intent(context, SearchActivity::class.java)
        context.startActivity(intent)
    }

    /** 打开关于页 */
    fun openAbout(context: Context) {
        val intent = Intent(context, AboutActivity::class.java)
        context.startActivity(intent)
    }

    /** 打开提示词库 */
    fun openPromptLibrary(context: Context) {
        val intent = Intent(context, PromptLibraryActivity::class.java)
        context.startActivity(intent)
    }

    /** 发送分享 */
    fun shareText(context: Context, text: String, title: String = context.getString(R.string.share_to)) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(intent, title))
    }

    /** 打开浏览器 */
    fun openBrowser(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    /** 带共享元素的转场 */
    fun openWithTransition(
        activity: FragmentActivity,
        target: Intent,
        sharedView: android.view.View?,
        transitionName: String?
    ) {
        val options = if (sharedView != null && transitionName != null) {
            ActivityOptionsCompat.makeSceneTransitionAnimation(activity, sharedView, transitionName)
        } else {
            ActivityOptionsCompat.makeBasic()
        }
        activity.startActivity(target, options.toBundle())
    }
}
