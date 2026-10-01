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
 * 手势操作管理器
 *
 * 管理聊天界面的手势操作（长按、双击、滑动等）。
 */
class GestureManager(private val context: Context) {

    enum class GestureType {
        LONG_PRESS,
        DOUBLE_TAP,
        SWIPE_LEFT,
        SWIPE_RIGHT,
        SWIPE_UP,
        SWIPE_DOWN,
        PINCH
    }

    data class GestureAction(
        val gesture: GestureType,
        val action: String,
        val enabled: Boolean = true
    )

    private val _gestureActions = MutableStateFlow<List<GestureAction>>(emptyList())
    val gestureActions: StateFlow<List<GestureAction>> = _gestureActions.asStateFlow()

    init {
        loadDefaultGestures()
    }

    private fun loadDefaultGestures() {
        _gestureActions.value = listOf(
            GestureAction(GestureType.LONG_PRESS, "show_message_actions"),
            GestureAction(GestureType.DOUBLE_TAP, "copy_message"),
            GestureAction(GestureType.SWIPE_LEFT, "reveal_actions"),
            GestureAction(GestureType.SWIPE_DOWN, "hide_keyboard")
        )
    }

    fun getActionForGesture(gesture: GestureType): String? {
        return _gestureActions.value.find { it.gesture == gesture && it.enabled }?.action
    }

    fun setGestureEnabled(gesture: GestureType, enabled: Boolean) {
        _gestureActions.value = _gestureActions.value.map {
            if (it.gesture == gesture) it.copy(enabled = enabled) else it
        }
    }
}
