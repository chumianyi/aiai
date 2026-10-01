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
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

/**
 * 快捷方式工具类。
 *
 * 提供静态/动态快捷方式、固定快捷方式等功能。
 */
object ShortcutUtil {

    private const val TAG = "ShortcutUtil"

    /**
     * 获取快捷方式管理器。
     */
    private fun getShortcutManager(context: Context): ShortcutManager? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            context.getSystemService(ShortcutManager::class.java)
        } else {
            null
        }
    }

    /**
     * 检查是否支持快捷方式。
     *
     * @param context 上下文
     * @return true 表示支持
     */
    fun isSupported(context: Context): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 &&
                getShortcutManager(context) != null
    }

    /**
     * 发布动态快捷方式。
     *
     * @param context 上下文
     * @param shortcuts 快捷方式列表
     * @return true 表示成功
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun publishDynamicShortcuts(
        context: Context,
        shortcuts: List<ShortcutInfo>
    ): Boolean {
        val manager = getShortcutManager(context) ?: return false
        return try {
            manager.dynamicShortcuts = shortcuts
            Log.d(TAG, "Published ${shortcuts.size} dynamic shortcuts")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Publish shortcuts failed", e)
            false
        }
    }

    /**
     * 创建快捷方式信息。
     *
     * @param context 上下文
     * @param id 唯一标识
     * @param shortLabel 短标签
     * @param longLabel 长标签
     * @param iconRes 图标资源
     * @param targetClass 目标 Activity
     * @return ShortcutInfo
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun createShortcutInfo(
        context: Context,
        id: String,
        shortLabel: String,
        longLabel: String = shortLabel,
        iconRes: Int,
        targetClass: Class<*>
    ): ShortcutInfo {
        val intent = Intent(context, targetClass).apply {
            action = Intent.ACTION_VIEW
        }

        return ShortcutInfo.Builder(context, id)
            .setShortLabel(shortLabel)
            .setLongLabel(longLabel)
            .setIcon(Icon.createWithResource(context, iconRes))
            .setIntent(intent)
            .build()
    }

    /**
     * 禁用快捷方式。
     *
     * @param context 上下文
     * @param shortcutIds 要禁用的快捷方式ID列表
     * @param message 禁用提示
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun disableShortcuts(
        context: Context,
        shortcutIds: List<String>,
        message: String = "此快捷方式已不可用"
    ) {
        val manager = getShortcutManager(context) ?: return
        manager.disableShortcuts(shortcutIds, message)
    }

    /**
     * 启用快捷方式。
     *
     * @param context 上下文
     * @param shortcutIds 要启用的快捷方式ID列表
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun enableShortcuts(context: Context, shortcutIds: List<String>) {
        val manager = getShortcutManager(context) ?: return
        manager.enableShortcuts(shortcutIds)
    }

    /**
     * 获取所有动态快捷方式。
     *
     * @param context 上下文
     * @return 快捷方式列表
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getDynamicShortcuts(context: Context): List<ShortcutInfo> {
        val manager = getShortcutManager(context) ?: return emptyList()
        return manager.dynamicShortcuts
    }

    /**
     * 获取所有已固定的快捷方式。
     *
     * @param context 上下文
     * @return 快捷方式列表
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun getPinnedShortcuts(context: Context): List<ShortcutInfo> {
        val manager = getShortcutManager(context) ?: return emptyList()
        return manager.pinnedShortcuts
    }

    /**
     * 请求固定快捷方式到桌面。
     *
     * @param context 上下文
     * @param shortcut 快捷方式信息
     * @return true 表示请求成功
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun requestPinShortcut(context: Context, shortcut: ShortcutInfo): Boolean {
        val manager = getShortcutManager(context) ?: return false
        if (!manager.isRequestPinShortcutSupported) return false

        val callbackIntent = manager.createShortcutResultIntent(shortcut)
        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context, 0, callbackIntent,
            android.app.PendingIntent.FLAG_MUTABLE
        )

        return manager.requestPinShortcut(shortcut, pendingIntent.intentSender)
    }

    /**
     * 移除动态快捷方式。
     *
     * @param context 上下文
     * @param shortcutIds 要移除的快捷方式ID列表
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun removeDynamicShortcuts(context: Context, shortcutIds: List<String>) {
        val manager = getShortcutManager(context) ?: return
        manager.removeDynamicShortcuts(shortcutIds)
    }

    /**
     * 移除所有动态快捷方式。
     *
     * @param context 上下文
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun removeAllDynamicShortcuts(context: Context) {
        val manager = getShortcutManager(context) ?: return
        manager.removeAllDynamicShortcuts()
    }

    /**
     * 更新快捷方式。
     *
     * @param context 上下文
     * @param shortcuts 要更新的快捷方式列表
     * @return true 表示成功
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun updateShortcuts(context: Context, shortcuts: List<ShortcutInfo>): Boolean {
        val manager = getShortcutManager(context) ?: return false
        return manager.updateShortcuts(shortcuts)
    }

    /**
     * 获取快捷方式最大数量。
     *
     * @param context 上下文
     * @return 最大数量
     */
    @RequiresApi(Build.VERSION_CODES.N_MR1)
    fun getMaxShortcutCount(context: Context): Int {
        val manager = getShortcutManager(context) ?: return 0
        return manager.maxShortcutCountPerActivity
    }
}
