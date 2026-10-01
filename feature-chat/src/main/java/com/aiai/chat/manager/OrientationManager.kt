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
 * 屏幕方向管理器
 *
 * 管理应用的屏幕方向锁定。
 */
class OrientationManager(private val context: Context) {

    enum class OrientationMode(val displayName: String) {
        AUTO("自动旋转"),
        PORTRAIT("竖屏"),
        LANDSCAPE("横屏")
    }

    private val _orientationMode = MutableStateFlow(OrientationMode.AUTO)
    val orientationMode: StateFlow<OrientationMode> = _orientationMode.asStateFlow()

    fun setOrientationMode(mode: OrientationMode) {
        _orientationMode.value = mode
    }

    fun lockPortrait() {
        _orientationMode.value = OrientationMode.PORTRAIT
    }

    fun lockLandscape() {
        _orientationMode.value = OrientationMode.LANDSCAPE
    }

    fun unlock() {
        _orientationMode.value = OrientationMode.AUTO
    }
}
