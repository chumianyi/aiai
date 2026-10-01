/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.view

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import com.aiai.settings.R
import com.aiai.settings.manager.KeyEncryptionManager

/**
 * 密码 / API Key 输入框：
 * - 显示 / 隐藏切换；
 * - 实时显示密码强度（0~4）。
 */
class PasswordInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val edit: EditText
    private val toggle: ImageView
    private val strengthBar: ProgressBar

    private var visible = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_password_input, this, true)
        edit = findViewById(R.id.etPassword)
        toggle = findViewById(R.id.ivToggle)
        strengthBar = findViewById(R.id.pbStrength)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.PasswordInputView)
            try {
                edit.hint = ta.getString(R.styleable.PasswordInputView_pwdHint) ?: ""
                val showStrength = ta.getBoolean(R.styleable.PasswordInputView_pwdShowStrength, true)
                strengthBar.visibility = if (showStrength) VISIBLE else GONE
            } finally { ta.recycle() }
        }

        toggle.setOnClickListener {
            visible = !visible
            applyVisibility()
        }
        applyVisibility()

        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                strengthBar.progress = KeyEncryptionManager.passwordStrength(s?.toString() ?: "") * 25
            }
        })
    }

    private fun applyVisibility() {
        edit.inputType = if (visible)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        edit.setSelection(edit.text?.length ?: 0)
    }

    /** 获取输入文本。 */
    fun getText(): String = edit.text.toString()

    fun setText(text: String) { edit.setText(text) }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // 拦截触摸，避免父容器消费
        return super.onTouchEvent(event)
    }
}
