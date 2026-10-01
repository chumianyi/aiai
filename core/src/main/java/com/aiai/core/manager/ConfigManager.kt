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
package com.aiai.core.manager

import android.util.Log

/**
 * 配置管理器。
 *
 * 提供应用配置管理、功能开关、参数配置等功能。
 */
object ConfigManager {

    private const val TAG = "ConfigManager"

    /**
     * 配置项数据类。
     *
     * @property key 配置键
     * @property value 配置值
     * @property defaultValue 默认值
     * @property description 描述
     */
    data class ConfigItem(
        val key: String,
        var value: Any,
        val defaultValue: Any,
        val description: String = ""
    )

    private val configs = mutableMapOf<String, ConfigItem>()

    init {
        // 初始化默认配置
        putBoolean("enable_debug_log", false, "启用调试日志")
        putBoolean("enable_crash_report", true, "启用崩溃报告")
        putBoolean("enable_analytics", true, "启用数据分析")
        putBoolean("auto_save_draft", true, "自动保存草稿")
        putBoolean("send_on_enter", false, "回车发送消息")
        putBoolean("show_timestamps", true, "显示时间戳")
        putBoolean("enable_markdown", true, "启用Markdown渲染")
        putBoolean("enable_code_highlight", true, "代码高亮")
        putInt("max_history_messages", 100, "最大历史消息数")
        putInt("request_timeout", 30000, "请求超时时间(毫秒)")
        putInt("retry_count", 3, "重试次数")
        putString("default_model", "gpt-3.5-turbo", "默认模型")
        putString("api_base_url", "https://api.openai.com", "API基础地址")
    }

    /**
     * 保存布尔配置。
     */
    fun putBoolean(key: String, value: Boolean, description: String = "") {
        configs[key] = ConfigItem(key, value, value, description)
    }

    /**
     * 获取布尔配置。
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        return (configs[key]?.value as? Boolean) ?: defaultValue
    }

    /**
     * 保存整数配置。
     */
    fun putInt(key: String, value: Int, description: String = "") {
        configs[key] = ConfigItem(key, value, value, description)
    }

    /**
     * 获取整数配置。
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        return (configs[key]?.value as? Int) ?: defaultValue
    }

    /**
     * 保存字符串配置。
     */
    fun putString(key: String, value: String, description: String = "") {
        configs[key] = ConfigItem(key, value, value, description)
    }

    /**
     * 获取字符串配置。
     */
    fun getString(key: String, defaultValue: String = ""): String {
        return (configs[key]?.value as? String) ?: defaultValue
    }

    /**
     * 保存长整数配置。
     */
    fun putLong(key: String, value: Long, description: String = "") {
        configs[key] = ConfigItem(key, value, value, description)
    }

    /**
     * 获取长整数配置。
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        return (configs[key]?.value as? Long) ?: defaultValue
    }

    /**
     * 保存浮点配置。
     */
    fun putFloat(key: String, value: Float, description: String = "") {
        configs[key] = ConfigItem(key, value, value, description)
    }

    /**
     * 获取浮点配置。
     */
    fun getFloat(key: String, defaultValue: Float = 0f): Float {
        return (configs[key]?.value as? Float) ?: defaultValue
    }

    /**
     * 移除配置。
     */
    fun remove(key: String) {
        configs.remove(key)
    }

    /**
     * 检查是否包含配置。
     */
    fun contains(key: String): Boolean {
        return configs.containsKey(key)
    }

    /**
     * 重置为默认值。
     */
    fun resetToDefault(key: String) {
        configs[key]?.let { item ->
            configs[key] = item.copy(value = item.defaultValue)
            Log.d(TAG, "Reset $key to default")
        }
    }

    /**
     * 重置所有配置。
     */
    fun resetAll() {
        configs.forEach { (key, item) ->
            configs[key] = item.copy(value = item.defaultValue)
        }
        Log.d(TAG, "Reset all configs to default")
    }

    /**
     * 获取所有配置。
     */
    fun getAllConfigs(): Map<String, ConfigItem> {
        return configs.toMap()
    }

    /**
     * 获取配置数量。
     */
    fun size(): Int = configs.size
}
