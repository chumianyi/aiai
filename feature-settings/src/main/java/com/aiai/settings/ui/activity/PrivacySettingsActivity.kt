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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.viewmodel.PrivacySettingsViewModel

/**
 * 隐私设置：数据收集、聊天记录保存、清除数据、隐私政策。
 */
class PrivacySettingsActivity : AppCompatActivity() {

    private lateinit var vm: PrivacySettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[PrivacySettingsViewModel::class.java]

        val dataCol = findViewById<SettingSwitchView>(R.id.swDataCollect)
        val saveChat = findViewById<SettingSwitchView>(R.id.swSaveChat)

        vm.uiState.observe(this) { s ->
            dataCol.setChecked(s.dataCollection)
            saveChat.setChecked(s.saveChatHistory)
            s.message?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
        }

        dataCol.onCheckedChange = { vm.setDataCollection(it) }
        saveChat.onCheckedChange = { vm.setSaveChatHistory(it) }

        findViewById<android.view.View>(R.id.btnClearData).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("清除全部数据")
                .setMessage("此操作不可恢复，确定继续？")
                .setPositiveButton("清除") { _, _ -> vm.clearAll() }
                .setNegativeButton("取消", null)
                .show()
        }
    }
}
