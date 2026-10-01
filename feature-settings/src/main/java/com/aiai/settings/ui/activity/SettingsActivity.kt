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

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.SettingItem
import com.aiai.settings.ui.adapter.SettingsAdapter
import com.aiai.settings.viewmodel.SettingsViewModel

/**
 * 设置主页 Activity。
 *
 * 展示所有设置入口分组，点击跳转到对应子页。
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var adapter: SettingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        val rv = findViewById<RecyclerView>(R.id.rvSettings)
        adapter = SettingsAdapter { item -> route(item) }
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        viewModel.items.observe(this) { list -> adapter.submit(list) }
    }

    private fun route(item: SettingItem) {
        when (item.key) {
            "api" -> startActivity(Intent(this, ApiConfigActivity::class.java))
            "key" -> startActivity(Intent(this, KeyManagementActivity::class.java))
            "general" -> startActivity(Intent(this, GeneralSettingsActivity::class.java))
            "appearance" -> startActivity(Intent(this, AppearanceSettingsActivity::class.java))
            "privacy" -> startActivity(Intent(this, PrivacySettingsActivity::class.java))
            "voice" -> startActivity(Intent(this, VoiceSettingsActivity::class.java))
            "image" -> startActivity(Intent(this, ImageGenSettingsActivity::class.java))
            "prompt" -> startActivity(Intent(this, PromptLibraryActivity::class.java))
            "favorite" -> startActivity(Intent(this, FavoriteManagerActivity::class.java))
            "export" -> startActivity(Intent(this, ExportChatActivity::class.java))
            "plugin" -> startActivity(Intent(this, PluginManagerActivity::class.java))
            "about" -> startActivity(Intent(this, AboutActivity::class.java))
            else -> Toast.makeText(this, "未实现: ${item.key}", Toast.LENGTH_SHORT).show()
        }
    }
}
