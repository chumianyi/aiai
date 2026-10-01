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
package com.aiai.common.util.ui

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * Dialog 工具类。
 */
object DialogUtil {

    /** 显示确认对话框。 */
    fun showConfirm(
        context: Context,
        title: String,
        message: String,
        positiveText: String = "确定",
        negativeText: String = "取消",
        onPositive: () -> Unit = {},
        onNegative: () -> Unit = {}
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText) { d, _ -> onPositive(); d.dismiss() }
            .setNegativeButton(negativeText) { d, _ -> onNegative(); d.dismiss() }
            .show()
    }

    /** 显示提示对话框。 */
    fun showAlert(context: Context, title: String, message: String, onOk: () -> Unit = {}) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("确定") { d, _ -> onOk(); d.dismiss() }
            .show()
    }
}
