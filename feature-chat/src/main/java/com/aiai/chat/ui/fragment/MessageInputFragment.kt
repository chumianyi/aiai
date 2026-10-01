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
package com.aiai.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aiai.chat.R
import com.aiai.chat.ui.view.ChatInputBar

/**
 * 底部输入栏Fragment
 *
 * 封装聊天输入栏，支持文字输入、语音切换、附件、发送。
 */
class MessageInputFragment : Fragment() {

    private lateinit var inputBar: ChatInputBar

    var onSendMessageListener: ((String) -> Unit)? = null
    var onAttachmentClickListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        inputBar = view.findViewById(R.id.chat_input_bar)

        inputBar.onSendClickListener = { text ->
            onSendMessageListener?.invoke(text)
            inputBar.clearInput()
        }

        inputBar.onAttachmentClickListener = {
            onAttachmentClickListener?.invoke()
        }
    }

    /**
     * 获取输入栏文本
     */
    fun getInputText(): String {
        return inputBar.getInput()
    }

    /**
     * 清空输入
     */
    fun clearInput() {
        inputBar.clearInput()
    }

    companion object {
        fun newInstance(): MessageInputFragment {
            return MessageInputFragment()
        }
    }
}
