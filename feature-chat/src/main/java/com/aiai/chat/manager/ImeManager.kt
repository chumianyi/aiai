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
 * 输入法管理器
 *
 * 管理软键盘的显示和隐藏。
 */
class ImeManager(private val context: Context) {

    private val _keyboardVisible = MutableStateFlow(false)
    val keyboardVisible: StateFlow<Boolean> = _keyboardVisible.asStateFlow()

    fun showKeyboard(view: android.view.View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.showSoftInput(view, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
        _keyboardVisible.value = true
    }

    fun hideKeyboard(view: android.view.View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        _keyboardVisible.value = false
    }

    fun setKeyboardVisible(visible: Boolean) {
        _keyboardVisible.value = visible
    }

    fun isKeyboardVisible(): Boolean {
        return _keyboardVisible.value
    }
}
