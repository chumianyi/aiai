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
import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * MVVM Fragment 基类。
 *
 * 自动收集 Loading/Error 状态。
 */
abstract class BaseMvvmFragment2<VB : ViewBinding, VM : BaseViewModel> : BaseFragment<VB, VM>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.loadingState.collectLatest { isLoading ->
                if (isLoading) showLoading() else hideLoading()
            }
        }
        lifecycleScope.launch {
            viewModel.errorEvent.collectLatest { error ->
                showError(error)
            }
        }
    }

    open fun showLoading() {}
    open fun hideLoading() {}
    open fun showError(message: String) {}
}
