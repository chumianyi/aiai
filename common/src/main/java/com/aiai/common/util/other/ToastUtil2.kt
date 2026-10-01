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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.common.util.other

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Toast 工具类（封装，避免重复弹出）。
 */
object ToastUtil {

    private var toast: Toast? = null
    private val handler = Handler(Looper.getMainLooper())

    /** 短时间 Toast。 */
    fun show(context: Context, message: String) {
        handler.post {
            toast?.cancel()
            toast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT).apply { show() }
        }
    }

    /** 长时间 Toast。 */
    fun showLong(context: Context, message: String) {
        handler.post {
            toast?.cancel()
            toast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_LONG).apply { show() }
        }
    }

    /** 资源 ID Toast。 */
    fun show(context: Context, resId: Int) {
        show(context, context.getString(resId))
    }
}
