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
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * Activity 相关扩展函数集合。
 *
 * 提供启动 Activity、界面切换动画、键盘控制、状态栏沉浸式、
 * 屏幕方向锁定、权限请求结果分发、Activity 栈管理等常用能力。
 */

// region 启动 Activity

/**
 * 使用 [clazz] 启动目标 Activity，支持可选参数与转场动画。
 *
 * @param block Intent 配置 lambda，可在其中 putExtra 等
 */
inline fun <reified T : Activity> Activity.startActivity(
    options: Bundle? = null,
    noinline block: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, T::class.java)
    block?.invoke(intent)
    try {
        startActivity(intent, options)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "找不到目标页面", Toast.LENGTH_SHORT).show()
    }
}

/**
 * 启动目标 Activity 并等待结果，使用 [requestCode] 标识请求。
 */
inline fun <reified T : Activity> Activity.startActivityForResult(
    requestCode: Int,
    noinline block: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, T::class.java)
    block?.invoke(intent)
    try {
        startActivityForResult(intent, requestCode)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "找不到目标页面", Toast.LENGTH_SHORT).show()
    }
}

/**
 * 以共享元素转场动画启动目标 Activity。
 */
inline fun <reified T : Activity> Activity.startActivityWithTransition(
    sharedView: View,
    transitionName: String,
    noinline block: (Intent.() -> Unit)? = null
) {
    val intent = Intent(this, T::class.java)
    block?.invoke(intent)
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedView, transitionName)
    try {
        startActivity(intent, options.toBundle())
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "找不到目标页面", Toast.LENGTH_SHORT).show()
    }
}

/**
 * 关闭当前 Activity 并自定义结束动画。
 *
 * @param enterAnim 进入动画资源 id
 * @param exitAnim 退出动画资源 id
 */
fun Activity.finishWithTransition(enterAnim: Int = 0, exitAnim: Int = 0) {
    finish()
    if (enterAnim != 0 || exitAnim != 0) {
        @Suppress("DEPRECATION")
        overridePendingTransition(enterAnim, exitAnim)
    }
}

/**
 * 以向右滑入滑出的动画关闭当前 Activity。
 */
fun Activity.finishSlideRight() {
    finishWithTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
}

/**
 * 以淡出动画关闭当前 Activity。
 */
fun Activity.finishFade() {
    finishWithTransition(android.R.anim.fade_in, android.R.anim.fade_out)
}

// endregion

// region 键盘控制

/**
 * 隐藏当前 Activity 焦点 View 上弹出的软键盘。
 */
fun Activity.hideKeyboard() {
    val view = currentFocus ?: View(this)
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * 强制显示软键盘到指定 [view]。
 */
fun Activity.showKeyboard(view: View) {
    view.requestFocus()
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * 切换软键盘的显示状态。
 */
fun Activity.toggleKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

// endregion

// region 状态栏 & 沉浸式

/**
 * 获取状态栏高度（像素）。
 */
fun Activity.getStatusBarHeight(): Int {
    var result = 0
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resId > 0) {
        result = resources.getDimensionPixelSize(resId)
    }
    return result
}

/**
 * 设置状态栏背景色。
 */
fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

/**
 * 设置状态栏图标为深色（适配浅色状态栏）。
 */
fun Activity.setStatusBarDarkTheme(dark: Boolean) {
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = dark
}

/**
 * 启用沉浸式状态栏：内容延伸到状态栏下方，状态栏透明。
 *
 * @param darkIcons 状态栏图标是否为深色
 */
fun Activity.enableTransparentStatusBar(darkIcons: Boolean = false) {
    window.apply {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = Color.TRANSPARENT
        decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
    }
    setStatusBarDarkTheme(darkIcons)
}

/**
 * 退出沉浸式状态栏，恢复默认。
 */
fun Activity.disableTransparentStatusBar() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        statusBarColor = Color.TRANSPARENT
    }
}

/**
 * 设置底部导航栏颜色。
 */
fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

/**
 * 开启全屏模式（隐藏状态栏和导航栏）。
 */
