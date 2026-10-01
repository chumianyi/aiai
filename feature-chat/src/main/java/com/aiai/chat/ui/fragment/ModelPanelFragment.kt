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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.model.ModelConfig
import com.aiai.chat.ui.adapter.ModelListAdapter

/**
 * 模型切换面板Fragment
 *
 * 底部弹出的模型选择面板，快速切换当前对话使用的模型。
 */
class ModelPanelFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModelListAdapter
    private var currentModelId: String = ""

    var onModelSelectedListener: ((ModelConfig) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_model_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rv_model_list)
        adapter = ModelListAdapter(
            onModelClick = { model ->
                onModelSelectedListener?.invoke(model)
                dismiss()
            },
            currentModelId = currentModelId
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 加载模型列表
        adapter.updateData(ModelConfig.presetModels())
    }

    /**
     * 设置当前选中模型
     */
    fun setCurrentModel(modelId: String) {
        currentModelId = modelId
    }

    /**
     * 关闭面板
     */
    fun dismiss() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    companion object {
        fun newInstance(): ModelPanelFragment {
            return ModelPanelFragment()
        }
    }
}
