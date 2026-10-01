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
package com.aiai.chat.util

import android.content.Context
import android.widget.Toast

/**
 * Toast工具类
 *
 * 封装Toast显示，避免重复创建。
 */
object ToastUtil {

    private var toast: Toast? = null

    fun show(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) {
        toast?.cancel()
        toast = Toast.makeText(context.applicationContext, message, duration)
        toast?.show()
    }

    fun showShort(context: Context, message: String) {
        show(context, message, Toast.LENGTH_SHORT)
    }

    fun showLong(context: Context, message: String) {
        show(context, message, Toast.LENGTH_LONG)
    }

    fun cancel() {
        toast?.cancel()
        toast = null
    }
}
