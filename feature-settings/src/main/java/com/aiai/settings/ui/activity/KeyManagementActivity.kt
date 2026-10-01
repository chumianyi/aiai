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
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.ui.adapter.KeyListAdapter
import com.aiai.settings.viewmodel.KeyManagementViewModel

/**
 * 密钥加密管理页：展示所有已保存密钥，支持删除、导出/导入。
 */
class KeyManagementActivity : AppCompatActivity() {

    private lateinit var vm: KeyManagementViewModel
    private lateinit var adapter: KeyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_management)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[KeyManagementViewModel::class.java]
        adapter = KeyListAdapter { vm.delete(it.id) }

        findViewById<RecyclerView>(R.id.rvKeys).apply {
            layoutManager = LinearLayoutManager(this@KeyManagementActivity)
            adapter = this@KeyManagementActivity.adapter
        }

        vm.uiState.observe(this) { s ->
            adapter.submit(s.keys)
            s.exportResult?.let { Toast.makeText(this, "已复制导出内容", Toast.LENGTH_LONG).show() }
            s.importResult?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
            s.error?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() }
        }

        findViewById<Button>(R.id.btnExport).setOnClickListener { vm.export("demo-password") }
        findViewById<Button>(R.id.btnAdd).setOnClickListener {
            startActivity(android.content.Intent(this, ApiConfigActivity::class.java))
        }
    }
}
