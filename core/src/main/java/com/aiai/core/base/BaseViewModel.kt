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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel 基类。
 *
 * 功能：
 * - 统一 CoroutineScope（viewModelScope）
 * - 异常处理
 * - Loading 状态
 * - 错误事件
 */
abstract class BaseViewModel : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState.asStateFlow()

    private val _errorEvent = MutableStateFlow("")
    val errorEvent: StateFlow<String> = _errorEvent.asStateFlow()

    /** 启动协程（自动异常捕获）。 */
    protected fun launch(
        block: suspend () -> Unit,
        onError: (Throwable) -> Unit = { _errorEvent.value = it.message ?: "未知错误" }
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /** 启动带 Loading 的协程。 */
    protected fun launchWithLoading(
        block: suspend () -> Unit,
        onError: (Throwable) -> Unit = { _errorEvent.value = it.message ?: "未知错误" }
    ) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                block()
            } catch (e: Exception) {
                onError(e)
            } finally {
                _loadingState.value = false
            }
        }
    }

    /** 显示 Loading。 */
    protected fun showLoading() { _loadingState.value = true }

    /** 隐藏 Loading。 */
    protected fun hideLoading() { _loadingState.value = false }

    /** 发送错误事件。 */
    protected fun showError(message: String) { _errorEvent.value = message }
}
