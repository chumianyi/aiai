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
 * 启动页管理器
 *
 * 管理应用启动时的引导页和欢迎页逻辑。
 */
class SplashScreenManager(private val context: Context) {

    enum class SplashState {
        SHOWING,
        FINISHED
    }

    data class SplashConfig(
        val durationMs: Long = 2000,
        val showGuide: Boolean = true
    )

    private val _splashState = MutableStateFlow(SplashState.SHOWING)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    private val _config = MutableStateFlow(SplashConfig())
    val config: StateFlow<SplashConfig> = _config.asStateFlow()

    fun finishSplash() {
        _splashState.value = SplashState.FINISHED
    }

    fun reset() {
        _splashState.value = SplashState.SHOWING
    }

    fun shouldShowGuide(): Boolean {
        return _config.value.showGuide
    }
}
