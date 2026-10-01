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
package com.aiai.settings.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import com.aiai.settings.R

/**
 * 引导页 View：多页滑动 + 指示器 + 跳过按钮。
 * 实际项目中作为欢迎页/更新引导页使用。
 */
class GuidePageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val pager: ViewPager2

    /** 跳过 / 完成回调。 */
    var onFinish: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_guide_page, this, true)
        pager = findViewById(R.id.viewPager)
        findViewById<android.widget.TextView>(R.id.tvSkip).setOnClickListener { onFinish?.invoke() }
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                // 更新指示器（占位）
            }
        })
    }

    /** 绑定页面标题列表。 */
    fun setPages(titles: List<String>) {
        // 实际项目用 FragmentStateAdapter，这里简化为标题
    }
}
