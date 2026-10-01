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
package com.aiai.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.ApiTestResultView
import com.aiai.settings.ui.view.PasswordInputView
import com.aiai.settings.viewmodel.ApiConfigViewModel

/**
 * API 配置 Fragment。
 */
class ApiConfigFragment : Fragment() {

    private lateinit var vm: ApiConfigViewModel

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_api_config, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(this)[ApiConfigViewModel::class.java]
        vm.load(null)

        val etUrl = v.findViewById<EditText>(R.id.etUrl)
        val etModel = v.findViewById<EditText>(R.id.etModel)
        val pwd = v.findViewById<PasswordInputView>(R.id.pwdKey)

        v.findViewById<Button>(R.id.btnTest).setOnClickListener {
            vm.updateBaseUrl(etUrl.text.toString())
            vm.updateModel(etModel.text.toString())
            vm.updateApiKey(pwd.getText())
            vm.test()
        }
        v.findViewById<Button>(R.id.btnSave).setOnClickListener {
            vm.updateBaseUrl(etUrl.text.toString())
            vm.updateModel(etModel.text.toString())
            vm.updateApiKey(pwd.getText())
            vm.save()
        }

        vm.uiState.observe(viewLifecycleOwner) { st ->
            v.findViewById<ApiTestResultView>(R.id.testResult)
                .bind(st.testResult ?: return@observe)
        }
    }
}
