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
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import com.aiai.core.util.LogUtil

/**
 * 应用快捷方式（长按图标）
 *
 * - 新对话
 * - 提示词库
 * - 设置
 */
object AppShortcuts {

    private const val TAG = "AppShortcuts"
    private const val ID_NEW_CHAT = "new_chat"
    private const val ID_PROMPT = "prompt"
    private const val ID_SETTINGS = "settings"

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun pushDynamicShortcuts(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return
        val manager = context.getSystemService(ShortcutManager::class.java) ?: return

        val shortcuts = listOf(
            ShortcutInfo.Builder(context, ID_NEW_CHAT)
                .setShortLabel(context.getString(R.string.shortcut_new_chat))
                .setLongLabel(context.getString(R.string.shortcut_new_chat_long))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_edit))
                .setIntent(android.content.Intent(android.content.Intent.ACTION_VIEW).setClassName(
                    context.packageName, "com.aiai.app.AppEntryActivity"))
                .build(),
            ShortcutInfo.Builder(context, ID_PROMPT)
                .setShortLabel(context.getString(R.string.shortcut_prompt))
                .setLongLabel(context.getString(R.string.shortcut_prompt_long))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_template))
                .build(),
            ShortcutInfo.Builder(context, ID_SETTINGS)
                .setShortLabel(context.getString(R.string.shortcut_settings))
                .setLongLabel(context.getString(R.string.shortcut_settings_long))
                .setIcon(Icon.createWithResource(context, R.drawable.ic_settings))
                .build()
        )

        manager.dynamicShortcuts = shortcuts
        LogUtil.d(TAG, "Dynamic shortcuts pushed: ${shortcuts.size}")
    }
}
