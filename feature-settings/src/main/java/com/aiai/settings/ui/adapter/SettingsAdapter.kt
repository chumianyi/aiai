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
import com.aiai.settings.model.SettingItem

/**
 * 设置列表 Adapter：支持 Header / Normal / Switch / Selector 多种 item 类型。
 */
class SettingsAdapter(
    private val onClick: (SettingItem) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<SettingItem>()

    fun submit(list: List<SettingItem>) {
        val diff = DiffUtil.calculateDiff(SettingsDiff(items, list))
        items.clear(); items.addAll(list)
        diff.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is SettingItem.Header -> TYPE_HEADER
            is SettingItem.Normal -> TYPE_NORMAL
            is SettingItem.Switch -> TYPE_SWITCH
            is SettingItem.Selector -> TYPE_SELECTOR
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inf = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderVH(inf.inflate(R.layout.item_setting_header, parent, false))
            TYPE_NORMAL -> NormalVH(inf.inflate(R.layout.item_setting_normal, parent, false))
            TYPE_SWITCH -> SwitchVH(inf.inflate(R.layout.item_setting_switch, parent, false))
            else -> SelectorVH(inf.inflate(R.layout.item_setting_selector, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is HeaderVH -> holder.bind(item as SettingItem.Header)
            is NormalVH -> holder.bind(item as SettingItem.Normal, onClick)
            is SwitchVH -> holder.bind(item as SettingItem.Switch, onClick)
            is SelectorVH -> holder.bind(item as SettingItem.Selector, onClick)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderVH(v: View) : RecyclerView.ViewHolder(v) {
        private val tv: TextView = v.findViewById(R.id.tvHeader)
        fun bind(item: SettingItem.Header) { tv.text = item.title }
    }

    class NormalVH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val subtitle: TextView = v.findViewById(R.id.tvSubtitle)
        fun bind(item: SettingItem.Normal, onClick: (SettingItem) -> Unit) {
            title.text = item.title
            subtitle.text = item.subtitle
            itemView.setOnClickListener { onClick(item) }
        }
    }

    class SwitchVH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val sw: android.widget.Switch = v.findViewById(R.id.sw)
        fun bind(item: SettingItem.Switch, onClick: (SettingItem) -> Unit) {
            title.text = item.title
            sw.isChecked = item.checked
            itemView.setOnClickListener { onClick(item) }
        }
    }

    class SelectorVH(v: View) : RecyclerView.ViewHolder(v) {
        private val title: TextView = v.findViewById(R.id.tvTitle)
        private val value: TextView = v.findViewById(R.id.tvValue)
        fun bind(item: SettingItem.Selector, onClick: (SettingItem) -> Unit) {
            title.text = item.title
            value.text = item.entries.getOrNull(item.selectedIndex) ?: ""
            itemView.setOnClickListener { onClick(item) }
        }
    }

    class SettingsDiff(
        private val old: List<SettingItem>,
        private val new: List<SettingItem>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size
        override fun getNewListSize() = new.size
        override fun areItemsTheSame(o: Int, n: Int) = old[o].key == new[n].key
        override fun areContentsTheSame(o: Int, n: Int) = old[o] == new[n]
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_NORMAL = 1
        private const val TYPE_SWITCH = 2
        private const val TYPE_SELECTOR = 3
    }
}
