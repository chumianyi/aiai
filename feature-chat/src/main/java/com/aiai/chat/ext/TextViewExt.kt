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

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * TextView/EditText扩展函数集合
 */

/**
 * 设置文本变化监听
 */
fun EditText.onTextChanged(listener: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener(s?.toString() ?: "")
        }
        override fun afterTextChanged(s: Editable?) {}
    })
}

/**
 * 获取编辑文本（去除首尾空格）
 */
fun EditText.textTrim(): String = text.toString().trim()

/**
 * 是否为空
 */
fun EditText.isEmpty(): Boolean = textTrim().isEmpty()

/**
 * 设置TextView文本（空安全）
 */
fun TextView.setTextSafe(text: String?) {
    this.text = text ?: ""
}

/**
 * 设置TextView可见性根据文本
 */
fun TextView.visibleIfNotEmpty() {
    visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
}
