/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.PromptCategory

/**
 * 提示词分类 Adapter。
 */
class PromptCategoryAdapter(
    private val onClick: (PromptCategory?) -> Unit
) : RecyclerView.Adapter<PromptCategoryAdapter.VH>() {

    private val items = mutableListOf<PromptCategory?>()
    private var selected: PromptCategory? = null

    fun submit(list: List<PromptCategory?>, selected: PromptCategory?) {
        this.selected = selected
        val diff = Diff(items, list)
        val result = DiffUtil.calculateDiff(diff)
        items.clear(); items.addAll(list)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_prompt_category, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val tv: TextView = v.findViewById(R.id.tvCategory)
        fun bind(item: PromptCategory?) {
            tv.text = item?.displayName ?: "全部"
            tv.isSelected = item == selected
            itemView.setOnClickListener { onClick(item) }
        }
    }

    class Diff(private val old: List<PromptCategory?>, private val new: List<PromptCategory?>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o] == new[n]
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
