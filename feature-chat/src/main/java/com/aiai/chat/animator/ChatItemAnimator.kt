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
package com.aiai.chat.animator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * 聊天列表动画
 *
 * 自定义RecyclerView ItemAnimator，优化聊天消息的添加和更新动画：
 * - 新消息从底部滑入
 * - 更新消息淡入淡出
 * - 删除消息淡出
 * - 流式输出更新不闪烁
 */
class ChatItemAnimator : DefaultItemAnimator() {

    private val addDuration: Long = 300
    private val changeDuration: Long = 150
    private val removeDuration: Long = 200

    init {
        supportsChangeAnimations = true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.apply {
            alpha = 0f
            translationY = height / 4f
            animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(addDuration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        dispatchAddStarting(holder)
                    }
                    override fun onAnimationEnd(animation: Animator) {
                        dispatchAddFinished(holder)
                    }
                })
                .start()
        }
        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        holder.itemView.animate()
            .alpha(0f)
            .setDuration(removeDuration)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    dispatchRemoveStarting(holder)
                }
                override fun onAnimationEnd(animation: Animator) {
                    dispatchRemoveFinished(holder)
                }
            })
            .start()
        return false
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        // 流式输出更新时使用更轻量的动画
        if (oldHolder === newHolder) {
            newHolder.itemView.alpha = 0.8f
            newHolder.itemView.animate()
                .alpha(1f)
                .setDuration(changeDuration)
                .start()
        }
        dispatchChangeFinished(oldHolder, true)
        dispatchChangeFinished(newHolder, false)
        return false
    }

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        // 允许复用ViewHolder以实现流式输出的局部更新
        return true
    }
}
