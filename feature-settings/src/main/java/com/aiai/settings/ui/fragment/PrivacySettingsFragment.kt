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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.viewmodel.PrivacySettingsViewModel

/**
 * 隐私设置 Fragment。
 */
class PrivacySettingsFragment : Fragment() {

    private lateinit var vm: PrivacySettingsViewModel

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_privacy_settings, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(this)[PrivacySettingsViewModel::class.java]
        val collect = v.findViewById<SettingSwitchView>(R.id.swDataCollect)
        val save = v.findViewById<SettingSwitchView>(R.id.swSaveChat)
        vm.uiState.observe(viewLifecycleOwner) { st ->
            collect.setChecked(st.dataCollection)
            save.setChecked(st.saveChatHistory)
        }
        collect.onCheckedChange = { vm.setDataCollection(it) }
        save.onCheckedChange = { vm.setSaveChatHistory(it) }
    }
}
