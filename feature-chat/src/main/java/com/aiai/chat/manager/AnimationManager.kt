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
 * 动画效果管理器
 *
 * 管理聊天界面的动画效果开关和配置。
 */
class AnimationManager(private val context: Context) {

    data class AnimationConfig(
        val messageAnimationEnabled: Boolean = true,
        val bubbleAnimationEnabled: Boolean = true,
        val typingAnimationEnabled: Boolean = true,
        val pageTransitionEnabled: Boolean = true,
        val animationDuration: Long = 300
    )

    private val _config = MutableStateFlow(AnimationConfig())
    val config: StateFlow<AnimationConfig> = _config.asStateFlow()

    fun updateConfig(config: AnimationConfig) {
        _config.value = config
    }

    fun disableAll() {
        _config.value = AnimationConfig(
            messageAnimationEnabled = false,
            bubbleAnimationEnabled = false,
            typingAnimationEnabled = false,
            pageTransitionEnabled = false
        )
    }

    fun enableAll() {
        _config.value = AnimationConfig()
    }

    fun areAnimationsEnabled(): Boolean {
        return _config.value.messageAnimationEnabled ||
                _config.value.bubbleAnimationEnabled ||
                _config.value.typingAnimationEnabled
    }
}
