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
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger
import com.aiai.core.viewmodel.BaseViewModel

/**
 * ViewModel Activity 基类。
 */
abstract class BaseVmActivity2<VB : ViewBinding, VM : BaseViewModel> : BaseActivity2<VB>() {

    protected lateinit var viewModel: VM
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        Logger.d("${javaClass.simpleName} viewModel init")
    }

    abstract fun getViewModelClass(): Class<VM>
}
