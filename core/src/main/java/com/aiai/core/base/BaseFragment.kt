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
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger

/**
 * 所有 Fragment 的基类。
 *
 * 功能：ViewBinding 封装、懒加载、生命周期日志。
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB get() = _binding!!

    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view, savedInstanceState)
        observeData()
        Logger.d("${javaClass.simpleName} onViewCreated")
    }

    /** 子类提供 ViewBinding。 */
    abstract fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /** 初始化 View。 */
    abstract fun initView(view: View, savedInstanceState: Bundle?)

    /** 观察数据。 */
    open fun observeData() {}

    /** 懒加载（首次可见时调用）。 */
    open fun lazyLoad() {}

    override fun onResume() {
        super.onResume()
        if (!isLoaded) {
            isLoaded = true
            lazyLoad()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isLoaded = false
    }
}
