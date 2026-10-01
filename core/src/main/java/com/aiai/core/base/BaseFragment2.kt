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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger

/**
 * Fragment 基类。
 *
 * 功能：
 * - ViewBinding 封装
 * - ViewModel 注入
 * - 懒加载
 * - 生命周期日志
 * - 通用 UI 操作
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    protected lateinit var viewModel: VM
        private set

    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        viewModel = ViewModelProvider(this)[getViewModelClass()]
        Logger.d("${javaClass.simpleName} onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        observeData()
    }

    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun getViewModelClass(): Class<VM>
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    open fun observeData() {}

    /** 懒加载（首次可见时调用）。 */
    open fun lazyLoad() {}

    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            isFirstLoad = false
            lazyLoad()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isFirstLoad = true
    }
}
