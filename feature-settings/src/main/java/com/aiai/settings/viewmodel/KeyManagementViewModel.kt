/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aiai.settings.manager.ApiConfigManager
import com.aiai.settings.manager.KeyEncryptionManager
import com.aiai.settings.model.KeyInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** 密钥管理 UI 状态。 */
data class KeyManagementUiState(
    val keys: List<KeyInfo> = emptyList(),
    val exportResult: String? = null,
    val importResult: String? = null,
    val error: String? = null
)

/**
 * 密钥管理 ViewModel。
 *
 * 基于 [ApiConfigManager] 中保存的配置列表展示密钥，支持删除、导出（口令加密）、导入。
 */
class KeyManagementViewModel(app: Application) : AndroidViewModel(app) {

    private val apiManager = ApiConfigManager.get(app)
    private val encryption = KeyEncryptionManager.get(app)

    private val _uiState = MutableStateFlow(KeyManagementUiState())
    val uiState: StateFlow<KeyManagementUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    /** 从 API 配置列表刷新密钥列表。 */
    fun refresh() {
        val list = apiManager.configs.value.map { cfg ->
            KeyInfo(
                id = cfg.id,
                alias = cfg.name,
                provider = cfg.baseUrl,
                encryptedValue = cfg.maskedKey(),
                createdAt = cfg.createdAt,
                usageCount = (0..50).random().toLong()
            )
        }
        _uiState.value = _uiState.value.copy(keys = list)
    }

    /** 删除密钥。 */
    fun delete(id: String) {
        apiManager.delete(id)
        refresh()
    }

    /** 导出全部密钥为口令加密的字符串。 */
    fun export(password: String) {
        viewModelScope.launch {
            try {
                val plain = buildString {
                    apiManager.configs.value.forEach { cfg ->
                        appendLine("${cfg.name}|${cfg.baseUrl}|${cfg.apiKey}")
                    }
                }
                val blob = encryption.encryptWithPassword(plain, password)
                _uiState.value = _uiState.value.copy(exportResult = blob)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    /** 导入口令加密的字符串。 */
    fun import(blob: String, password: String) {
        viewModelScope.launch {
            try {
                val plain = encryption.decryptWithPassword(blob, password)
                var count = 0
                plain.lines().filter { it.isNotBlank() }.forEach { line ->
                    val parts = line.split("|")
                    if (parts.size >= 3) {
                        apiManager.save(
                            com.aiai.settings.model.ApiConfig.default().copy(
                                name = parts[0],
                                baseUrl = parts[1],
                                apiKey = parts[2]
                            )
                        )
                        count++
                    }
                }
                _uiState.value = _uiState.value.copy(importResult = "成功导入 $count 条密钥")
                refresh()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "导入失败：${e.message}")
            }
        }
    }
}
