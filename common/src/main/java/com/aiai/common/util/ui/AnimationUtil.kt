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
package com.aiai.common.util.ui

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation

/**
 * 动画工具类。
 */
object AnimationUtil {

    /** 淡入动画。 */
    fun fadeIn(view: View, duration: Long = 300) {
        view.startAnimation(AlphaAnimation(0f, 1f).apply { this.duration = duration })
    }

    /** 淡出动画。 */
    fun fadeOut(view: View, duration: Long = 300) {
        view.startAnimation(AlphaAnimation(1f, 0f).apply { this.duration = duration })
    }

    /** 缩放动画。 */
    fun scale(view: View, from: Float, to: Float, duration: Long = 200) {
        view.startAnimation(ScaleAnimation(from, to, from, to).apply { this.duration = duration })
    }

    /** 从底部滑入。 */
    fun slideUp(view: View, duration: Long = 300) {
        view.startAnimation(TranslateAnimation(0f, 0f, view.height.toFloat(), 0f).apply { this.duration = duration })
    }

    /** 从顶部滑入。 */
    fun slideDown(view: View, duration: Long = 300) {
        view.startAnimation(TranslateAnimation(0f, 0f, -view.height.toFloat(), 0f).apply { this.duration = duration })
    }
}
