/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.aiai.chat.R

/**
 * 状态布局容器
 *
 * 统一管理加载、空、错误、内容四种状态视图的切换。
 * 通过ID识别子View中的不同状态布局，自动切换可见性。
 *
 * 子View需通过ID区分：
 * - R.id.state_content: 内容视图
 * - R.id.state_loading: 加载视图
 * - R.id.state_empty: 空状态视图
 * - R.id.state_error: 错误视图
 */
class StateLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    /**
     * 状态枚举
     */
    enum class State {
        /** 加载中 */
        LOADING,

        /** 内容展示 */
        CONTENT,

        /** 空状态 */
        EMPTY,

        /** 错误状态 */
        ERROR
    }

    private var contentView: View? = null
    private var loadingView: View? = null
    private var emptyView: View? = null
    private var errorView: View? = null

    private var currentState: State = State.CONTENT

    var onRetryListener: (() -> Unit)? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        findStateViews()
        showState(currentState)
    }

    private fun findStateViews() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            when (child.id) {
                R.id.state_content -> contentView = child
                R.id.state_loading -> loadingView = child
                R.id.state_empty -> emptyView = child
                R.id.state_error -> errorView = child
            }
        }
        // 如果没有指定content id，默认第一个子View是内容
        if (contentView == null && childCount > 0) {
            contentView = getChildAt(0)
        }
    }

    /**
     * 显示指定状态
     */
    fun showState(state: State) {
        currentState = state
        contentView?.visibility = if (state == State.CONTENT) View.VISIBLE else View.GONE
        loadingView?.visibility = if (state == State.LOADING) View.VISIBLE else View.GONE
        emptyView?.visibility = if (state == State.EMPTY) View.VISIBLE else View.GONE
        errorView?.visibility = if (state == State.ERROR) View.VISIBLE else View.GONE

        if (state == State.ERROR) {
            errorView?.setOnClickListener { onRetryListener?.invoke() }
        }
    }

    /**
     * 显示加载状态
     */
    fun showLoading() = showState(State.LOADING)

    /**
     * 显示内容状态
     */
    fun showContent() = showState(State.CONTENT)

    /**
     * 显示空状态
     */
    fun showEmpty() = showState(State.EMPTY)

    /**
     * 显示错误状态
     */
    fun showError() = showState(State.ERROR)

    /**
     * 获取当前状态
     */
    fun getCurrentState(): State = currentState
}
