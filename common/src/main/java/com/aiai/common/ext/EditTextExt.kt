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
package com.aiai.common.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * EditText 扩展函数集合。
 *
 * 提供文本变化监听、防抖、输入过滤、字数统计、清除按钮、焦点管理等
 * 常用 EditText 操作。
 */

// region 文本变化监听

/**
 * 监听文本变化。
 *
 * @param beforeTextChanged 文本变化前回调
 * @param onTextChanged 文本变化中回调
 * @param afterTextChanged 文本变化后回调
 * @return TextWatcher 实例，可用于移除监听
 */
fun EditText.addTextChangedListener(
    beforeTextChanged: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    onTextChanged: (CharSequence?, Int, Int, Int) -> Unit = { _, _, _, _ -> },
    afterTextChanged: (Editable?) -> Unit = {}
): TextWatcher {
    val watcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeTextChanged(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s, start, before, count)
        }

        override fun afterTextChanged(s: Editable?) {
            afterTextChanged(s)
        }
    }
    addTextChangedListener(watcher)
    return watcher
}

/**
 * 防抖监听文本变化。
 *
 * @param debounceTime 防抖时间（毫秒）
 * @param scope 协程作用域
 * @param onTextChanged 文本变化回调
 * @return TextWatcher 实例
 */
fun EditText.onTextChangedDebounce(
    debounceTime: Long = 300L,
    scope: CoroutineScope = MainScope(),
    onTextChanged: (String) -> Unit
): TextWatcher {
    var debounceJob: Job? = null
    return addTextChangedListener(
        onTextChanged = { s, _, _, _ ->
            debounceJob?.cancel()
            debounceJob = scope.launch {
                delay(debounceTime)
                onTextChanged(s?.toString() ?: "")
            }
        }
    )
}

/**
 * 监听编辑完成（输入框失去焦点或按下回车）。
 *
 * @param onEditorAction 编辑动作回调
 */
fun EditText.onEditorAction(
    onEditorAction: (String) -> Unit
) {
    setOnEditorActionListener { _, actionId, _ ->
        onEditorAction(text.toString())
        true
    }
}

// endregion

// region 输入过滤

/**
 * 设置最大输入长度。
 *
 * @param maxLength 最大长度
 */
fun EditText.setMaxLength(maxLength: Int) {
    filters = filters + android.text.InputFilter.LengthFilter(maxLength)
}

/**
 * 只允许输入数字。
 */
fun EditText.inputNumbersOnly() {
    inputType = android.text.InputType.TYPE_CLASS_NUMBER or
            android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
}

/**
 * 只允许输入手机号。
 */
fun EditText.inputPhoneNumber() {
    inputType = android.text.InputType.TYPE_CLASS_PHONE
    setMaxLength(11)
}

/**
 * 密码输入模式。
 *
 * @param visible 是否可见
 */
fun EditText.inputPassword(visible: Boolean = false) {
    inputType = if (visible) {
        android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
    } else {
        android.text.InputType.TYPE_CLASS_TEXT or
                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
    }
    setSelection(text.length)
}

/**
 * 邮箱输入模式。
 */
fun EditText.inputEmail() {
    inputType = android.text.InputType.TYPE_CLASS_TEXT or
            android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
}

/**
 * 网址输入模式。
 */
fun EditText.inputUrl() {
    inputType = android.text.InputType.TYPE_CLASS_TEXT or
            android.text.InputType.TYPE_TEXT_VARIATION_URI
}

/**
 * 多行文本输入模式。
 *
 * @param lines 行数
 */
fun EditText.inputMultiLine(lines: Int = 4) {
    inputType = android.text.InputType.TYPE_CLASS_TEXT or
            android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE
    setLines(lines)
}

// endregion

// region 字数统计

/**
 * 字数统计。
 *
 * @param counterView 显示字数的 TextView
 * @param maxLength 最大字数限制
 */
fun EditText.setupCharacterCounter(
    counterView: TextView,
    maxLength: Int
) {
    setMaxLength(maxLength)
    addTextChangedListener(
        onTextChanged = { s, _, _, _ ->
            val count = s?.length ?: 0
            counterView.text = "$count/$maxLength"
        }
    )
}

/**
 * 获取当前文本长度。
 */
fun EditText.textLength(): Int = text?.length ?: 0

/**
 * 是否为空。
 */
fun EditText.isEmpty(): Boolean = text?.isEmpty() ?: true

/**
 * 是否达到最大长度。
 *
 * @param maxLength 最大长度
 */
fun EditText.isAtMaxLength(maxLength: Int): Boolean = textLength() >= maxLength

// endregion

// region 清除按钮

/**
 * 设置清除按钮（文本不为空时显示）。
 *
 * @param clearIcon 清除图标资源
 * @param onCleared 清除完成回调
 */
