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
import com.aiai.chat.ui.adapter.ConversationListAdapter
import com.aiai.chat.ui.viewmodel.ConversationListViewModel
import kotlinx.coroutines.launch

/**
 * 聊天列表Fragment
 *
 * 在主界面中嵌入的聊天列表，展示所有会话。
 */
class ChatListFragment : Fragment() {

    private val viewModel: ConversationListViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_conversations)
        adapter = ConversationListAdapter(
            onItemClick = { conversation ->
                // 跳转到聊天页
            },
            onItemLongClick = { conversation ->
                viewModel.togglePin(conversation.id)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.conversations.collect { list ->
                adapter.updateData(list)
            }
        }
    }

    companion object {
        fun newInstance(): ChatListFragment {
            return ChatListFragment()
        }
    }
}
