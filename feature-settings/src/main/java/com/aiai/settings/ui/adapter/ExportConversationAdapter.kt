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
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.ExportConversation

/**
 * 导出会话选择 Adapter。
 */
class ExportConversationAdapter(
    private val onToggle: (ExportConversation) -> Unit
) : RecyclerView.Adapter<ExportConversationAdapter.VH>() {

    private val list = mutableListOf<ExportConversation>()

    fun submit(l: List<ExportConversation>) {
        val result = DiffUtil.calculateDiff(Diff(list, l))
        list.clear(); list.addAll(l)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_export_conversation, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val count: TextView = v.findViewById(R.id.tvCount)
        private val cb: CheckBox = v.findViewById(R.id.cbSelect)
        fun bind(item: ExportConversation) {
            title.text = item.title
            count.text = "${item.messageCount} 条消息"
            cb.isChecked = item.checked
            itemView.setOnClickListener { onToggle(item) }
        }
    }

    class Diff(private val old: List<ExportConversation>, private val new: List<ExportConversation>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].conversationId == new[n].conversationId
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