fun EditText.setClearButton(
    clearIcon: Int = android.R.drawable.ic_menu_close_clear_cancel,
    onCleared: () -> Unit = {}
) {
    val drawable = context.getDrawable(clearIcon) ?: return
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    addTextChangedListener(
        onTextChanged = { s, _, _, _ ->
            setCompoundDrawablesRelative(null, null,
                if (s?.isNotEmpty() == true) drawable else null, null)
        }
    )

    setOnTouchListener { _, event ->
        if (compoundDrawablesRelative[2] != null && event.action == android.view.MotionEvent.ACTION_UP) {
            val touchX = event.x.toInt()
            val drawableWidth = drawable.intrinsicWidth
            if (touchX >= width - paddingRight - drawableWidth) {
                setText("")
                onCleared()
                return@setOnTouchListener true
            }
        }
        false
    }
}

// endregion

// region 焦点管理

/**
 * 请求焦点并弹出软键盘。
 */
fun EditText.requestFocusAndShowKeyboard() {
    requestFocus()
    post {
        val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as? android.view.inputmethod.InputMethodManager
        imm?.showSoftInput(this, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * 清除焦点并隐藏软键盘。
 */
fun EditText.clearFocusAndHideKeyboard() {
    clearFocus()
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
            as? android.view.inputmethod.InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 是否有焦点。
 */
fun EditText.isFocused(): Boolean = hasFocus()

/**
 * 失去焦点时隐藏软键盘。
 */
fun EditText.hideKeyboardOnFocusLost() {
    setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                    as? android.view.inputmethod.InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}

// endregion

// region 其他

/**
 * 选中文本。
 */
fun EditText.selectAll() {
    setSelection(0, text.length)
}

/**
 * 将光标移动到末尾。
 */
fun EditText.setSelectionToEnd() {
    setSelection(text.length)
}

/**
 * 复制文本到剪贴板。
 */
fun EditText.copyToClipboard() {
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as? android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("text", text)
    clipboard?.setPrimaryClip(clip)
}

/**
 * 从剪贴板粘贴文本。
 */
fun EditText.pasteFromClipboard() {
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as? android.content.ClipboardManager
    val clip = clipboard?.primaryClip
    if (clip != null && clip.itemCount > 0) {
        val pasteText = clip.getItemAt(0).text
        append(pasteText)
    }
}

/**
 * 选中文本剪切到剪贴板。
 */
fun EditText.cutSelectedText() {
    val selectedText = text?.substring(selectionStart, selectionEnd) ?: return
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as? android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("text", selectedText)
    clipboard?.setPrimaryClip(clip)
    text?.delete(selectionStart, selectionEnd)
}

/**
 * 选中文本复制到剪贴板。
 */
fun EditText.copySelectedText() {
    val selectedText = text?.substring(selectionStart, selectionEnd) ?: return
    val clipboard = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE)
            as? android.content.ClipboardManager
    val clip = android.content.ClipData.newPlainText("text", selectedText)
    clipboard?.setPrimaryClip(clip)
}

/**
 * 禁用输入。
 */
fun EditText.disableInput() {
    isFocusable = false
    isFocusableInTouchMode = false
    isCursorVisible = false
    keyListener = null
}

/**
 * 启用输入。
 */
fun EditText.enableInput() {
    isFocusable = true
    isFocusableInTouchMode = true
    isCursorVisible = true
}

/**
 * 设置占位文本颜色。
 *
 * @param color 颜色值
 */
fun EditText.setHintTextColorCompat(color: Int) {
    setHintTextColor(color)
}

/**
 * 验证输入是否为空。
 *
 * @param errorMessage 错误提示
 * @return true 表示输入有效
 */
fun EditText.validateNotEmpty(errorMessage: String = "输入不能为空"): Boolean {
    return if (text.isNullOrBlank()) {
        error = errorMessage
        requestFocus()
        false
    } else {
        true
    }
}

/**
 * 验证输入长度范围。
 *
 * @param minLength 最小长度
 * @param maxLength 最大长度
 * @param errorMessage 错误提示
 * @return true 表示输入有效
 */
fun EditText.validateLength(
    minLength: Int = 0,
    maxLength: Int = Int.MAX_VALUE,
    errorMessage: String = "输入长度不符合要求"
): Boolean {
    val length = textLength()
    return if (length < minLength || length > maxLength) {
        error = errorMessage
        requestFocus()
        false
    } else {
        true
    }
}

/**
 * 验证邮箱格式。
 *
 * @param errorMessage 错误提示
 * @return true 表示输入有效
 */
fun EditText.validateEmail(errorMessage: String = "邮箱格式不正确"): Boolean {
    val email = text.toString()
    val emailPattern = android.util.Patterns.EMAIL_ADDRESS
    return if (!emailPattern.matcher(email).matches()) {
        error = errorMessage
        requestFocus()
        false
    } else {
        true
    }
}

/**
 * 验证手机号格式。
 *
 * @param errorMessage 错误提示
 * @return true 表示输入有效
 */
fun EditText.validatePhone(errorMessage: String = "手机号格式不正确"): Boolean {
    val phone = text.toString()
    val phonePattern = android.util.Patterns.PHONE
    return if (!phonePattern.matcher(phone).matches() || phone.length != 11) {
        error = errorMessage
        requestFocus()
        false
    } else {
        true
    }
}

// endregion
