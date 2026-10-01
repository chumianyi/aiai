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

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar

/**
 * Fragment 相关扩展函数集合。
 *
 * 提供 Fragment 事务封装、回退栈管理、显示隐藏切换、权限请求、
 * Toast / Snackbar 快捷调用等能力。
 */

// region Fragment 事务

/**
 * 使用 [containerId] 添加 Fragment [fragment]，可选是否加入回退栈。
 */
fun FragmentActivity.addFragment(
    containerId: Int,
    fragment: Fragment,
    tag: String? = fragment.javaClass.name,
    addToBackStack: Boolean = false,
    commit: FragmentTransaction.() -> Unit = {}
) {
    supportFragmentManager.beginTransaction().apply {
        add(containerId, fragment, tag)
        commit()
    }.also { if (addToBackStack) supportFragmentManager.popBackStack() }
}

/**
 * 使用 [clazz] 创建 Fragment 实例（通过空构造函数）并替换。
 */
inline fun <reified T : Fragment> FragmentActivity.replaceFragment(
    containerId: Int,
    addToBackStack: Boolean = true,
    noinline commit: (FragmentTransaction.() -> Unit)? = null
) {
    val fragment = T::class.java.getDeclaredConstructor().newInstance()
    supportFragmentManager.beginTransaction().apply {
        replace(containerId, fragment, T::class.java.name)
        commit?.invoke(this)
        if (addToBackStack) addToBackStack(T::class.java.name)
    }.commit()
}

/**
 * 在当前 Fragment 中添加子 Fragment。
 */
fun Fragment.addChildFragment(
    containerId: Int,
    fragment: Fragment,
    tag: String? = fragment.javaClass.name,
    addToBackStack: Boolean = false
) {
    childFragmentManager.beginTransaction().apply {
        add(containerId, fragment, tag)
        if (addToBackStack) addToBackStack(tag)
    }.commit()
}

/**
 * 在当前 Fragment 中替换子 Fragment。
 */
inline fun <reified T : Fragment> Fragment.replaceChildFragment(
    containerId: Int,
    addToBackStack: Boolean = true
) {
    val fragment = T::class.java.getDeclaredConstructor().newInstance()
    childFragmentManager.beginTransaction().apply {
        replace(containerId, fragment, T::class.java.name)
        if (addToBackStack) addToBackStack(T::class.java.name)
    }.commit()
}

/**
 * 回退栈出栈一层。
 */
fun FragmentActivity.popBackStack() {
    if (supportFragmentManager.backStackEntryCount > 0) {
        supportFragmentManager.popBackStack()
    }
}

/**
 * 弹出回退栈直到指定 [tag] 对应的 Fragment。
 */
fun FragmentActivity.popBackStackTo(tag: String, inclusive: Boolean = false) {
    val flags = if (inclusive) FragmentManager.POP_BACK_STACK_INCLUSIVE else 0
    supportFragmentManager.popBackStack(tag, flags)
}

/**
 * 清空整个回退栈。
 */
fun FragmentActivity.clearBackStack() {
    repeat(supportFragmentManager.backStackEntryCount) {
        supportFragmentManager.popBackStack()
    }
}

// endregion

// region 显示隐藏

/**
 * 显示隐藏 Fragment（基于 show / hide，不销毁实例）。
 */
fun FragmentTransaction.showFragment(fragment: Fragment): FragmentTransaction = show(fragment)

/**
 * 隐藏 Fragment。
 */
fun FragmentTransaction.hideFragment(fragment: Fragment): FragmentTransaction = hide(fragment)

/**
 * 同时显示 [show] 列表并隐藏 [hide] 列表。
 */
fun FragmentTransaction.switchFragments(show: List<Fragment>, hide: List<Fragment>) {
    show.forEach { show(it) }
    hide.forEach { hide(it) }
}

// endregion

//region Context 相关

/**
 * 获取 Fragment 持有 Activity 的 Context，类型安全。
 */
val Fragment.realContext: Context
    get() = context ?: requireContext()

/**
 * Fragment 中弹出 Toast。
 */
fun Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, text, duration).show()
}

/**
 * Fragment 中弹出长 Toast。
 */
fun Fragment.toastLong(text: String) = toast(text, Toast.LENGTH_LONG)

/**
 * 在根 View 上显示 Snackbar。
 */
fun Fragment.snackbar(text: String, duration: Int = Snackbar.LENGTH_SHORT) {
    val view = view ?: return
    Snackbar.make(view, text, duration).show()
}

/**
 * 显示带 Action 的 Snackbar。
 */
fun Fragment.snackbarAction(
    text: String,
    actionText: String,
    onAction: (View) -> Unit
) {
    val view = view ?: return
    Snackbar.make(view, text, Snackbar.LENGTH_LONG)
        .setAction(actionText, onAction)
        .show()
}

// endregion

// region 权限

/**
 * Fragment 中判断是否已授予权限。
 */
fun Fragment.hasPermission(permission: String): Boolean {
    return context?.checkSelfPermission(permission) == android.content.pm.PackageManager.PERMISSION_GRANTED
}

/**
 * Fragment 中请求权限，使用 [requestCode] 标识。
 */
fun Fragment.requestPermissions(
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
    pendingFragmentPermissionCallbacks[requestCode] = FragmentPermissionCallback(onGranted, onDenied)
}

/**
 * Fragment 中分发权限请求结果。
 */
fun Fragment.dispatchPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    val callback = pendingFragmentPermissionCallbacks.remove(requestCode) ?: return
    val denied = permissions.filterIndexed { index, _ ->
        grantResults.getOrNull(index) != android.content.pm.PackageManager.PERMISSION_GRANTED
    }
    if (denied.isEmpty()) callback.onGranted?.invoke() else callback.onDenied?.invoke(denied)
}

private val pendingFragmentPermissionCallbacks =
    mutableMapOf<Int, FragmentPermissionCallback>()

private data class FragmentPermissionCallback(
    val onGranted: (() -> Unit)?,
    val onDenied: ((List<String>) -> Unit)?
)

// endregion

// region 其他

/**
 * 安全执行 [block]，仅当 Fragment 仍已附加时。
 */
inline fun Fragment.runIfAttached(block: Fragment.() -> Unit) {
    if (isAdded && !isDetached && !isRemoving) block()
}

/**
 * 获取 Fragment 的可用 TAG，优先使用 arguments 中的 key。
 */
fun Fragment.fragmentTag(): String = tag ?: this::class.java.simpleName

/**
 * 检查 Fragment 是否处于可见状态（对用户可见）。
 */
fun Fragment.isViewVisible(): Boolean = isAdded && userVisibleHint && view != null

/**
 * 获取 Fragment 父 Activity 强转为 [T]，类型不匹配抛异常。
 */
inline fun <reified T> Fragment.parentActivity(): T {
    return requireActivity() as T
}

/**
 * 获取 Fragment 父 Fragment 强转为 [T]。
 */
inline fun <reified T> Fragment.parentFragmentOf(): T {
    return requireParentFragment() as T
}

// endregion
