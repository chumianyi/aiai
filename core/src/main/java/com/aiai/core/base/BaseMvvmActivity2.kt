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

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger
import com.aiai.core.viewmodel.BaseViewModel

/**
 * MVVM Activity 基类。
 *
 * 自动收集 Loading/Error 状态。
 */
abstract class BaseMvvmActivity2<VB : ViewBinding, VM : BaseViewModel> : BaseVmActivity2<VB, VM>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadingState.collect { isLoading ->
                if (isLoading) showLoading() else hideLoading()
            }
        }
        lifecycleScope.launch {
            viewModel.errorEvent.collect { error ->
                showError(error)
            }
        }
    }

    open fun showLoading() {
        Logger.d("showLoading")
    }

    open fun hideLoading() {
        Logger.d("hideLoading")
    }

    open fun showError(message: String) {
        Logger.e("showError: $message")
    }
}
