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
package com.aiai.core.manager

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * 键盘管理器。
 *
 * 提供显示/隐藏、高度监听、模式切换等功能。
 */
class KeyboardManager(private val activity: Activity) {

    /** 键盘高度变化回调。 */
    interface OnKeyboardVisibilityListener {
        /**
         * 键盘可见性变化。
         *
         * @param isVisible 键盘是否可见
         * @param height 键盘高度
         */
        fun onKeyboardVisibilityChanged(isVisible: Boolean, height: Int)
    }

    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    /**
     * 显示软键盘。
     *
     * @param editText 目标输入框
     */
    fun showKeyboard(editText: EditText) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        editText.requestFocus()
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /** 隐藏软键盘。 */
    fun hideKeyboard() {
        val view = activity.currentFocus ?: activity.window.decorView
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /** 切换键盘状态。 */
    fun toggleKeyboard() {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    /**
     * 监听键盘可见性。
     *
     * @param listener 可见性回调
     */
    fun setOnKeyboardVisibilityListener(listener: OnKeyboardVisibilityListener) {
        val rootView = activity.window.decorView
        val visibleFrame = android.graphics.Rect()

        globalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private var previousHeight = 0

            override fun onGlobalLayout() {
                rootView.getWindowVisibleDisplayFrame(visibleFrame)
                val heightDiff = rootView.height - visibleFrame.bottom

                if (heightDiff != previousHeight) {
                    previousHeight = heightDiff
                    val isVisible = heightDiff > 200
                    listener.onKeyboardVisibilityChanged(isVisible, heightDiff)
                }
            }
        }

        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    /** 移除键盘监听。 */
    fun removeKeyboardListener() {
        globalLayoutListener?.let {
            activity.window.decorView.viewTreeObserver.removeOnGlobalLayoutListener(it)
            globalLayoutListener = null
        }
    }

    /**
     * 设置键盘调整模式为调整大小。
     */
    fun setAdjustResize() {
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    /**
     * 设置键盘调整模式为平移。
     */
    fun setAdjustPan() {
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    /**
     * 设置键盘默认隐藏。
     */
    fun setSoftInputHidden() {
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    /**
     * 设置键盘默认可见。
     */
    fun setSoftInputVisible() {
        activity.window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }
}
