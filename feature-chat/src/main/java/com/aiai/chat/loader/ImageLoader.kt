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
package com.aiai.chat.loader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.aiai.chat.R

/**
 * 图片加载器
 *
 * Glide封装，统一管理聊天中的图片加载，支持：
 * - 占位图、错误图
 * - 圆角处理
 * - 淡入淡出动画
 * - 圆形头像
 */
class ImageLoader(private val context: Context) {

    /**
     * 加载普通图片
     */
    fun loadImage(imageView: ImageView, url: String?) {
        Glide.with(context)
            .load(url)
            .placeholder(R.drawable.bg_image_placeholder)
            .error(R.drawable.bg_image_error)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(imageView)
    }

    /**
     * 加载圆角图片
     */
    fun loadRoundedImage(imageView: ImageView, url: String?, cornerRadius: Int = 12) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().circleCrop())
            .placeholder(R.drawable.bg_image_placeholder)
            .error(R.drawable.bg_image_error)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(imageView)
    }

    /**
     * 加载圆形头像
     */
    fun loadAvatar(imageView: ImageView, url: String?) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(R.drawable.ic_default_avatar)
            .error(R.drawable.ic_default_avatar)
            .into(imageView)
    }

    /**
     * 加载本地图片
     */
    fun loadLocalImage(imageView: ImageView, path: String?) {
        Glide.with(context)
            .load(path)
            .placeholder(R.drawable.bg_image_placeholder)
            .error(R.drawable.bg_image_error)
            .transition(DrawableTransitionOptions.withCrossFade(300))
            .into(imageView)
    }

    /**
     * 清除内存缓存
     */
    fun clearMemory() {
        Glide.get(context).clearMemory()
    }

    /**
     * 清除磁盘缓存
     */
    fun clearDiskCache() {
        Thread {
            Glide.get(context).clearDiskCache()
        }.start()
    }
}
