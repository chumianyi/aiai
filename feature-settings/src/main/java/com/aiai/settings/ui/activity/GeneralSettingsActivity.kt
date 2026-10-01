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
import com.aiai.settings.R
import com.aiai.settings.ui.view.CacheCleanView
import com.aiai.settings.ui.view.SettingSelectorView
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.viewmodel.GeneralSettingsViewModel

/**
 * 通用设置：语言、缓存、通知、自动更新、默认参数。
 */
class GeneralSettingsActivity : AppCompatActivity() {

    private lateinit var vm: GeneralSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[GeneralSettingsViewModel::class.java]

        val lang = findViewById<SettingSelectorView>(R.id.selectLanguage)
        lang.setEntries(listOf("跟随系统", "简体中文", "English", "日本語", "한국어"))

        val notif = findViewById<SettingSwitchView>(R.id.swNotification)
        val autoUpdate = findViewById<SettingSwitchView>(R.id.swAutoUpdate)
        val cacheView = findViewById<CacheCleanView>(R.id.cacheView)

        vm.uiState.observe(this) { s ->
            notif.setChecked(s.notification)
            autoUpdate.setChecked(s.autoUpdate)
            cacheView.setSize(s.cacheSize)
            s.cleanedMessage?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        notif.onCheckedChange = { vm.setNotification(it) }
        autoUpdate.onCheckedChange = { vm.setAutoUpdate(it) }
        findViewById<android.view.View>(R.id.btnCleanCache).setOnClickListener { vm.cleanCache() }
    }
}
