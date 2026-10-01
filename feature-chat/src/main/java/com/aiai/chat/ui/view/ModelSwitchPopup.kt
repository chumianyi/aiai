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
package com.aiai.chat.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.model.ModelConfig

/**
 * 模型切换弹窗
 *
 * 以PopupWindow形式显示可用模型列表，支持快速切换当前使用的模型。
 * 点击外部自动关闭。
 */
class ModelSwitchPopup(
    private val context: Context,
    private val models: List<ModelConfig>,
    private val currentModelId: String,
    private val onModelSelected: (ModelConfig) -> Unit
) : PopupWindow(context, null, 0) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ModelPopupAdapter

    init {
        initView()
    }

    private fun initView() {
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_model_switch, null)
        setContentView(contentView)

        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        animationStyle = R.style.PopupAnimation

        recyclerView = contentView.findViewById(R.id.rv_models)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ModelPopupAdapter(models, currentModelId) { model ->
            onModelSelected(model)
            dismiss()
        }
        recyclerView.adapter = adapter
    }

    /**
     * 显示在锚点View下方
     */
    fun showAsDropDown(anchor: View) {
        showAsDropDown(anchor, 0, 0)
    }

    /**
     * 更新模型列表
     */
    fun updateModels(models: List<ModelConfig>, currentModelId: String) {
        adapter.updateData(models, currentModelId)
    }

    /**
     * 模型弹窗列表Adapter
     */
    private class ModelPopupAdapter(
        private var models: List<ModelConfig>,
        private var currentModelId: String,
        private val onClick: (ModelConfig) -> Unit
    ) : RecyclerView.Adapter<ModelPopupAdapter.ViewHolder>() {

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvName: TextView = itemView.findViewById(R.id.tv_model_name)
            val tvDesc: TextView = itemView.findViewById(R.id.tv_model_desc)
            val tvBadge: TextView = itemView.findViewById(R.id.tv_badge)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_model, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val model = models[position]
            holder.tvName.text = model.name
            holder.tvDesc.text = model.description.take(30)

            if (model.id == currentModelId) {
                holder.tvBadge.visibility = View.VISIBLE
                holder.tvBadge.text = "使用中"
            } else {
                holder.tvBadge.visibility = View.GONE
            }

            holder.itemView.setOnClickListener { onClick(model) }
        }

        override fun getItemCount(): Int = models.size

        fun updateData(newModels: List<ModelConfig>, newCurrentId: String) {
            models = newModels
            currentModelId = newCurrentId
            notifyDataSetChanged()
        }
    }
}
