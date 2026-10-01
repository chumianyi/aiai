/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.aiai.settings.R

/**
 * 设置项组件：图标 + 标题 + 副标题 + 右侧控件占位 + 右箭头。
 *
 * 通过自定义属性 `settingIcon` / `settingTitle` / `settingSubtitle` /
 * `settingShowArrow` 在 XML 中配置；右侧可通过 [contentView] 塞入
 * Switch / Slider / 当前值等自定义控件。
 */
class SettingItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val icon: ImageView
    private val title: TextView
    private val subtitle: TextView
    private val arrow: ImageView
    private val divider: View
    private val contentHost: FrameLayout

    /** 右侧自定义控件容器，可 addView。 */
    val contentView: FrameLayout get() = contentHost

    init {
        LayoutInflater.from(context).inflate(R.layout.view_setting_item, this, true)
        icon = findViewById(R.id.ivIcon)
        title = findViewById(R.id.tvTitle)
        subtitle = findViewById(R.id.tvSubtitle)
        arrow = findViewById(R.id.ivArrow)
        divider = findViewById(R.id.divider)
        contentHost = findViewById(R.id.contentHost)

        attrs?.let { parseAttrs(it) }
        isClickable = true
        isFocusable = true
    }

    private fun parseAttrs(a: AttributeSet) {
        val ta: TypedArray = context.obtainStyledAttributes(a, R.styleable.SettingItemView)
        try {
            val iconRes = ta.getResourceId(R.styleable.SettingItemView_settingIcon, 0)
            if (iconRes != 0) icon.setImageResource(iconRes) else icon.visibility = GONE

            title.text = ta.getString(R.styleable.SettingItemView_settingTitle) ?: ""
            val sub = ta.getString(R.styleable.SettingItemView_settingSubtitle)
            if (sub.isNullOrBlank()) subtitle.visibility = GONE else subtitle.text = sub

            arrow.visibility = if (ta.getBoolean(R.styleable.SettingItemView_settingShowArrow, true)) VISIBLE else GONE
            divider.visibility = if (ta.getBoolean(R.styleable.SettingItemView_settingShowDivider, true)) VISIBLE else GONE

            val tint = ta.getColor(R.styleable.SettingItemView_settingIconTint, 0)
            if (tint != 0) icon.setColorFilter(tint)
        } finally {
            ta.recycle()
        }
    }

    /** 设置标题。 */
    fun setTitle(text: String) { title.text = text }

    /** 设置副标题，空串隐藏。 */
    fun setSubtitle(text: String) {
        if (text.isEmpty()) subtitle.visibility = GONE
        else { subtitle.visibility = VISIBLE; subtitle.text = text }
    }

    /** 是否显示箭头。 */
    fun showArrow(show: Boolean) { arrow.visibility = if (show) VISIBLE else GONE }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // 默认高度 56dp，宽度铺满父布局
        val height = (resources.getDimension(R.dimen.item_height)).toInt()
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }
}