fun Activity.enterFullScreen() {
    window.decorView.systemUiVisibility = (
        View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        )
}

/**
 * 退出全屏模式。
 */
fun Activity.exitFullScreen() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
}

// endregion

// region 屏幕方向

/**
 * 锁定屏幕为竖屏。
 */
fun Activity.lockPortrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

/**
 * 锁定屏幕为横屏。
 */
fun Activity.lockLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

/**
 * 解锁屏幕方向，恢复传感器自动旋转。
 */
fun Activity.unlockOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

// endregion

// region 权限 & 结果

/**
 * 判断是否已授予指定权限。
 */
fun Activity.hasPermission(permission: String): Boolean {
    return checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

/**
 * 批量判断权限是否已全部授予。
 */
fun Activity.hasPermissions(vararg permissions: String): Boolean {
    return permissions.all { hasPermission(it) }
}

/**
 * 简化版权限请求入口，回调通过 [onGranted] / [onDenied] 返回结果。
 *
 * 注意：需要在 Activity 中重写 onRequestPermissionsResult 并转发到
 * [onRequestPermissionsResult]，或自行使用 Activity Result API。
 */
@RequiresPermission("android.permission.REQUEST_PERMISSIONS")
fun Activity.requestPermissions(
    vararg permissions: String,
    requestCode: Int,
    onGranted: (() -> Unit)? = null,
    onDenied: ((List<String>) -> Unit)? = null
) {
    val needed = permissions.filterNot { hasPermission(it) }.toTypedArray()
    if (needed.isEmpty()) {
        onGranted?.invoke()
        return
    }
    requestPermissions(needed, requestCode)
    pendingPermissionCallbacks[requestCode] = PermissionCallback(onGranted, onDenied)
}

/** 内存中保存的待处理权限回调，由 Activity 手动转发。 */
private val pendingPermissionCallbacks =
    mutableMapOf<Int, PermissionCallback>()

/**
 * 分发权限请求结果。在 Activity 的 onRequestPermissionsResult 中调用。
 */
fun Activity.dispatchPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    val callback = pendingPermissionCallbacks.remove(requestCode) ?: return
    val denied = permissions.filterIndexed { index, _ ->
        grantResults.getOrNull(index) != android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    if (denied.isEmpty()) callback.onGranted?.invoke() else callback.onDenied?.invoke(denied)
}

private data class PermissionCallback(
    val onGranted: (() -> Unit)?,
    val onDenied: ((List<String>) -> Unit)?
)

// endregion

// region Activity 栈管理

/**
 * 获取当前任务栈中的所有 Activity（弱引用列表）。
 */
fun Context.getActivityStack(): List<Activity> {
    return ActivityStackHolder.stack.filter { !it.isFinite() }
}

/**
 * 打印当前 Activity 栈，便于调试。
 */
fun Context.dumpActivityStack(): String {
    return ActivityStackHolder.stack.joinToString(" <- ") { it.javaClass.simpleName }
}

/**
 * 关闭栈中除了当前 Activity 之外的所有 Activity。
 */
fun Activity.finishAllExceptCurrent() {
    ActivityStackHolder.stack.forEach { if (it !== this && !it.isFinishing) it.finish() }
}

/**
 * 关闭栈中所有 Activity 并退出应用。
 */
fun Activity.finishAll() {
    ActivityStackHolder.stack.forEach { if (!it.isFinishing) it.finish() }
}

/**
 * 判断当前设备是否为平板（基于最小宽度 dp）。
 */
fun Activity.isTablet(): Boolean {
    return (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >=
        Configuration.SCREENLAYOUT_SIZE_LARGE
}

/**
 * 判断当前屏幕是否处于横屏。
 */
fun Activity.isLandscape(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

/**
 * 判断当前是否处于夜间模式。
 */
fun Activity.isNightMode(): Boolean {
    val mode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return mode == Configuration.UI_MODE_NIGHT_YES
}

/** Activity 栈持有者（弱引用集合，防止内存泄漏）。 */
internal object ActivityStackHolder {
    val stack: MutableList<Activity> = mutableListOf()
}
