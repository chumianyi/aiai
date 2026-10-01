/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.util

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

/**
 * 显示相关工具：dp/sp 转换、View 尺寸测量、Toast 快捷调用。
 */
object DisplayUtils {

    /** dp 转 px。 */
    fun dp(context: Context, value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)

    /** dp 转 px（Int）。 */
    fun dpInt(context: Context, value: Float): Int = dp(context, value).toInt()

    /** sp 转 px。 */
    fun sp(context: Context, value: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.resources.displayMetrics)

    /** 屏幕宽度 px。 */
    fun screenWidth(context: Context): Int = context.resources.displayMetrics.widthPixels

    /** 屏幕高度 px。 */
    fun screenHeight(context: Context): Int = context.resources.displayMetrics.heightPixels

    /** 屏幕密度。 */
    fun density(context: Context): Float = context.resources.displayMetrics.density

    /** 快捷 Toast。 */
    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    /** 设置 View 的 marginTop（px）。 */
    fun setMarginTop(view: View, marginPx: Int) {
        val lp = view.layoutParams as? ViewGroup.MarginLayoutParams ?: return
        lp.topMargin = marginPx
        view.layoutParams = lp
    }

    /** 设置 View 的 padding。 */
    fun setPadding(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        view.setPadding(left, top, right, bottom)
    }

    /** 获取状态栏高度。 */
    fun statusBarHeight(context: Context): Int {
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resId > 0) context.resources.getDimensionPixelSize(resId) else dpInt(context, 24f)
    }

    /** 判断是否为平板。 */
    fun isTablet(context: Context): Boolean {
        val size = context.resources.configuration.screenLayout and
            android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK
        return size >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /** 隐藏软键盘。 */
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? android.view.inputmethod.InputMethodManager
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
