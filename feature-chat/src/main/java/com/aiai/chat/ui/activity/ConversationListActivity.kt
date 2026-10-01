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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.ui.adapter.ConversationListAdapter
import com.aiai.chat.ui.viewmodel.ConversationListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 会话列表页Activity
 *
 * 展示所有会话卡片，支持搜索、新建、编辑模式、删除。
 */
@AndroidEntryPoint
class ConversationListActivity : AppCompatActivity() {

    private lateinit var viewModel: ConversationListViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConversationListAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnNew: FloatingActionButton
    private lateinit var btnEdit: ImageButton
    private lateinit var tvEditTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation_list)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[ConversationListViewModel::class.java]

        initViews()
        initObservers()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.rv_conversation_list)
        searchView = findViewById(R.id.search_view)
        btnNew = findViewById(R.id.btn_new_chat)
        btnEdit = findViewById(R.id.btn_edit)
        tvEditTitle = findViewById(R.id.tv_edit_title)

        adapter = ConversationListAdapter(
            onItemClick = { conversation ->
                val intent = Intent(this, ChatActivity::class.java).apply {
                    putExtra(ChatActivity.EXTRA_CONVERSATION_ID, conversation.id)
                    putExtra(ChatActivity.EXTRA_TITLE, conversation.title)
                }
                startActivity(intent)
            },
            onItemLongClick = { conversation ->
                viewModel.enterEditMode()
                viewModel.toggleSelect(conversation.id)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnNew.setOnClickListener {
            startActivity(Intent(this, NewConversationActivity::class.java))
        }

        btnEdit.setOnClickListener {
            if (viewModel.isEditMode.value) {
                viewModel.exitEditMode()
            } else {
                viewModel.enterEditMode()
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query ?: "")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText ?: "")
                return true
            }
        })
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.conversations.collect { list ->
                adapter.updateData(list)
            }
        }

        lifecycleScope.launch {
            viewModel.isEditMode.collect { editing ->
                tvEditTitle.visibility = if (editing) View.VISIBLE else View.GONE
                btnEdit.setImageResource(
                    if (editing) R.drawable.ic_close else R.drawable.ic_edit
                )
            }
        }
    }
}
