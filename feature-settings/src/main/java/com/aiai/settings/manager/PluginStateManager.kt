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
import com.aiai.settings.model.PluginInfo

/**
 * 插件状态持久化：安装/启用状态。
 */
class PluginStateManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_plugins", Context.MODE_PRIVATE)

    fun isInstalled(id: String): Boolean = prefs.getBoolean("inst_$id", false)
    fun setInstalled(id: String, v: Boolean) = prefs.edit().putBoolean("inst_$id", v).apply()

    fun isEnabled(id: String): Boolean = prefs.getBoolean("en_$id", true)
    fun setEnabled(id: String, v: Boolean) = prefs.edit().putBoolean("en_$id", v).apply()

    fun merge(plugins: List<PluginInfo>): List<PluginInfo> {
        return plugins.map {
            it.copy(
                status = if (isInstalled(it.id)) com.aiai.settings.model.PluginStatus.INSTALLED
                         else com.aiai.settings.model.PluginStatus.NOT_INSTALLED,
                enabled = isEnabled(it.id)
            )
        }
    }
}
