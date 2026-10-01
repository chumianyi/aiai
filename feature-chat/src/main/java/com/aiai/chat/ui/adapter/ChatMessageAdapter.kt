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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.enums.MessageType
import com.aiai.chat.data.model.ChatMessage
import com.aiai.chat.ui.view.MarkdownTextView
import com.aiai.chat.ui.view.MessageStatusView
import com.aiai.chat.ui.view.TypingIndicatorView

/**
 * 聊天消息Adapter
 *
 * 支持多视图类型：文本/图片/文件/语音/代码/系统/错误/加载中。
 * 使用DiffUtil高效更新，支持流式输出的局部刷新。
 */
class ChatMessageAdapter(
    private val onMessageClickListener: ((ChatMessage) -> Unit)? = null,
    private val onMessageLongClickListener: ((ChatMessage) -> Unit)? = null,
    private val onRetryClickListener: ((ChatMessage) -> Unit)? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<ChatMessage>()

    companion object {
        private const val TYPE_TEXT_USER = 1
        private const val TYPE_TEXT_AI = 2
        private const val TYPE_IMAGE = 3
        private const val TYPE_FILE = 4
        private const val TYPE_VOICE = 5
        private const val TYPE_CODE = 6
        private const val TYPE_SYSTEM = 7
        private const val TYPE_ERROR = 8
        private const val TYPE_LOADING = 9
    }

    /**
     * 更新消息列表（带DiffUtil计算）
     */
    fun updateMessages(newMessages: List<ChatMessage>) {
        val diffCallback = ChatMessageDiffCallback(messages, newMessages)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        messages.clear()
        messages.addAll(newMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return when (message.type) {
            MessageType.TEXT -> if (message.isUserMessage()) TYPE_TEXT_USER else TYPE_TEXT_AI
            MessageType.IMAGE -> TYPE_IMAGE
            MessageType.FILE -> TYPE_FILE
            MessageType.VOICE -> TYPE_VOICE
            MessageType.CODE -> TYPE_CODE
            MessageType.SYSTEM -> TYPE_SYSTEM
            MessageType.ERROR -> TYPE_ERROR
            MessageType.LOADING -> TYPE_LOADING
            else -> TYPE_TEXT_AI
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_TEXT_USER -> {
                val view = inflater.inflate(R.layout.item_message_text, parent, false)
                TextMessageViewHolder(view, isUser = true)
            }
            TYPE_TEXT_AI -> {
                val view = inflater.inflate(R.layout.item_message_text, parent, false)
                TextMessageViewHolder(view, isUser = false)
            }
            TYPE_IMAGE -> {
                val view = inflater.inflate(R.layout.item_message_image, parent, false)
                ImageMessageViewHolder(view)
            }
            TYPE_FILE -> {
                val view = inflater.inflate(R.layout.item_message_file, parent, false)
                FileMessageViewHolder(view)
            }
            TYPE_VOICE -> {
                val view = inflater.inflate(R.layout.item_message_voice, parent, false)
                VoiceMessageViewHolder(view)
            }
            TYPE_CODE -> {
                val view = inflater.inflate(R.layout.item_message_code, parent, false)
                CodeMessageViewHolder(view)
            }
            TYPE_SYSTEM -> {
                val view = inflater.inflate(R.layout.item_message_system, parent, false)
                SystemMessageViewHolder(view)
            }
            TYPE_ERROR -> {
                val view = inflater.inflate(R.layout.item_message_error, parent, false)
                ErrorMessageViewHolder(view)
            }
            TYPE_LOADING -> {
                val view = inflater.inflate(R.layout.item_message_loading, parent, false)
                LoadingMessageViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_message_text, parent, false)
                TextMessageViewHolder(view, isUser = false)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is TextMessageViewHolder -> holder.bind(message)
            is ImageMessageViewHolder -> holder.bind(message)
            is FileMessageViewHolder -> holder.bind(message)
            is VoiceMessageViewHolder -> holder.bind(message)
            is CodeMessageViewHolder -> holder.bind(message)
            is SystemMessageViewHolder -> holder.bind(message)
            is ErrorMessageViewHolder -> holder.bind(message)
            is LoadingMessageViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    // === ViewHolders ===

    inner class TextMessageViewHolder(itemView: View, private val isUser: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val tvContent: MarkdownTextView = itemView.findViewById(R.id.tv_content)
        private val statusView: MessageStatusView = itemView.findViewById(R.id.view_status)

        fun bind(message: ChatMessage) {
            tvContent.setMarkdown(message.content)
            statusView.setStatus(message.status)

            itemView.setOnClickListener { onMessageClickListener?.invoke(message) }
            itemView.setOnLongClickListener {
                onMessageLongClickListener?.invoke(message)
                true
            }
        }
    }

    inner class ImageMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.setOnClickListener { onMessageClickListener?.invoke(message) }
        }
    }

    inner class FileMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.setOnClickListener { onMessageClickListener?.invoke(message) }
        }
    }

    inner class VoiceMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.setOnClickListener { onMessageClickListener?.invoke(message) }
        }
    }

    inner class CodeMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.setOnLongClickListener {
                onMessageLongClickListener?.invoke(message)
                true
            }
        }
    }

    inner class SystemMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSystem: TextView = itemView.findViewById(R.id.tv_system)
        fun bind(message: ChatMessage) {
            tvSystem.text = message.content
        }
    }

    inner class ErrorMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvError: TextView = itemView.findViewById(R.id.tv_error)
        private val btnRetry: TextView = itemView.findViewById(R.id.btn_retry)
        fun bind(message: ChatMessage) {
            tvError.text = message.errorMessage ?: message.content
            btnRetry.setOnClickListener { onRetryClickListener?.invoke(message) }
        }
    }

    inner class LoadingMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typingIndicator: TypingIndicatorView = itemView.findViewById(R.id.typing_indicator)
        fun bind(message: ChatMessage) {
            typingIndicator.setAnimating(true)
        }
    }

    /**
     * DiffUtil回调
     */
    class ChatMessageDiffCallback(
        private val oldList: List<ChatMessage>,
        private val newList: List<ChatMessage>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos].id == newList[newPos].id
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            val old = oldList[oldPos]
            val new = newList[newPos]
            return old.content == new.content &&
                   old.status == new.status &&
                   old.updatedAt == new.updatedAt
        }

        override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
            return super.getChangePayload(oldPos, newPos)
        }
    }
}
