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
import com.aiai.chat.data.model.PromptSuggestion

/**
 * 快捷提示词Adapter
 *
 * 展示首页推荐的快捷提示词标签。
 */
class PromptQuickAdapter(
    private val onPromptClick: (PromptSuggestion) -> Unit
) : RecyclerView.Adapter<PromptQuickAdapter.ViewHolder>() {

    private val prompts = mutableListOf<PromptSuggestion>()

    fun updateData(newList: List<PromptSuggestion>) {
        val diff = DiffUtil.calculateDiff(PromptDiffCallback(prompts, newList))
        prompts.clear()
        prompts.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_prompt_quick, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(prompts[position])
    }

    override fun getItemCount(): Int = prompts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_prompt_title)

        fun bind(prompt: PromptSuggestion) {
            tvTitle.text = prompt.title
            itemView.setOnClickListener { onPromptClick(prompt) }
        }
    }

    class PromptDiffCallback(
        private val oldList: List<PromptSuggestion>,
        private val newList: List<PromptSuggestion>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
    }
}
