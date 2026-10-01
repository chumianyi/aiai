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
 * 隐私政策管理器
 *
 * 管理用户隐私政策同意状态。
 */
class PrivacyPolicyManager(private val context: Context) {

    enum class ConsentStatus {
        UNKNOWN,
        AGREED,
        DENIED
    }

    private val _consentStatus = MutableStateFlow(ConsentStatus.UNKNOWN)
    val consentStatus: StateFlow<ConsentStatus> = _consentStatus.asStateFlow()

    fun agree() {
        _consentStatus.value = ConsentStatus.AGREED
    }

    fun deny() {
        _consentStatus.value = ConsentStatus.DENIED
    }

    fun hasAgreed(): Boolean {
        return _consentStatus.value == ConsentStatus.AGREED
    }

    fun needsConsent(): Boolean {
        return _consentStatus.value == ConsentStatus.UNKNOWN
    }
}
