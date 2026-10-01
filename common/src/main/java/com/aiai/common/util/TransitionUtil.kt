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
package com.aiai.common.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair

/**
 * 转场动画工具类。
 *
 * 提供共享元素、淡入淡出、滑动、爆炸等转场动画功能。
 */
object TransitionUtil {

    /**
     * 淡入动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun fadeIn(duration: Long = 300): Animation {
        return AlphaAnimation(0f, 1f).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
        }
    }

    /**
     * 淡出动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun fadeOut(duration: Long = 300): Animation {
        return AlphaAnimation(1f, 0f).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    /**
     * 从下方滑入动画。
     *
     * @param duration 动画时长
     * @param distance 滑动距离（像素）
     * @return Animation
     */
    fun slideInFromBottom(duration: Long = 300, distance: Float = 200f): Animation {
        return TranslateAnimation(0f, 0f, distance, 0f).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
        }
    }

    /**
     * 向下滑出动画。
     *
     * @param duration 动画时长
     * @param distance 滑动距离（像素）
     * @return Animation
     */
    fun slideOutToBottom(duration: Long = 300, distance: Float = 200f): Animation {
        return TranslateAnimation(0f, 0f, 0f, distance).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    /**
     * 从右侧滑入动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun slideInFromRight(duration: Long = 300): Animation {
        return TranslateAnimation(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT.toFloat(),
            0f, 0f, 0f
        ).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
        }
    }

    /**
     * 向右侧滑出动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun slideOutToRight(duration: Long = 300): Animation {
        return TranslateAnimation(
            0f,
            android.view.ViewGroup.LayoutParams.MATCH_PARENT.toFloat(),
            0f, 0f
        ).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    /**
     * 缩放进入动画。
     *
     * @param duration 动画时长
     * @param fromScale 起始缩放比例
     * @return Animation
     */
    fun scaleIn(duration: Long = 300, fromScale: Float = 0.8f): Animation {
        return ScaleAnimation(
            fromScale, 1f,
            fromScale, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            this.duration = duration
            interpolator = DecelerateInterpolator()
        }
    }

    /**
     * 缩放退出动画。
     *
     * @param duration 动画时长
     * @param toScale 结束缩放比例
     * @return Animation
     */
    fun scaleOut(duration: Long = 300, toScale: Float = 1.1f): Animation {
        return ScaleAnimation(
            1f, toScale,
            1f, toScale,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            this.duration = duration
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    /**
     * 组合动画（淡入+缩放）。
     *
     * @param duration 动画时长
     * @return AnimationSet
     */
    fun fadeScaleIn(duration: Long = 300): AnimationSet {
        return AnimationSet(true).apply {
            addAnimation(fadeIn(duration))
            addAnimation(scaleIn(duration))
        }
    }

    /**
     * 组合动画（淡入+从下方滑入）。
     *
     * @param duration 动画时长
     * @return AnimationSet
     */
    fun fadeSlideIn(duration: Long = 300): AnimationSet {
        return AnimationSet(true).apply {
            addAnimation(fadeIn(duration))
            addAnimation(slideInFromBottom(duration))
        }
    }

    /**
     * 设置共享元素转场。
     *
     * @param activity Activity
     * @param sharedViews 共享 View 对列表
     * @return ActivityOptionsCompat
     */
    fun makeSceneTransitionOptions(
        activity: Activity,
        vararg sharedViews: Pair<View, String>
    ): ActivityOptionsCompat {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedViews)
    }

    /**
     * 设置 Activity 转场动画。
     *
     * @param activity Activity
     * @param enterAnim 进入动画资源
     * @param exitAnim 退出动画资源
     */
    fun overridePendingTransition(
        activity: Activity,
        enterAnim: Int,
        exitAnim: Int
    ) {
        activity.overridePendingTransition(enterAnim, exitAnim)
    }

    /**
     * 打开新 Activity 的默认转场动画（淡入淡出）。
     *
     * @param activity Activity
     */
    fun startActivityFadeTransition(activity: Activity) {
        overridePendingTransition(activity, android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * 关闭 Activity 的默认转场动画（淡入淡出）。
     *
     * @param activity Activity
     */
    fun finishFadeTransition(activity: Activity) {
        overridePendingTransition(activity, android.R.anim.fade_in, android.R.anim.fade_out)
    }

    /**
     * 打开新 Activity 的滑动转场动画。
     *
     * @param activity Activity
     */
    fun startActivitySlideTransition(activity: Activity) {
        overridePendingTransition(activity, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
    }

    /**
     * 为 View 设置入场动画。
     *
     * @param view 目标 View
     * @param animation 动画
     */
    fun startEnterAnimation(view: View, animation: Animation) {
        view.startAnimation(animation)
    }

    /**
     * 为 View 设置出场动画。
     *
     * @param view 目标 View
     * @param animation 动画
     * @param onEnd 动画结束回调
     */
    fun startExitAnimation(
        view: View,
        animation: Animation,
        onEnd: () -> Unit = {}
    ) {
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                onEnd()
            }
        })
        view.startAnimation(animation)
    }

    /**
     * 抖动动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun shake(duration: Long = 500): Animation {
        val animations = arrayOf(
            TranslateAnimation(0f, 10f, 0f, 0f),
            TranslateAnimation(0f, -10f, 0f, 0f),
            TranslateAnimation(0f, 10f, 0f, 0f),
            TranslateAnimation(0f, -10f, 0f, 0f),
            TranslateAnimation(0f, 5f, 0f, 0f),
            TranslateAnimation(0f, -5f, 0f, 0f)
        )

        val set = AnimationSet(true)
        animations.forEach { set.addAnimation(it) }
        set.duration = duration / animations.size
        return set
    }

    /**
     * 弹跳动画。
     *
     * @param duration 动画时长
     * @return Animation
     */
    fun bounce(duration: Long = 500): Animation {
        return TranslateAnimation(0f, 0f, 0f, -50f).apply {
            this.duration = duration / 2
            repeatCount = 1
            repeatMode = Animation.REVERSE
            interpolator = DecelerateInterpolator()
        }
    }
}
