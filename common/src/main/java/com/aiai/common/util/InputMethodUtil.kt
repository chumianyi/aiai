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

import android.content.Context
import com.aiai.common.ext.dp2px
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 输入法工具类。
 *
 * 提供显示/隐藏、切换、监听等功能。
 */
object InputMethodUtil {

    /**
     * 显示软键盘。
     *
     * @param context 上下文
     * @param view 目标 View
     */
    fun showKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 隐藏软键盘。
     *
     * @param context 上下文
     * @param view 目标 View
     */
    fun hideKeyboard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 隐藏软键盘（通过 Activity）。
     *
     * @param activity Activity
     */
    fun hideKeyboard(activity: android.app.Activity) {
        val view = activity.currentFocus ?: activity.window.decorView
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * 切换软键盘状态。
     *
     * @param context 上下文
     */
    fun toggleKeyboard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    /**
     * 检查软键盘是否可见。
     *
     * @param context 上下文
     * @param rootView 根 View
     * @return true 表示可见
     */
    fun isKeyboardVisible(context: Context, rootView: View): Boolean {
        val heightDiff = rootView.rootView.height - rootView.height
        return heightDiff > dp2px(200)
    }

    /**
     * 获取当前输入法包名。
     *
     * @param context 上下文
     * @return 输入法包名
     */
    fun getCurrentInputMethod(context: Context): String {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return try {
            val binding = imm.javaClass.getMethod("getCurrentInputMethodBinding").invoke(imm)
            binding?.javaClass?.getMethod("getPackageName")?.invoke(binding) as? String ?: ""
        } catch (e: Exception) { "" }
    }

    /**
     * 跳转到输入法设置。
     *
     * @param context 上下文
     */
    fun openInputMethodSettings(context: Context) {
        val intent = android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 选择输入法。
     *
     * @param context 上下文
     */
    fun showInputMethodPicker(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showInputMethodPicker()
    }

    /**
     * 重启输入法。
     *
     * @param context 上下文
     */
    fun restartInputMethod(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.restartInput(null)
    }

    /**
     * 隐藏软键盘并清除焦点。
     *
     * @param context 上下文
     * @param view 目标 View
     */
    fun hideKeyboardAndClearFocus(context: Context, view: View) {
        hideKeyboard(context, view)
        view.clearFocus()
    }
}
