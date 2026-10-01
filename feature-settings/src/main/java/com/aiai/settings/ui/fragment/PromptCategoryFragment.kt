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
import com.aiai.settings.model.PromptCategory
import com.aiai.settings.ui.adapter.PromptAdapter
import com.aiai.settings.viewmodel.PromptLibraryViewModel

/**
 * 提示词分类 Fragment：展示某一分类下的提示词列表。
 */
class PromptCategoryFragment : Fragment() {

    private lateinit var vm: PromptLibraryViewModel
    private lateinit var adapter: PromptAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_prompt_category, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(requireActivity())[PromptLibraryViewModel::class.java]
        adapter = PromptAdapter({ }, { vm.toggleFavorite(it.id) })
        v.findViewById<RecyclerView>(R.id.rvPrompts).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@PromptCategoryFragment.adapter
        }
        vm.uiState.observe(viewLifecycleOwner) { st -> adapter.submit(st.prompts) }
    }

    companion object {
        fun newInstance(category: PromptCategory): PromptCategoryFragment {
            return PromptCategoryFragment().apply {
                arguments = Bundle().apply { putString("category", category.name) }
            }
        }
    }
}
