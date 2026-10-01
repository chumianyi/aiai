/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 用户行为埋点：匿名统计设置项点击。
 */
class AnalyticsManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_analytics", Context.MODE_PRIVATE)

    fun track(event: String, params: Map<String, String> = emptyMap()) {
        if (!com.aiai.settings.manager.SettingsManager.get(context).dataCollection.value) return
        // 实际项目上报到服务器，这里仅本地记录
        val count = prefs.getInt(event, 0) + 1
        prefs.edit().putInt(event, count).apply()
    }

    fun eventCount(event: String): Int = prefs.getInt(event, 0)

    fun clear() = prefs.edit().clear().apply()
}
