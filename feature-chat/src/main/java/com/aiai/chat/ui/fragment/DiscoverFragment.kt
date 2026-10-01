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
import com.aiai.chat.ui.viewmodel.DiscoverViewModel
import kotlinx.coroutines.launch

/**
 * 发现页Fragment
 *
 * 展示功能入口网格、提示词推荐、插件推荐。
 */
class DiscoverFragment : Fragment() {

    private val viewModel: DiscoverViewModel by viewModels()

    private lateinit var rvPrompts: RecyclerView
    private lateinit var promptAdapter: PromptQuickAdapter
    private lateinit var tvFeatureGrid: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initObservers()
    }

    private fun initViews(view: View) {
        rvPrompts = view.findViewById(R.id.rv_discover_prompts)
        promptAdapter = PromptQuickAdapter { prompt ->
            // 使用提示词
        }
        rvPrompts.layoutManager = LinearLayoutManager(requireContext())
        rvPrompts.adapter = promptAdapter
    }

    private fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recommendedPrompts.collect { prompts ->
                promptAdapter.updateData(prompts)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.featureGrid.collect { features ->
                // 动态渲染功能入口
            }
        }
    }

    companion object {
        fun newInstance(): DiscoverFragment {
            return DiscoverFragment()
        }
    }
}
