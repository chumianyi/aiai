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
package com.aiai.common.ext

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt

/**
 * Window 扩展函数集合。
 *
 * 提供状态栏设置、导航栏设置、软键盘模式、安全区域等常用 Window 操作。
 */

// region 状态栏

/**
 * 设置状态栏颜色。
 *
 * @param color 状态栏颜色
 * @param darkIcons 是否使用深色图标（浅色状态栏背景时使用）
 */
fun Window.setStatusBarColor(@ColorInt color: Int, darkIcons: Boolean = false) {
    statusBarColor = color
    setStatusBarDarkIcons(darkIcons)
}

/**
 * 设置状态栏图标深浅。
 *
 * @param dark 是否为深色图标
 */
fun Window.setStatusBarDarkIcons(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val decor = decorView
        decor.systemUiVisibility = if (dark) {
            decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            decor.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }
}

/**
 * 透明状态栏。
 *
 * @param darkIcons 状态栏图标是否为深色
 */
fun Window.transparentStatusBar(darkIcons: Boolean = false) {
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBarColor = Color.TRANSPARENT
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    setStatusBarDarkIcons(darkIcons)
}

/**
 * 隐藏状态栏（全屏）。
 */
fun Window.hideStatusBar() {
    decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
}

/**
 * 显示状态栏。
 */
fun Window.showStatusBar() {
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
}

/**
 * 状态栏是否可见。
 */
fun Window.isStatusBarVisible(): Boolean {
    return decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0
}

// endregion

// region 导航栏

/**
 * 设置导航栏颜色。
 *
 * @param color 导航栏颜色
 * @param darkIcons 是否使用深色图标
 */
fun Window.setNavigationBarColor(@ColorInt color: Int, darkIcons: Boolean = false) {
    navigationBarColor = color
    setNavigationBarDarkIcons(darkIcons)
}

/**
 * 设置导航栏图标深浅。
 *
 * @param dark 是否为深色图标
 */
fun Window.setNavigationBarDarkIcons(dark: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decor = decorView
        decor.systemUiVisibility = if (dark) {
            decor.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            decor.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
    }
}

/**
 * 透明导航栏。
 */
fun Window.transparentNavigationBar() {
    navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        isNavigationBarContrastEnforced = false
    }
}

/**
 * 隐藏导航栏（沉浸式）。
 */
fun Window.hideNavigationBar() {
    decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
}

/**
 * 显示导航栏。
 */
fun Window.showNavigationBar() {
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

/**
 * 导航栏是否可见。
 */
fun Window.isNavigationBarVisible(): Boolean {
    return decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
}

// endregion

// region 沉浸式

/**
 * 沉浸式状态栏和导航栏。
 *
 * @param statusBarDarkIcons 状态栏图标是否深色
 * @param navBarDarkIcons 导航栏图标是否深色
 */
fun Window.immersive(statusBarDarkIcons: Boolean = false, navBarDarkIcons: Boolean = false) {
    transparentStatusBar(statusBarDarkIcons)
    transparentNavigationBar()
    setNavigationBarDarkIcons(navBarDarkIcons)
}

/**
 * 全屏沉浸式（隐藏状态栏和导航栏）。
 */
fun Window.fullscreenImmersive() {
    setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
}

/**
 * 退出全屏沉浸式。
 */
fun Window.exitFullscreenImmersive() {
    clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

// endregion

// region 软键盘模式

/**
 * 设置软键盘调整模式为调整大小。
 */
fun Window.adjustResize() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

/**
 * 设置软键盘调整模式为平移。
 */
fun Window.adjustPan() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
}

/**
 * 设置软键盘隐藏模式。
 */
fun Window.adjustNothing() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
}

/**
 * 软键盘默认隐藏。
 */
fun Window.softInputHidden() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

/**
 * 软键盘默认可见。
 */
fun Window.softInputVisible() {
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
}

// endregion

// region 窗口属性

/**
 * 设置窗口背景亮度。
 *
 * @param brightness 亮度（0.0 - 1.0）
 */
fun Window.setWindowBrightness(brightness: Float) {
    val attrs = attributes
    attrs.screenBrightness = brightness
    attributes = attrs
}

/**
 * 恢复窗口默认亮度。
 */
fun Window.resetWindowBrightness() {
    val attrs = attributes
    attrs.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
    attributes = attrs
}

/**
 * 设置窗口动画。
 *
 * @param animRes 动画样式资源
 */
fun Window.setWindowAnimation(animRes: Int) {
    setWindowAnimations(animRes)
}

/**
 * 保持屏幕常亮。
 */
fun Window.keepScreenOn() {
    addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/**
 * 取消屏幕常亮。
 */
fun Window.clearKeepScreenOn() {
    clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
}

/**
 * 设置窗口透明度。
 *
 * @param alpha 透明度（0.0 - 1.0）
 */
fun Window.setWindowDimAmount(alpha: Float) {
    setDimAmount(alpha)
}

/**
 * 窗口背景模糊效果（API 31+）。
 *
 * @param blurRadius 模糊半径
 */
fun Window.setBackgroundBlurRadius(blurRadius: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        setBackgroundBlurRadius(blurRadius)
    }
}

// endregion

// region 安全区域

/**
 * 获取状态栏高度。
 *
 * @return 状态栏高度像素值
 */
fun Window.getStatusBarHeight(): Int {
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
 * @return 导航栏高度像素值
 */
fun Window.getNavigationBarHeight(): Int {
    var result = 0
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = context.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 获取屏幕宽度。
 *
 * @return 屏幕宽度像素值
 */
fun Window.getScreenWidth(): Int {
    return context.resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高度。
 *
 * @return 屏幕高度像素值
 */
fun Window.getScreenHeight(): Int {
    return context.resources.displayMetrics.heightPixels
}

/**
 * 设置 DecorView 内边距以适配状态栏。
 *
 * @param top 是否适配顶部
 * @param bottom 是否适配底部
 */
fun Window.fitSystemBars(top: Boolean = true, bottom: Boolean = false) {
    decorView.setOnApplyWindowInsetsListener { v, insets ->
        val statusBarHeight = insets.systemWindowInsetTop
        val navBarHeight = insets.systemWindowInsetBottom
        v.setPadding(
            0,
            if (top) statusBarHeight else 0,
            0,
            if (bottom) navBarHeight else 0
        )
        insets
    }
}

// endregion

// region Activity 相关

/**
 * 设置 Activity 全屏。
 */
fun Activity.setFullScreen() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

/**
 * 设置屏幕方向为竖屏。
 */
fun Activity.setPortrait() {
    requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * 设置屏幕方向为横屏。
 */
fun Activity.setLandscape() {
    requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * 解锁屏幕。
 */
fun Activity.unlockScreen() {
    window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
    window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
}

/**
 * 禁止截屏。
 */
fun Activity.preventScreenshot() {
    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

/**
 * 允许截屏。
 */
fun Activity.allowScreenshot() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

// endregion
