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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.ui.adapter.SettingsAdapter
import com.aiai.settings.viewmodel.SettingsViewModel

/**
 * 设置主 Fragment（嵌入 SettingsActivity 或大屏容器）。
 */
class SettingsFragment : Fragment() {

    private lateinit var vm: SettingsViewModel
    private lateinit var adapter: SettingsAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        return i.inflate(R.layout.fragment_settings, c, false)
    }

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(this)[SettingsViewModel::class.java]
        adapter = SettingsAdapter { }
        v.findViewById<RecyclerView>(R.id.rvSettings).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@SettingsFragment.adapter
        }
        vm.items.observe(viewLifecycleOwner) { adapter.submit(it) }
    }
}
