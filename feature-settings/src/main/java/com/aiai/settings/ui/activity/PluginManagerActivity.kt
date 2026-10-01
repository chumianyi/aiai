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
import com.aiai.settings.ui.adapter.PluginAdapter
import com.aiai.settings.viewmodel.PluginViewModel

/**
 * 插件管理页：发现、安装、启用。
 */
class PluginManagerActivity : AppCompatActivity() {

    private lateinit var vm: PluginViewModel
    private lateinit var adapter: PluginAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plugin_manager)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[PluginViewModel::class.java]
        adapter = PluginAdapter { plugin ->
            when (plugin.status) {
                com.aiai.settings.model.PluginStatus.NOT_INSTALLED -> vm.install(plugin.id)
                else -> vm.toggleEnabled(plugin)
            }
        }

        findViewById<RecyclerView>(R.id.rvPlugins).apply {
            layoutManager = LinearLayoutManager(this@PluginManagerActivity)
            adapter = this@PluginManagerActivity.adapter
        }

        vm.uiState.observe(this) { s -> adapter.submit(s.plugins) }
    }
}
