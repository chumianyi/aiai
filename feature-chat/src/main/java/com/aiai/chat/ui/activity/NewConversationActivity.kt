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

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aiai.chat.R
import com.aiai.chat.ui.viewmodel.NewConversationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * 新建对话页Activity
 *
 * 提供模型选择、系统提示词设置、参数调节功能。
 */
@AndroidEntryPoint
class NewConversationActivity : AppCompatActivity() {

    private lateinit var viewModel: NewConversationViewModel

    private lateinit var btnBack: ImageButton
    private lateinit var btnSelectModel: TextView
    private lateinit var etSystemPrompt: EditText
    private lateinit var sbTemperature: SeekBar
    private lateinit var tvTemperatureValue: TextView
    private lateinit var sbMaxTokens: SeekBar
    private lateinit var tvMaxTokensValue: TextView
    private lateinit var btnCreate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_conversation)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[NewConversationViewModel::class.java]

        initViews()
        initObservers()
        setupListeners()
    }

    private fun initViews() {
        btnBack = findViewById(R.id.btn_back)
        btnSelectModel = findViewById(R.id.tv_selected_model)
        etSystemPrompt = findViewById(R.id.et_system_prompt)
        sbTemperature = findViewById(R.id.sb_temperature)
        tvTemperatureValue = findViewById(R.id.tv_temperature_value)
        sbMaxTokens = findViewById(R.id.sb_max_tokens)
        tvMaxTokensValue = findViewById(R.id.tv_max_tokens_value)
        btnCreate = findViewById(R.id.btn_create)
    }

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.selectedModel.collect { model ->
                btnSelectModel.text = model?.name ?: "选择模型"
            }
        }

        lifecycleScope.launch {
            viewModel.temperature.collect { temp ->
                tvTemperatureValue.text = String.format("%.1f", temp)
                sbTemperature.progress = (temp * 10).toInt()
            }
        }

        lifecycleScope.launch {
            viewModel.maxTokens.collect { tokens ->
                tvMaxTokensValue.text = "$tokens"
                sbMaxTokens.progress = tokens / 100
            }
        }
    }

    private fun setupListeners() {
        btnBack.setOnClickListener { finish() }

        btnSelectModel.setOnClickListener {
            startActivity(Intent(this, ModelSelectorActivity::class.java))
        }

        sbTemperature.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onTemperatureChanged(progress / 10f)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        sbMaxTokens.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) viewModel.onMaxTokensChanged(progress * 100)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        etSystemPrompt.setOnEditorActionListener { _, _, _ ->
            viewModel.onSystemPromptChanged(etSystemPrompt.text.toString())
            false
        }

        btnCreate.setOnClickListener {
            viewModel.createConversation { convId ->
                val intent = Intent(this, ChatActivity::class.java).apply {
                    putExtra(ChatActivity.EXTRA_CONVERSATION_ID, convId)
                }
                startActivity(intent)
                finish()
            }
        }
    }
}
