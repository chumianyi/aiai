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
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aiai.chat.R
import com.aiai.chat.data.model.ChatMessage

/**
 * 消息详情/引用Fragment
 *
 * 展示单条消息的详细信息，包括完整内容、元数据、引用上下文。
 */
class MessageDetailFragment : Fragment() {

    private lateinit var tvContent: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvModel: TextView
    private lateinit var tvTokens: TextView

    private var message: ChatMessage? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        message?.let { bindData(it) }
    }

    private fun initViews(view: View) {
        tvContent = view.findViewById(R.id.tv_detail_content)
        tvTime = view.findViewById(R.id.tv_detail_time)
        tvModel = view.findViewById(R.id.tv_detail_model)
        tvTokens = view.findViewById(R.id.tv_detail_tokens)
    }

    /**
     * 设置消息数据
     */
    fun setMessage(message: ChatMessage) {
        this.message = message
        if (::tvContent.isInitialized) {
            bindData(message)
        }
    }

    private fun bindData(message: ChatMessage) {
        tvContent.text = message.content
        tvTime.text = android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", message.createdAt)
        tvModel.text = "模型: ${message.modelName}"
        tvTokens.text = "Token: ${message.tokenCount}"
    }

    companion object {
        fun newInstance(message: ChatMessage): MessageDetailFragment {
            return MessageDetailFragment().apply {
                this.message = message
            }
        }
    }
}
