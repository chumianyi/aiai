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
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.settings.R
import com.aiai.settings.model.AppLanguage

/**
 * 语言选择 Adapter。
 */
class LanguageAdapter(
    private val onClick: (AppLanguage) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.VH>() {

    private val items = AppLanguage.entries.toList()
    private var selected: AppLanguage = AppLanguage.SYSTEM

    fun setSelected(lang: AppLanguage) { selected = lang; notifyDataSetChanged() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(LayoutInflater.from(parent.context).inflate(R.layout.item_language, parent, false))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        private val tv: TextView = v.findViewById(R.id.tvLang)
        fun bind(item: AppLanguage) {
            tv.text = item.displayName
            tv.setTypeface(null, if (item == selected) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
            itemView.setOnClickListener { onClick(item) }
        }
    }
}
