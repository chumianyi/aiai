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

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 列表 ViewModel 基类。
 *
 * 功能：分页、下拉刷新、上拉加载更多。
 *
 * @param T 列表数据项类型
 */
abstract class BaseListViewModel<T> : BaseViewModel() {

    protected val _list = MutableStateFlow<List<T>>(emptyList())
    val list: StateFlow<List<T>> = _list.asStateFlow()

    protected val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    protected val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    protected var page = 1
    protected val pageSize = 20
    protected var hasMore = true

    /** 下拉刷新。 */
    open fun refresh() {
        page = 1
        hasMore = true
        _isRefreshing.value = true
        loadData()
    }

    /** 上拉加载更多。 */
    open fun loadMore() {
        if (!hasMore || _isLoadingMore.value) return
        _isLoadingMore.value = true
        page++
        loadData()
    }

    /** 加载数据（子类实现）。 */
    protected abstract fun loadData()
}
