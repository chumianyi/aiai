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
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.ui.adapter.SearchResultAdapter
import com.aiai.chat.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 对话搜索页Activity
 *
 * 提供搜索框、历史搜索、搜索结果、结果高亮。
 */
@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel

    private lateinit var searchView: SearchView
    private lateinit var btnBack: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchResultAdapter
    private lateinit var tvHistoryTitle: TextView
    private lateinit var rvHistory: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[SearchViewModel::class.java]

        initViews()
        initObservers()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        searchView = findViewById(R.id.search_view)
        recyclerView = findViewById(R.id.rv_search_results)
        tvHistoryTitle = findViewById(R.id.tv_history_title)

        adapter = SearchResultAdapter { message ->
            // 跳转到对应聊天位置
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnBack.setOnClickListener { finish() }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.onSearchQueryChanged(query ?: "")
                viewModel.search()
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.onSearchQueryChanged(newText ?: "")
                return true
            }
        })

        searchView.isIconified = false
        searchView.requestFocus()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.searchResults.collect { results ->
                adapter.updateResults(results, viewModel.searchQuery.value)
            }
        }

        lifecycleScope.launch {
            viewModel.hasSearched.collect { hasSearched ->
                tvHistoryTitle.visibility = if (hasSearched) View.GONE else View.VISIBLE
            }
        }
    }
}
