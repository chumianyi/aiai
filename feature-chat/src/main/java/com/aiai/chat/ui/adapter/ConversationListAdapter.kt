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
import com.aiai.chat.data.model.Conversation
import com.aiai.chat.ui.view.ConversationCardView

/**
 * 会话列表Adapter
 *
 * 展示会话卡片列表，支持点击、长按、侧滑操作。
 */
class ConversationListAdapter(
    private val onItemClick: (Conversation) -> Unit,
    private val onItemLongClick: (Conversation) -> Unit = {},
    private val onDeleteClick: (Conversation) -> Unit = {},
    private val onPinClick: (Conversation) -> Unit = {}
) : RecyclerView.Adapter<ConversationListAdapter.ViewHolder>() {

    private val conversations = mutableListOf<Conversation>()

    fun updateData(newList: List<Conversation>) {
        val diff = DiffUtil.calculateDiff(ConvDiffCallback(conversations, newList))
        conversations.clear()
        conversations.addAll(newList)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_conversation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(conversations[position])
    }

    override fun getItemCount(): Int = conversations.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: ConversationCardView = itemView.findViewById(R.id.conversation_card)

        fun bind(conv: Conversation) {
            cardView.setCardData(
                title = conv.title,
                preview = conv.lastMessage,
                time = conv.getFormattedTime(),
                unread = conv.unreadCount,
                pinned = conv.isPinned
            )

            itemView.setOnClickListener { onItemClick(conv) }
            itemView.setOnLongClickListener {
                onItemLongClick(conv)
                true
            }
        }
    }

    class ConvDiffCallback(
        private val oldList: List<Conversation>,
        private val newList: List<Conversation>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos].id == newList[newPos].id
        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            val o = oldList[oldPos]
            val n = newList[newPos]
            return o.title == n.title &&
                   o.lastMessage == n.lastMessage &&
                   o.updatedAt == n.updatedAt &&
                   o.unreadCount == n.unreadCount &&
                   o.isPinned == n.isPinned
        }
    }
}
