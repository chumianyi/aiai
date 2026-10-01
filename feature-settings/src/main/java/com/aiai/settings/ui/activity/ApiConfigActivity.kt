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
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.ApiTestResultView
import com.aiai.settings.ui.view.PasswordInputView
import com.aiai.settings.viewmodel.ApiConfigViewModel

/**
 * API 配置页：填写接口地址、密钥、模型，支持测试与保存。
 */
class ApiConfigActivity : AppCompatActivity() {

    private lateinit var vm: ApiConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_api_config)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[ApiConfigViewModel::class.java]
        vm.load(intent.getStringExtra("config_id"))

        val etUrl = findViewById<EditText>(R.id.etUrl)
        val etModel = findViewById<EditText>(R.id.etModel)
        val pwd = findViewById<PasswordInputView>(R.id.pwdKey)
        val btnTest = findViewById<Button>(R.id.btnTest)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val resultView = findViewById<ApiTestResultView>(R.id.testResult)

        vm.uiState.observe(this) { s ->
            etUrl.setText(s.config.baseUrl)
            etModel.setText(s.config.modelName)
            if (pwd.getText().isEmpty()) pwd.setText(s.config.apiKey)
            s.testResult?.let { resultView.bind(it) }
            s.error?.let { Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }
            if (s.saveSuccess) {
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show()
                finish()
            }
            btnTest.isEnabled = !s.testing
        }

        etUrl.doOnTextChanged { vm.updateBaseUrl(it) }
        etModel.doOnTextChanged { vm.updateModel(it) }

        btnTest.setOnClickListener {
            vm.updateApiKey(pwd.getText())
            vm.test()
        }
        btnSave.setOnClickListener {
            vm.updateApiKey(pwd.getText())
            vm.save()
        }
    }

    private fun EditText.doOnTextChanged(action: (String) -> Unit) {
        addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) { action(s.toString()) }
        })
    }
}
