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

import com.aiai.common.ext.dp2px
import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 窗口 Insets 工具类。
 *
 * 提供沉浸式、键盘高度、安全区域等功能。
 */
object WindowInsetsUtil {

    /**
     * 键盘高度变化回调。
     */
    interface KeyboardHeightCallback {
        /**
         * 键盘高度变化。
         *
         * @param height 键盘高度（像素）
         * @param isVisible 键盘是否可见
         */
        fun onKeyboardHeightChanged(height: Int, isVisible: Boolean)
    }

    /**
     * 监听键盘高度变化。
     *
     * @param activity Activity
     * @param callback 高度回调
     * @return OnGlobalLayoutListener
     */
    fun addKeyboardHeightListener(
        activity: Activity,
        callback: KeyboardHeightCallback
    ): ViewTreeObserver.OnGlobalLayoutListener {
        val rootView = activity.window.decorView
        val visibleRect = Rect()

        val listener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var previousHeight = 0

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(visibleRect)
                val heightDiff = rootView.height - visibleRect.bottom

                if (heightDiff != previousHeight) {
                    previousHeight = heightDiff
                    val isVisible = heightDiff > dp2px(100)
                    callback.onKeyboardHeightChanged(heightDiff, isVisible)
                }
            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(listener)
        return listener
    }

    /**
     * 移除键盘高度监听。
     *
     * @param activity Activity
     * @param listener 要移除的监听
     */
    fun removeKeyboardHeightListener(
        activity: Activity,
        listener: ViewTreeObserver.OnGlobalLayoutListener
    ) {
        val rootView = activity.window.decorView
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(listener)
    }

    /**
     * 设置沉浸式状态栏（内容绘制到状态栏后面）。
     *
     * @param activity Activity
     * @param lightStatusBar 状态栏图标是否为深色
     */
    fun setImmersiveStatusBar(activity: Activity, lightStatusBar: Boolean = false) {
        val window = activity.window
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )

        val decor = window.decorView
        decor.systemUiVisibility = when {
            lightStatusBar -> {
                decor.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            else -> {
                decor.systemUiVisibility or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
        }
    }

    /**
     * 设置沉浸式状态栏和导航栏。
     *
     * @param activity Activity
     * @param lightStatusBar 状态栏图标是否为深色
     * @param lightNavBar 导航栏图标是否为深色
     */
    fun setImmersiveBoth(
        activity: Activity,
        lightStatusBar: Boolean = false,
        lightNavBar: Boolean = false
    ) {
        val window = activity.window
        val decor = window.decorView

        var flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (lightStatusBar) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        if (lightNavBar) {
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }

        decor.systemUiVisibility = flags
    }

    /**
     * 监听窗口 Insets。
     *
     * @param view 目标 View
     * @param listener Insets 回调
     */
    fun setOnApplyWindowInsetsListener(
        view: View,
        listener: (View, WindowInsetsCompat) -> WindowInsetsCompat
    ) {
        ViewCompat.setOnApplyWindowInsetsListener(view, listener)
    }

    /**
     * 获取状态栏高度。
     *
     * @param context 上下文
     * @return 状态栏高度（像素）
     */
    fun getStatusBarHeight(context: android.content.Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取导航栏高度。
     *
     * @param context 上下文
     * @return 导航栏高度（像素）
     */
    fun getNavigationBarHeight(context: android.content.Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 获取系统窗口内边距。
     *
     * @param view View
     * @return Insets
     */
    fun getSystemWindowInsets(view: View): Rect {
        val insets = ViewCompat.getRootWindowInsets(view)
        val result = Rect()
        insets?.let {
            result.left = it.systemWindowInsetLeft
            result.top = it.systemWindowInsetTop
            result.right = it.systemWindowInsetRight
            result.bottom = it.systemWindowInsetBottom
        }
        return result
    }

    /**
     * 适配状态栏（给 View 设置顶部 padding）。
     *
     * @param view 目标 View
     */
    fun fitStatusBar(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.setPadding(
                v.paddingLeft,
                insets.systemWindowInsetTop,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }
    }

    /**
     * 适配导航栏（给 View 设置底部 padding）。
     *
     * @param view 目标 View
     */
    fun fitNavigationBar(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.setPadding(
                v.paddingLeft,
                v.paddingTop,
                v.paddingRight,
                insets.systemWindowInsetBottom
            )
            insets
        }
    }

    /**
     * 同时适配状态栏和导航栏。
     *
     * @param view 目标 View
     */
    fun fitSystemBars(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            v.setPadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
            insets
        }
    }

    /**
     * 检查是否有刘海屏。
     *
     * @param activity Activity
     * @return true 表示有刘海
     */
    fun hasCutout(activity: Activity): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            val windowInsets = activity.window.decorView.rootWindowInsets
            val cutout = windowInsets?.displayCutout
            return cutout != null
        }
        return false
    }
}
