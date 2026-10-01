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
import android.widget.Toast

/**
 * 对话框快捷工具。
 */
object DialogUtils {

    /** 显示确认对话框。 */
    fun confirm(
        context: Context,
        title: String,
        message: String,
        positive: String = "确认",
        negative: String = "取消",
        onConfirm: () -> Unit
    ) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positive) { _, _ -> onConfirm() }
            .setNegativeButton(negative, null)
            .show()
    }

    /** 显示消息对话框。 */
    fun alert(context: Context, title: String, message: String) {
        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("好的", null)
            .show()
    }

    /** 显示 Toast。 */
    fun toast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}
