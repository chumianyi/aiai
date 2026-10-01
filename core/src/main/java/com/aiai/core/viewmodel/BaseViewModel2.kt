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
package com.aiai.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel 基类。
 *
 * 提供：
 * - loading 状态
 * - 错误事件
 * - 异常处理
 * - 统一 launch
 */
abstract class BaseViewModel : ViewModel() {

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorEvent = MutableSharedFlow<String>()
    val errorEvent: SharedFlow<String> = _errorEvent

    /** 启动协程，带异常处理。 */
    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                _errorEvent.emit(e.message ?: "未知错误")
            }
        }
    }

    /** 启动协程，带 loading。 */
    protected fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loadingState.value = true
                block()
            } catch (e: Exception) {
                _errorEvent.emit(e.message ?: "未知错误")
            } finally {
                _loadingState.value = false
            }
        }
    }

    fun showLoading() { _loadingState.value = true }
    fun hideLoading() { _loadingState.value = false }

    suspend fun sendError(msg: String) { _errorEvent.emit(msg) }
}
