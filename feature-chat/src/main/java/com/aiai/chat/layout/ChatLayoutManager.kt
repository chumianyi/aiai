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
package com.aiai.chat.layout

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 聊天列表自定义LayoutManager
 *
 * 继承LinearLayoutManager，优化聊天列表的滚动体验：
 * - 自动滚动到底部
 * - 滚动速度控制
 * - 预加载更多历史消息
 */
class ChatLayoutManager(
    context: Context
) : LinearLayoutManager(context, VERTICAL, false) {

    private var scrollSpeedFactor: Float = 1.0f

    /**
     * 滚动到指定位置并平滑滚动
     */
    fun smoothScrollToPosition(recyclerView: RecyclerView, position: Int) {
        val autoScroller = RecyclerView.SmoothScroller(
            recyclerView.context
        ).apply {
            targetPosition = position
        }
        startSmoothScroll(autoScroller)
    }

    /**
     * 滚动到底部
     */
    fun scrollToBottom(recyclerView: RecyclerView) {
        if (itemCount > 0) {
            scrollToPosition(itemCount - 1)
        }
    }

    /**
     * 平滑滚动到底部
     */
    fun smoothScrollToBottom(recyclerView: RecyclerView) {
        if (itemCount > 0) {
            smoothScrollToPosition(recyclerView, itemCount - 1)
        }
    }

    /**
     * 是否已经在底部
     */
    fun isAtBottom(): Boolean {
        return findLastVisibleItemPosition() >= itemCount - 1
    }

    /**
     * 设置滚动速度系数
     */
    fun setScrollSpeedFactor(factor: Float) {
        scrollSpeedFactor = factor.coerceIn(0.5f, 2.0f)
    }
}
