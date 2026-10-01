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
package com.aiai.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.ui.adapter.PromptQuickAdapter
import com.aiai.chat.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

/**
 * 首页Fragment
 *
 * 展示推荐提示词、快捷入口、最近对话、使用统计。
 */
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var rvPrompts: RecyclerView
    private lateinit var promptAdapter: PromptQuickAdapter
    private lateinit var tvTodayCount: TextView
    private lateinit var tvTotalChats: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        rvPrompts = view.findViewById(R.id.rv_quick_prompts)
        tvTodayCount = view.findViewById(R.id.tv_today_count)
        tvTotalChats = view.findViewById(R.id.tv_total_chats)

        promptAdapter = PromptQuickAdapter { prompt ->
            // 点击提示词，发起新对话
        }
        rvPrompts.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvPrompts.adapter = promptAdapter
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.hotPrompts.collect { prompts ->
                promptAdapter.updateData(prompts)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.usageStats.collect { stats ->
                tvTodayCount.text = "${stats.todayMessages}"
                tvTotalChats.text = "${stats.totalConversations}"
            }
        }
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
