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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding

/**
 * 带 ViewModel 的 BaseActivity。
 *
 * @param VB ViewBinding 类型
 * @param VM ViewModel 类型
 */
abstract class BaseVmActivity<VB : ViewBinding, VM : ViewModel> : BaseActivity<VB>() {

    protected lateinit var viewModel: VM
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        super.onCreate(savedInstanceState)
    }

    /** 子类提供 ViewModel Class。 */
    abstract fun getViewModelClass(): Class<VM>
}
