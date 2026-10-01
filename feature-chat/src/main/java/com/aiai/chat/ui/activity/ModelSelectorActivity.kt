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
import android.widget.ImageButton
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.ui.adapter.ModelListAdapter
import com.aiai.chat.ui.viewmodel.ModelSelectorViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 模型选择器Activity
 *
 * 展示模型列表、模型详情、参数配置，支持添加自定义模型。
 */
@AndroidEntryPoint
class ModelSelectorActivity : AppCompatActivity() {

    private lateinit var viewModel: ModelSelectorViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModelListAdapter
    private lateinit var searchView: SearchView
    private lateinit var btnBack: ImageButton
    private lateinit var btnAddCustom: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_model_selector)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[ModelSelectorViewModel::class.java]

        initViews()
        initObservers()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        recyclerView = findViewById(R.id.rv_model_list)
        searchView = findViewById(R.id.search_view)
        btnAddCustom = findViewById(R.id.btn_add_custom)

        adapter = ModelListAdapter(
            onModelClick = { model ->
                // 返回选中的模型
                intent.putExtra("selected_model_id", model.id)
                setResult(RESULT_OK, intent)
                finish()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnBack.setOnClickListener { finish() }

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

        btnAddCustom.setOnClickListener {
            viewModel.showAddCustomModel()
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.models.collect { models ->
                adapter.updateData(models)
            }
        }
    }
}
