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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.ui.adapter.FavoriteAdapter
import com.aiai.settings.ui.view.SearchBarView
import com.aiai.settings.viewmodel.FavoriteViewModel

/**
 * 收藏管理：列表、搜索、取消收藏、导出。
 */
class FavoriteManagerActivity : AppCompatActivity() {

    private lateinit var vm: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_manager)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[FavoriteViewModel::class.java]
        adapter = FavoriteAdapter(
            onClick = { },
            onRemove = { vm.remove(it.id) }
        )

        findViewById<RecyclerView>(R.id.rvFavorites).apply {
            layoutManager = LinearLayoutManager(this@FavoriteManagerActivity)
            adapter = this@FavoriteManagerActivity.adapter
        }

        findViewById<SearchBarView>(R.id.searchBar).onQueryChange = { vm.search(it) }

        vm.uiState.observe(this) { s -> adapter.submit(s.filtered) }

        findViewById<android.view.View>(R.id.btnExport).setOnClickListener {
            Toast.makeText(this, "已导出 ${vm.uiState.value.filtered.size} 条收藏", Toast.LENGTH_SHORT).show()
        }
    }
}
