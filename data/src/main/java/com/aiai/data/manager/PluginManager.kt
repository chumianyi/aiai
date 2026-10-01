/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.data.manager

import android.util.Log
import com.aiai.data.dao.PluginDao
import com.aiai.data.entity.PluginEntity

/**
 * 插件管理器。
 *
 * 管理插件的启用、禁用、配置等功能。
 */
class PluginManager(
    private val pluginDao: PluginDao,
) {

    companion object {
        private const val TAG = "PluginManager"
    }

    /**
     * 获取所有已安装插件。
     */
    fun getAllPlugins(): List<PluginEntity> {
        return pluginDao.getAllPluginsSync()
    }

    /**
     * 获取已启用的插件。
     */
    fun getEnabledPlugins(): List<PluginEntity> {
        return pluginDao.getEnabledPluginsSync()
    }

    /**
     * 启用插件。
     */
    suspend fun enablePlugin(pluginId: String) {
        pluginDao.setEnabled(pluginId, true)
        Log.d(TAG, "Enabled plugin: $pluginId")
    }

    /**
     * 禁用插件。
     */
    suspend fun disablePlugin(pluginId: String) {
        pluginDao.setEnabled(pluginId, false)
        Log.d(TAG, "Disabled plugin: $pluginId")
    }

    /**
     * 安装插件。
     */
    suspend fun installPlugin(plugin: PluginEntity) {
        pluginDao.insert(plugin)
        Log.d(TAG, "Installed plugin: ${plugin.name}")
    }

    /**
     * 卸载插件。
     */
    suspend fun uninstallPlugin(pluginId: String) {
        pluginDao.deleteById(pluginId)
        Log.d(TAG, "Uninstalled plugin: $pluginId")
    }

    /**
     * 更新插件配置。
     */
    suspend fun updatePluginConfig(pluginId: String, configJson: String) {
        pluginDao.updateConfig(pluginId, configJson)
        Log.d(TAG, "Updated config for: $pluginId")
    }

    /**
     * 检查插件是否已安装。
     */
    suspend fun isInstalled(packageName: String): Boolean {
        return pluginDao.getPluginByPackageName(packageName) != null
    }

    /**
     * 获取插件数量统计。
     */
    fun getPluginStats(): PluginStats {
        val all = pluginDao.getAllPluginsSync()
        return PluginStats(
            total = all.size,
            enabled = all.count { it.isEnabled },
            disabled = all.count { !it.isEnabled },
        )
    }

    /**
     * 插件统计数据。
     */
    data class PluginStats(
        val total: Int,
        val enabled: Int,
        val disabled: Int,
    )
}
