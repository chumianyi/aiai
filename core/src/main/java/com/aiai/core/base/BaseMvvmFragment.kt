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

import com.aiai.common.ext.launchWhenStarted
import com.aiai.common.util.other.Logger

/**
 * MVVM 模式基类 Fragment。
 *
 * 自动收集 StateFlow，处理 Loading/Error。
 */
abstract class BaseMvvmFragment<VB : ViewBinding, VM : BaseViewModel> : BaseVmFragment<VB, VM>() {

    override fun observeData() {
        launchWhenStarted {
            viewModel.loadingState.collect { isLoading ->
                if (isLoading) showLoading() else hideLoading()
            }
        }
        launchWhenStarted {
            viewModel.errorEvent.collect { error ->
                showError(error)
            }
        }
    }

    open fun showLoading() {}
    open fun hideLoading() {}
    open fun showError(message: String) {}
}
