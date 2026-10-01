/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.PromptTemplate

/**
 * 提示词列表 Adapter。
 */
class PromptAdapter(
    private val onClick: (PromptTemplate) -> Unit,
    private val onFav: (PromptTemplate) -> Unit
) : RecyclerView.Adapter<PromptAdapter.VH>() {

    private val list = mutableListOf<PromptTemplate>()

    fun submit(l: List<PromptTemplate>) {
        val diff = Diff(list, l)
        val result = DiffUtil.calculateDiff(diff)
        list.clear(); list.addAll(l)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.item_prompt, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val desc: TextView = v.findViewById(R.id.tvDesc)
        private val fav: ImageView = v.findViewById(R.id.ivFav)
        fun bind(item: PromptTemplate) {
            title.text = item.title
            desc.text = item.description.ifEmpty { item.content.take(60) }
            fav.setImageResource(if (item.isFavorite) R.drawable.ic_star else R.drawable.ic_star)
            itemView.setOnClickListener { onClick(item) }
            fav.setOnClickListener { onFav(item) }
        }
    }

    class Diff(private val old: List<PromptTemplate>, private val new: List<PromptTemplate>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].id == new[n].id
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
