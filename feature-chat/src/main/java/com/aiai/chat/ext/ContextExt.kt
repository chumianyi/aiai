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
package com.aiai.chat.ext

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Context扩展函数集合
 *
 * 提供常用的UI操作便捷方法。
 */

/**
 * 显示短时间Toast
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * 显示长时间Toast
 */
fun Context.toastLong(message: String) {
    toast(message, Toast.LENGTH_LONG)
}

/**
 * Fragment显示Toast
 */
fun Fragment.toast(message: String) {
    requireContext().toast(message)
}

/**
 * dp转px
 */
fun Context.dp2px(dp: Float): Int {
    return (dp * resources.displayMetrics.density + 0.5f).toInt()
}

/**
 * px转dp
 */
fun Context.px2dp(px: Float): Int {
    return (px / resources.displayMetrics.density + 0.5f).toInt()
}

/**
 * sp转px
 */
fun Context.sp2px(sp: Float): Int {
    return (sp * resources.displayMetrics.scaledDensity + 0.5f).toInt()
}

/**
 * 显示Snackbar
 */
fun android.view.View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionText: String? = null,
    action: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, duration)
    if (actionText != null && action != null) {
        snackbar.setAction(actionText) { action() }
    }
    snackbar.show()
}
