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
import kotlinx.coroutines.launch

/**
 * ViewModel 基类 V3。
 */
abstract class BaseViewModelV3 : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading

    private val _error = MutableSharedFlow<String>()
    val error = _error

    protected fun launch(block: suspend () -> Unit) {
        viewModelScope.launch {
            try { block() } catch (e: Exception) { _error.emit(e.message ?: "错误") }
        }
    }

    protected fun launchWithLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                block()
            } catch (e: Exception) { _error.emit(e.message ?: "错误") }
            finally { _loading.value = false }
        }
    }
}
