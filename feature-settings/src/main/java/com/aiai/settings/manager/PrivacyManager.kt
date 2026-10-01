/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.FavoriteItem

/**
 * 隐私管理。
 *
 * 统一管理数据收集开关、聊天记录留存策略、本地数据清除与导出。
 */
class PrivacyManager(
    private val context: Context,
    private val settings: SettingsManager
) {

    /** 是否允许匿名数据收集。 */
    var dataCollectionEnabled: Boolean
        get() = settings.dataCollection.value
        set(value) = settings.setDataCollection(value)

    /** 是否保留聊天记录。 */
    var saveChatHistory: Boolean
        get() = settings.saveChatHistory.value
        set(value) = settings.setSaveChatHistory(value)

    /**
     * 清除全部本地业务数据（数据库、缓存、收藏等）。
     * 注意：不清除 API 配置与密钥（避免用户被登出）。
     */
    fun clearAllUserData(onDone: () -> Unit) {
        // 1. 清除缓存
        CacheManager.get(context).clean()
        // 2. 删除数据库文件（占位，实际由 data 模块提供）
        context.deleteDatabase("aiai_database.db")
        // 3. 清除收藏（占位）
        onDone()
    }

    /**
     * 导出用户全部数据到外部文件。
     */
    fun exportUserData(favorites: List<FavoriteItem>): String {
        return buildString {
            appendLine("爱Ai 用户数据导出")
            appendLine("导出时间: ${System.currentTimeMillis()}")
            appendLine("=== 收藏 ===")
            favorites.forEach {
                appendLine("- [${it.type}] ${it.title}: ${it.summary}")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: PrivacyManager? = null

        fun get(context: Context): PrivacyManager {
            return instance ?: synchronized(this) {
                instance ?: PrivacyManager(
                    context.applicationContext,
                    SettingsManager.get(context)
                ).also { instance = it }
            }
        }
    }
}
