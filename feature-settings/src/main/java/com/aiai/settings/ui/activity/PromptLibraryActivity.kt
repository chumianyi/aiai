/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.PromptCategory
import com.aiai.settings.ui.adapter.PromptAdapter
import com.aiai.settings.ui.adapter.PromptCategoryAdapter
import com.aiai.settings.ui.view.SearchBarView
import com.aiai.settings.viewmodel.PromptLibraryViewModel

/**
 * 提示词模板库：分类 + 搜索 + 列表 + 收藏。
 */
class PromptLibraryActivity : AppCompatActivity() {

    private lateinit var vm: PromptLibraryViewModel
    private lateinit var adapter: PromptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prompt_library)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[PromptLibraryViewModel::class.java]
        adapter = PromptAdapter(
            onClick = { /* 使用模板 */ },
            onFav = { vm.toggleFavorite(it.id) }
        )

        val rv = findViewById<RecyclerView>(R.id.rvPrompts)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        val categoryRv = findViewById<RecyclerView>(R.id.rvCategories)
        val catAdapter = PromptCategoryAdapter { vm.selectCategory(it) }
        categoryRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        categoryRv.adapter = catAdapter

        val search = findViewById<SearchBarView>(R.id.searchBar)
        search.onQueryChange = { vm.search(it) }

        vm.uiState.observe(this) { s ->
            adapter.submit(s.prompts)
            catAdapter.submit(listOf(null) + PromptCategory.entries, s.selectedCategory)
        }
    }
}
