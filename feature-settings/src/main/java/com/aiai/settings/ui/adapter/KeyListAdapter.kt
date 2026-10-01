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
import com.aiai.settings.model.KeyInfo

/**
 * 密钥列表 Adapter。
 */
class KeyListAdapter(
    private val onDelete: (KeyInfo) -> Unit
) : RecyclerView.Adapter<KeyListAdapter.VH>() {

    private val list = mutableListOf<KeyInfo>()

    fun submit(l: List<KeyInfo>) {
        val result = DiffUtil.calculateDiff(Diff(list, l))
        list.clear(); list.addAll(l)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_key, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val alias: TextView = v.findViewById(R.id.tvAlias)
        private val provider: TextView = v.findViewById(R.id.tvProvider)
        private val masked: TextView = v.findViewById(R.id.tvMasked)
        private val del: ImageView = v.findViewById(R.id.ivDelete)
        fun bind(item: KeyInfo) {
            alias.text = item.alias
            provider.text = item.provider
            masked.text = item.encryptedValue
            del.setOnClickListener { onDelete(item) }
        }
    }

    class Diff(private val old: List<KeyInfo>, private val new: List<KeyInfo>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].id == new[n].id
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
