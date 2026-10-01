/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 代理设置管理器
 *
 * 管理API请求的代理设置。
 */
class ProxyManager(private val context: Context) {

    data class ProxyConfig(
        val enabled: Boolean = false,
        val host: String = "",
        val port: Int = 0,
        val type: ProxyType = ProxyType.HTTP
    )

    enum class ProxyType {
        HTTP,
        SOCKS
    }

    private val _config = MutableStateFlow(ProxyConfig())
    val config: StateFlow<ProxyConfig> = _config.asStateFlow()

    fun updateConfig(config: ProxyConfig) {
        _config.value = config
    }

    fun disableProxy() {
        _config.value = ProxyConfig(enabled = false)
    }

    fun isProxyEnabled(): Boolean {
        return _config.value.enabled
    }
}
