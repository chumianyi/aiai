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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.FavoriteItem

/**
 * 收藏列表 Adapter。
 */
class FavoriteAdapter(
    private val onClick: (FavoriteItem) -> Unit,
    private val onRemove: (FavoriteItem) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.VH>() {

    private val list = mutableListOf<FavoriteItem>()

    fun submit(l: List<FavoriteItem>) {
        val result = DiffUtil.calculateDiff(Diff(list, l))
        list.clear(); list.addAll(l)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val summary: TextView = v.findViewById(R.id.tvSummary)
        private val remove: ImageView = v.findViewById(R.id.ivRemove)
        fun bind(item: FavoriteItem) {
            title.text = item.title
            summary.text = item.summary
            itemView.setOnClickListener { onClick(item) }
            remove.setOnClickListener { onRemove(item) }
        }
    }

    class Diff(private val old: List<FavoriteItem>, private val new: List<FavoriteItem>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].id == new[n].id
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
