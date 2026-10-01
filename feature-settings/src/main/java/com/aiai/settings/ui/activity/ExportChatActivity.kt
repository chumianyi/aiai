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
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.ExportFormat
import com.aiai.settings.ui.adapter.ExportConversationAdapter
import com.aiai.settings.ui.view.ProgressRingView
import com.aiai.settings.viewmodel.ExportViewModel

/**
 * 导出聊天记录：选择会话、格式、进度、分享。
 */
class ExportChatActivity : AppCompatActivity() {

    private lateinit var vm: ExportViewModel
    private lateinit var adapter: ExportConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_export_chat)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[ExportViewModel::class.java]
        adapter = ExportConversationAdapter { vm.toggleSelect(it.conversationId) }

        findViewById<RecyclerView>(R.id.rvConversations).apply {
            layoutManager = LinearLayoutManager(this@ExportChatActivity)
            adapter = this@ExportChatActivity.adapter
        }

        val formatGroup = findViewById<RadioGroup>(R.id.rgFormat)
        findViewById<Button>(R.id.btnStart).setOnClickListener { vm.startExport() }

        vm.uiState.observe(this) { s ->
            adapter.submit(s.conversations)
            findViewById<ProgressRingView>(R.id.ringProgress).setProgress(s.progress.percent.toFloat())
        }

        formatGroup.setOnCheckedChangeListener { _, id ->
            val fmt = when (id) {
                R.id.rbTxt -> ExportFormat.TXT
                R.id.rbMd -> ExportFormat.MD
                R.id.rbJson -> ExportFormat.JSON
                else -> ExportFormat.PDF
            }
            vm.setFormat(fmt)
        }
    }
}
