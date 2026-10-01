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
package com.aiai.core.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 列表 ViewModel 基类。
 *
 * 提供分页、刷新、加载更多。
 */
abstract class BaseListViewModel<T> : BaseViewModel() {

    protected val _list = MutableStateFlow<List<T>>(emptyList())
    val list: StateFlow<List<T>> = _list

    protected val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    protected val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore

    protected val _isEmpty = MutableStateFlow(false)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    protected var page = 1
    protected val pageSize = 20

    abstract suspend fun loadData()

    open fun refresh() {
        page = 1
        _isRefreshing.value = true
        launchWithLoading {
            loadData()
            _isRefreshing.value = false
        }
    }

    open fun loadMore() {
        page++
        _isLoadingMore.value = true
        launch {
            loadData()
            _isLoadingMore.value = false
        }
    }
}
