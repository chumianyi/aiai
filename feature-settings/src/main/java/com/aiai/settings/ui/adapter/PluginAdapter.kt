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
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.PluginInfo
import com.aiai.settings.model.PluginStatus

/**
 * 插件列表 Adapter。
 */
class PluginAdapter(
    private val onAction: (PluginInfo) -> Unit
) : RecyclerView.Adapter<PluginAdapter.VH>() {

    private val list = mutableListOf<PluginInfo>()

    fun submit(l: List<PluginInfo>) {
        val result = DiffUtil.calculateDiff(Diff(list, l))
        list.clear(); list.addAll(l)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_plugin, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val icon: ImageView = v.findViewById(R.id.ivIcon)
        private val name: TextView = v.findViewById(R.id.tvName)
        private val desc: TextView = v.findViewById(R.id.tvDesc)
        private val status: TextView = v.findViewById(R.id.tvStatus)
        private val action: TextView = v.findViewById(R.id.tvAction)
        fun bind(item: PluginInfo) {
            name.text = item.name
            desc.text = item.description
            status.text = when (item.status) {
                PluginStatus.ENABLED -> "已启用"
                PluginStatus.INSTALLED -> "已安装"
                PluginStatus.INSTALLING -> "安装中…"
                PluginStatus.NOT_INSTALLED -> "未安装"
                else -> "异常"
            }
            action.text = when (item.status) {
                PluginStatus.NOT_INSTALLED -> "安装"
                PluginStatus.INSTALLED -> "启用"
                PluginStatus.ENABLED -> "停用"
                PluginStatus.INSTALLING -> "…"
                else -> "重试"
            }
            action.setOnClickListener { onAction(item) }
        }
    }

    class Diff(private val old: List<PluginInfo>, private val new: List<PluginInfo>) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].id == new[n].id
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }
}
