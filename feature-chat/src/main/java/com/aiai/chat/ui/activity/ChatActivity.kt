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
package com.aiai.chat.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.model.Conversation
import com.aiai.chat.ui.fragment.MessageInputFragment
import com.aiai.chat.ui.view.ChatInputBar
import com.aiai.chat.ui.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 聊天详情页Activity
 *
 * 包含消息列表、流式输出、输入栏、模型切换、更多操作。
 */
@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private lateinit var viewModel: ChatViewModel

    private lateinit var tvTitle: TextView
    private lateinit var tvModelName: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnMore: ImageButton
    private lateinit var btnModelSwitch: View
    private lateinit var recyclerView: RecyclerView

    private var conversationId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        conversationId = intent.getStringExtra(EXTRA_CONVERSATION_ID) ?: ""
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "新对话"

        viewModel = androidx.lifecycle.ViewModelProvider(this)[ChatViewModel::class.java]

        initViews()
        initObservers()

        // 初始化会话
        val conversation = Conversation(id = conversationId, title = title)
        viewModel.initConversation(conversation)
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tv_title)
        tvModelName = findViewById(R.id.tv_model_name)
        btnBack = findViewById(R.id.btn_back)
        btnMore = findViewById(R.id.btn_more)
        btnModelSwitch = findViewById(R.id.btn_model_switch)
        recyclerView = findViewById(R.id.rv_messages)

        btnBack.setOnClickListener { finish() }

        btnModelSwitch.setOnClickListener {
            // 显示模型切换面板
        }

        btnMore.setOnClickListener {
            showMoreActions()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                tvTitle.text = state.title
            }
        }

        lifecycleScope.launch {
            viewModel.currentModelName.collect { modelName ->
                tvModelName.text = modelName
            }
        }
    }

    private fun showMoreActions() {
        // 弹出操作菜单：清空对话、分享、设置等
    }

    companion object {
        const val EXTRA_CONVERSATION_ID = "extra_conversation_id"
        const val EXTRA_TITLE = "extra_title"
    }
}
