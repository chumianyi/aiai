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

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.aiai.common.util.other.Logger

/**
 * 列表页基类 Fragment。
 *
 * 功能：下拉刷新、上拉加载、空状态、错误状态。
 */
abstract class BaseListFragment<VB : ViewBinding, VM : BaseListViewModel<*>> : BaseMvvmFragment<VB, VM>() {

    /** SwipeRefreshLayout 资源 id。 */
    protected abstract val swipeRefreshId: Int

    /** 空状态 View 资源 id。 */
    protected open val emptyViewId: Int = 0

    /** 错误状态 View 资源 id。 */
    protected open val errorViewId: Int = 0

    protected var swipeRefresh: SwipeRefreshLayout? = null

    override fun initView(view: View, savedInstanceState: Bundle?) {
        swipeRefresh = view.findViewById<View>(swipeRefreshId) as? SwipeRefreshLayout
        swipeRefresh?.setOnRefreshListener { viewModel.refresh() }
    }

    override fun showLoading() {
        swipeRefresh?.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefresh?.isRefreshing = false
    }

    override fun showError(message: String) {
        Logger.e("Error: $message")
    }

    /** 加载更多。 */
    open fun loadMore() {
        viewModel.loadMore()
    }
}
