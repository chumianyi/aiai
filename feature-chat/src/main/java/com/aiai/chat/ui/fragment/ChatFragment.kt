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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.animator.ChatItemAnimator
import com.aiai.chat.layout.ChatLayoutManager
import com.aiai.chat.ui.adapter.ChatMessageAdapter
import com.aiai.chat.ui.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

/**
 * 聊天内容Fragment
 *
 * 可嵌入Activity的聊天消息列表Fragment，包含消息列表和输入栏。
 */
class ChatFragment : Fragment() {

    private val viewModel: ChatViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatMessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_messages)
        adapter = ChatMessageAdapter(
            onMessageClickListener = { message ->
                // 显示消息操作菜单
            },
            onRetryClickListener = { message ->
                viewModel.retryMessage(message.id)
            }
        )
        val layoutManager = ChatLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = ChatItemAnimator()
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collect { messages ->
                adapter.updateMessages(messages)
                if (messages.isNotEmpty()) {
                    recyclerView.scrollToPosition(messages.size - 1)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isGenerating.collect { generating ->
                // 更新输入栏发送按钮状态
            }
        }
    }

    /**
     * 发送消息
     */
    fun sendMessage(content: String) {
        viewModel.sendMessage(content)
    }

    companion object {
        fun newInstance(): ChatFragment {
            return ChatFragment()
        }
    }
}
