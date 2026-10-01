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
package com.aiai.settings.manager

import android.content.Context
import com.aiai.settings.model.ApiConfig
import com.aiai.settings.model.ApiTestResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * API 配置管理。
 *
 * 负责：
 * - 多套 API 配置的增删改查与切换；
 * - 密钥字段通过 [KeyEncryptionManager] 加密存储；
 * - 使用 [Gson] 序列化配置列表到普通 SharedPreferences；
 * - 提供真实 HTTP 连接测试（GET baseUrl + /models）。
 */
class ApiConfigManager(
    private val context: Context,
    private val encryption: KeyEncryptionManager
) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _configs = MutableStateFlow<List<ApiConfig>>(loadConfigs())
    /** 所有配置流。 */
    val configs: StateFlow<List<ApiConfig>> = _configs.asStateFlow()

    private val _activeConfig = MutableStateFlow<ApiConfig?>(loadActive())
    /** 当前启用的配置流。 */
    val activeConfig: StateFlow<ApiConfig?> = _activeConfig.asStateFlow()

    // region 持久化

    private fun loadConfigs(): List<ApiConfig> {
        val raw = prefs.getString(KEY_CONFIGS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<ApiConfig>>() {}.type
            val list: List<ApiConfig> = gson.fromJson(raw, type)
            // 解密 apiKey 字段
            list.map { cfg ->
                cfg.copy(apiKey = encryption.decryptAndLoad(keyForKey(cfg.id)) ?: "")
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun persist(list: List<ApiConfig>) {
        // 序列化时使用掩码，真实密钥单独加密保存
        val masked = list.map { it.copy(apiKey = "") }
        prefs.edit().putString(KEY_CONFIGS, gson.toJson(masked)).apply()
        list.forEach { cfg ->
            if (cfg.apiKey.isNotEmpty()) {
                encryption.encryptAndStore(keyForKey(cfg.id), cfg.apiKey)
            } else {
                encryption.remove(keyForKey(cfg.id))
            }
        }
    }

    private fun keyForKey(id: String) = "apikey_$id"

    private fun loadActive(): ApiConfig? {
        val activeId = prefs.getString(KEY_ACTIVE_ID, null) ?: return null
        return _configs.value.firstOrNull { it.id == activeId }
    }

    // endregion

    // region 增删改

    /**
     * 保存（新增或更新）配置 [config]。
     */
    fun save(config: ApiConfig) {
        val list = _configs.value.toMutableList()
        val idx = list.indexOfFirst { it.id == config.id }
        if (idx >= 0) list[idx] = config else list.add(config)
        _configs.value = list
        persist(list)
    }

    /**
     * 删除指定 [id] 配置，若删除的是当前配置则自动切换到第一个。
     */
    fun delete(id: String) {
        val list = _configs.value.filterNot { it.id == id }
        _configs.value = list
        encryption.remove(keyForKey(id))
        persist(list)
        if (_activeConfig.value?.id == id) {
            setActive(list.firstOrNull()?.id)
        }
    }

    /**
     * 切换到 [id] 为当前激活配置。
     */
    fun setActive(id: String?) {
        if (id == null) {
            prefs.edit().remove(KEY_ACTIVE_ID).apply()
            _activeConfig.value = null
            return
        }
        val list = _configs.value.map { it.copy(isActive = it.id == id) }
        _configs.value = list
        prefs.edit().putString(KEY_ACTIVE_ID, id).apply()
        _activeConfig.value = list.firstOrNull { it.id == id }
    }

    /** 获取指定 id 的配置。 */
    fun getById(id: String): ApiConfig? = _configs.value.firstOrNull { it.id == id }

    // endregion

    // region 校验与连接测试

    /**
     * 校验 [config] 字段合法性，返回错误消息，null 表示通过。
     */
    fun validate(config: ApiConfig): String? {
        if (config.baseUrl.isBlank()) return "接口地址不能为空"
        if (!config.baseUrl.startsWith("http://") && !config.baseUrl.startsWith("https://")) {
            return "接口地址必须以 http(s):// 开头"
        }
        if (config.apiKey.isBlank()) return "API 密钥不能为空"
        if (config.modelName.isBlank()) return "模型名称不能为空"
        return null
    }

    /**
     * 在 IO 线程测试 [config] 的连通性，请求 GET {baseUrl}/models。
     */
    suspend fun testConnection(config: ApiConfig): ApiTestResult = withContext(Dispatchers.IO) {
        val url = try {
            URL(config.baseUrl.trimEnd('/') + "/models")
        } catch (e: Exception) {
            return@withContext ApiTestResult(false, message = "地址格式错误")
        }
        val start = System.currentTimeMillis()
        var conn: HttpURLConnection? = null
        try {
            conn = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "GET"
                connectTimeout = 8000
                readTimeout = 8000
                setRequestProperty("Authorization", "Bearer ${config.apiKey}")
                setRequestProperty("Accept", "application/json")
            }
            val code = conn.responseCode
            val latency = System.currentTimeMillis() - start
            val ok = code in 200..399
            ApiTestResult(
                success = ok,
                latencyMs = latency,
                httpCode = code,
                message = if (ok) "HTTP $code" else "HTTP $code ${conn.responseMessage}"
            )
        } catch (e: IOException) {
            ApiTestResult(false, message = e.message ?: "网络不可达")
        } catch (e: Exception) {
            ApiTestResult(false, message = e.message ?: "未知错误")
        } finally {
            conn?.disconnect()
        }
    }

    // endregion

    companion object {
        private const val PREFS_NAME = "aiai_api_configs"
        private const val KEY_CONFIGS = "configs_json"
        private const val KEY_ACTIVE_ID = "active_config_id"

        @Volatile
        private var instance: ApiConfigManager? = null

        /** 获取全局单例。 */
        fun get(context: Context): ApiConfigManager {
            return instance ?: synchronized(this) {
                instance ?: ApiConfigManager(
                    context.applicationContext,
                    KeyEncryptionManager.get(context)
                ).also { instance = it }
            }
        }
    }
}
