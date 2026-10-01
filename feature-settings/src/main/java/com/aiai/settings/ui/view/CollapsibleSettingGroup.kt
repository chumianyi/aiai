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
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.aiai.settings.R

/**
 * 可折叠设置组：标题 + 箭头，内容区可展开收起。
 */
class CollapsibleSettingGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val titleView: TextView
    private val content: LinearLayout
    private var expanded = true

    init {
        orientation = VERTICAL
        inflate(context, R.layout.view_collapsible_group, this)
        titleView = findViewById(R.id.tvGroupTitle)
        content = findViewById(R.id.groupContent)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.CollapsibleSettingGroup)
            try {
                titleView.text = ta.getString(R.styleable.CollapsibleSettingGroup_groupTitle) ?: ""
                expanded = ta.getBoolean(R.styleable.CollapsibleSettingGroup_groupExpanded, true)
            } finally { ta.recycle() }
        }
        titleView.setOnClickListener { toggle() }
        applyState()
    }

    /** 添加子设置项到内容区。 */
    fun addContent(child: View) { content.addView(child) }

    private fun toggle() { expanded = !expanded; applyState() }

    private fun applyState() {
        content.visibility = if (expanded) VISIBLE else GONE
        titleView.rotation = if (expanded) 0f else -90f
    }
}
