/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
package com.aiai.common.util.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * 图片加载配置工具类（基于 Glide）。
 *
 * 封装常用的图片加载配置：圆角、占位图、错误图、圆形等。
 */
object ImageLoaderConfig {

    /** 默认占位图资源 id。 */
    var placeholderRes: Int = 0

    /** 默认错误图资源 id。 */
    var errorRes: Int = 0

    /**
     * 加载普通图片。
     */
    fun load(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().placeholder(placeholderRes).error(errorRes))
            .into(imageView)
    }

    /**
     * 加载圆形图片。
     */
    fun loadCircle(context: Context, url: String?, imageView: ImageView) {
        Glide.with(context)
            .load(url)
            .apply(RequestOptions().circleCrop().placeholder(placeholderRes).error(errorRes))
            .into(imageView)
    }

    /**
     * 加载圆角图片。
     */
    fun loadRounded(context: Context, url: String?, imageView: ImageView, radiusDp: Int) {
        Glide.with(context)
            .load(url)
            .apply(
                RequestOptions()
                    .circleCrop()
                    .placeholder(placeholderRes)
                    .error(errorRes)
            )
            .into(imageView)
    }
}
