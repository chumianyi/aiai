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

import android.text.SpannableString
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.model.ChatMessage

/**
 * 搜索结果Adapter
 *
 * 展示搜索命中的消息结果，关键字高亮显示。
 */
class SearchResultAdapter(
    private val onResultClick: (ChatMessage) -> Unit
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    private val results = mutableListOf<ChatMessage>()
    private var query: String = ""

    fun updateResults(newResults: List<ChatMessage>, query: String) {
        this.query = query
        val diff = DiffUtil.calculateDiff(SearchDiffCallback(results, newResults))
        results.clear()
        results.addAll(newResults)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position], query)
    }

    override fun getItemCount(): Int = results.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        private val tvConversation: TextView = itemView.findViewById(R.id.tv_conversation)

        fun bind(message: ChatMessage, query: String) {
            tvContent.text = highlightText(message.content, query)
            tvTime.text = android.text.format.DateFormat.format("MM-dd HH:mm", message.createdAt)

            itemView.setOnClickListener { onResultClick(message) }
        }

        private fun highlightText(text: String, query: String): CharSequence {
            if (query.isBlank()) return text
            val spannable = SpannableString(text)
            var start = text.indexOf(query, ignoreCase = true)
            while (start >= 0) {
                spannable.setSpan(
                    BackgroundColorSpan(0xFFFFEB3B.toInt()),
                    start,
                    start + query.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                start = text.indexOf(query, start + query.length, ignoreCase = true)
            }
            return spannable
        }
    }

    class SearchDiffCallback(
        private val oldList: List<ChatMessage>,
        private val newList: List<ChatMessage>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].content == newList[newPos].content
    }
}
