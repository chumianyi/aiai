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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aiai.chat.R

/**
 * 图片网格Adapter
 *
 * 以网格形式展示图片列表，用于图片选择和生成结果展示。
 */
class ImageGridAdapter(
    private val onImageClick: (String) -> Unit,
    private val onImageLongClick: (String) -> Unit = {}
) : RecyclerView.Adapter<ImageGridAdapter.ViewHolder>() {

    private val images = mutableListOf<String>()

    fun updateData(newImages: List<String>) {
        val diff = DiffUtil.calculateDiff(ImageDiffCallback(images, newImages))
        images.clear()
        images.addAll(newImages)
        diff.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_grid, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)

        fun bind(url: String) {
            // 实际项目中使用Glide加载
            itemView.setOnClickListener { onImageClick(url) }
            itemView.setOnLongClickListener {
                onImageLongClick(url)
                true
            }
        }
    }

    class ImageDiffCallback(
        private val oldList: List<String>,
        private val newList: List<String>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
        override fun areContentsTheSame(oldPos: Int, newPos: Int) =
            oldList[oldPos] == newList[newPos]
    }
}
