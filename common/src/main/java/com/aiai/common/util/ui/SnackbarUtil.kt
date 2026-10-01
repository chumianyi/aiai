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

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Snackbar 工具类。
 */
object SnackbarUtil {

    /** 短 Snackbar。 */
    fun show(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show()
    }

    /** 长 Snackbar。 */
    fun showLong(view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
    }

    /** 带 Action 的 Snackbar。 */
    fun showWithAction(view: View, text: String, actionText: String, onAction: (View) -> Unit) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG)
            .setAction(actionText, onAction)
            .show()
    }
}
