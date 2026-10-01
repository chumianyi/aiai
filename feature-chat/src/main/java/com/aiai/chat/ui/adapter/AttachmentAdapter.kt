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
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R
import com.aiai.chat.data.enums.AttachmentType

/**
 * 附件选择Adapter
 *
 * 展示附件面板中的选项：图片、文件、拍照、语音等。
 */
class AttachmentAdapter(
    private val onItemClick: (AttachmentType) -> Unit
) : RecyclerView.Adapter<AttachmentAdapter.ViewHolder>() {

    data class AttachmentItem(
        val type: AttachmentType,
        val title: String,
        val iconRes: Int
    )

    private val items = listOf(
        AttachmentItem(AttachmentType.IMAGE, "图片", R.drawable.ic_image),
        AttachmentItem(AttachmentType.CAMERA, "拍照", R.drawable.ic_camera),
        AttachmentItem(AttachmentType.FILE, "文件", R.drawable.ic_file),
        AttachmentItem(AttachmentType.VOICE, "语音", R.drawable.ic_voice),
        AttachmentItem(AttachmentType.VIDEO, "视频", R.drawable.ic_video)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attachment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        private val tvTitle: TextView = itemView.findViewById(R.id.tv_title)

        fun bind(item: AttachmentItem) {
            ivIcon.setImageResource(item.iconRes)
            tvTitle.text = item.title
            itemView.setOnClickListener { onItemClick(item.type) }
        }
    }
}
