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
package com.aiai.chat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.model.ModelConfig

/**
 * 模型列表Adapter
 *
 * 展示可用AI模型列表，支持选择、详情查看。
 */
class ModelListAdapter(
    private val onModelClick: (ModelConfig) -> Unit,
    private val currentModelId: String = ""
) : RecyclerView.Adapter<ModelListAdapter.ViewHolder>() {

    private val models = mutableListOf<ModelConfig>()

    fun updateData(newList: List<ModelConfig>) {
        val diff = DiffUtil.calculateDiff(ModelDiffCallback(models, newList))
        models.clear()
        models.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(models[position])
    }

    override fun getItemCount(): Int = models.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_model_name)
        private val tvDesc: TextView = itemView.findViewById(R.id.tv_model_desc)
        private val tvBadge: TextView = itemView.findViewById(R.id.tv_badge)
        private val tvProvider: TextView = itemView.findViewById(R.id.tv_provider)

        fun bind(model: ModelConfig) {
            tvName.text = model.name
            tvDesc.text = model.description
            tvProvider.text = model.provider

            if (model.id == currentModelId) {
                tvBadge.visibility = View.VISIBLE
                tvBadge.text = "使用中"
            } else if (model.badges.isNotEmpty()) {
                tvBadge.visibility = View.VISIBLE
                tvBadge.text = model.badges.first()
            } else {
                tvBadge.visibility = View.GONE
            }

            itemView.setOnClickListener { onModelClick(model) }
        }
    }

    class ModelDiffCallback(
        private val oldList: List<ModelConfig>,
        private val newList: List<ModelConfig>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos] == newList[newPos]
        }
    }
}
