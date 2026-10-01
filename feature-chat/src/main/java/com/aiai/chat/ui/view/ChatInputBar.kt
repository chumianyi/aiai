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
package com.aiai.chat.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aiai.chat.R

/**
 * 聊天底部输入栏View
 *
 * 集成文字输入、语音切换、附件按钮、发送按钮。
 * 支持语音/文字模式切换，附件面板展开收起。
 */
class ChatInputBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var editText: AutoResizeEditText
    private lateinit var btnVoice: ImageView
    private lateinit var btnAttachment: ImageView
    private lateinit var btnSend: ImageButton
    private lateinit var btnEmoji: ImageView

    private var isVoiceMode: Boolean = false

    var onSendClickListener: ((String) -> Unit)? = null
    var onAttachmentClickListener: (() -> Unit)? = null
    var onVoiceClickListener: (() -> Unit)? = null
    var onTextChangeListener: ((String) -> Unit)? = null

    init {
        orientation = VERTICAL
        inflateView()
        setupListeners()
    }

    private fun inflateView() {
        LayoutInflater.from(context).inflate(R.layout.view_chat_input_bar, this, true)
        editText = findViewById(R.id.et_input)
        btnVoice = findViewById(R.id.btn_voice)
        btnAttachment = findViewById(R.id.btn_attachment)
        btnSend = findViewById(R.id.btn_send)
        btnEmoji = findViewById(R.id.btn_emoji)
    }

    private fun setupListeners() {
        btnSend.setOnClickListener {
            val text = editText.getInputText()
            if (text.isNotEmpty()) {
                onSendClickListener?.invoke(text)
                editText.clearInput()
            }
        }

        btnAttachment.setOnClickListener {
            onAttachmentClickListener?.invoke()
        }

        btnVoice.setOnClickListener {
            isVoiceMode = !isVoiceMode
            onVoiceClickListener?.invoke()
        }

        editText.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChangeListener?.invoke(s?.toString() ?: "")
                updateSendButtonState()
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun updateSendButtonState() {
        val hasText = editText.hasText()
        btnSend.isEnabled = hasText
        btnSend.alpha = if (hasText) 1.0f else 0.5f
    }

    /**
     * 获取输入框文本
     */
    fun getInput(): String = editText.getInputText()

    /**
     * 设置输入框文本
     */
    fun setInput(text: String) {
        editText.setText(text)
    }

    /**
     * 清空输入
     */
    fun clearInput() {
        editText.clearInput()
    }

    /**
     * 设置语音模式
     */
    fun setVoiceMode(voiceMode: Boolean) {
        isVoiceMode = voiceMode
    }

    /**
     * 请求输入框焦点
     */
    fun requestInputFocus() {
        editText.requestFocus()
    }
}
