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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.core.base

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding

/**
 * ViewPager Fragment 基类。
 *
 * 功能：
 * - 懒加载
 * - 预加载控制
 * - 页面切换监听
 * - 可见性判断
 */
abstract class BaseViewPagerFragment<VB : ViewBinding> : BaseFragment<VB>() {

    /** 是否已经初始化过数据。 */
    private var isDataInitialized = false

    /** 是否对用户可见。 */
    private var isVisibleToUser = false

    /**
     * 懒加载数据。
     * 子类重写此方法实现首次可见时加载数据。
     */
    open fun lazyLoadData() {}

    /**
     * 页面重新可见时调用。
     */
    open fun onFragmentVisible() {}

    /**
     * 页面不可见时调用。
     */
    open fun onFragmentHidden() {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser

        if (isVisibleToUser && isResumed) {
            onVisible()
        } else if (!isVisibleToUser && isResumed) {
            onHidden()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (userVisibleHint && !isDataInitialized) {
            onVisible()
        }
    }

    /** 页面变为可见。 */
    private fun onVisible() {
        if (!isDataInitialized) {
            isDataInitialized = true
            lazyLoadData()
        }
        onFragmentVisible()
    }

    /** 页面变为不可见。 */
    private fun onHidden() {
        onFragmentHidden()
    }

    /**
     * 是否已经懒加载过。
     */
    fun isDataInitialized(): Boolean = isDataInitialized

    /**
     * 是否对用户可见。
     */
    fun isVisibleToUser(): Boolean = isVisibleToUser

    /**
     * 刷新数据（重新加载）。
     */
    open fun refreshData() {
        isDataInitialized = false
        if (isVisibleToUser) {
            onVisible()
        }
    }

    companion object {
        private const val STATE_IS_DATA_INITIALIZED = "state_is_data_initialized"
    }
}
