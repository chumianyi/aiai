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

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * Fragment扩展函数集合
 */

/**
 * Fragment显示Toast
 */
fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

/**
 * Fragment显示长Toast
 */
fun Fragment.toastLong(message: String) {
    toast(message, Toast.LENGTH_LONG)
}

/**
 * 跳转Activity（带参数）
 */
inline fun <reified T : android.app.Activity> Fragment.startActivity(vararg params: Pair<String, String>) {
    val intent = Intent(requireContext(), T::class.java)
    params.forEach { (key, value) ->
        intent.putExtra(key, value)
    }
    startActivity(intent)
}

/**
 * 打开网页
 */
fun Fragment.openUrl(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}
