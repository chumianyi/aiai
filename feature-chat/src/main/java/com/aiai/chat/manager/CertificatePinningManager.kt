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
 * 证书锁定管理器
 *
 * 管理SSL证书锁定（Certificate Pinning）配置。
 */
class CertificatePinningManager(private val context: Context) {

    data class PinConfig(
        val enabled: Boolean = false,
        val pins: List<String> = emptyList()
    )

    private val _config = MutableStateFlow(PinConfig())
    val config: StateFlow<PinConfig> = _config.asStateFlow()

    fun updateConfig(config: PinConfig) {
        _config.value = config
    }

    fun addPin(pin: String) {
        _config.value = _config.value.copy(pins = _config.value.pins + pin)
    }

    fun isEnabled(): Boolean {
        return _config.value.enabled
    }
}
