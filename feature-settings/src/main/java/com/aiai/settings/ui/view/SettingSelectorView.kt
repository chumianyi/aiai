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

import android.app.AlertDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.aiai.settings.R

/**
 * 选择器设置项：标题 + 当前值 + 箭头，点击弹出列表 Dialog。
 */
class SettingSelectorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : SettingItemView(context, attrs, defStyle) {

    private var entries: List<String> = emptyList()
    private var selectedIndex: Int = 0

    /** 选中回调。 */
    var onSelect: ((index: Int, text: String) -> Unit)? = null

    init {
        val tv = TextView(context).apply {
            textSize = 14f
            setTextColor(context.getColor(R.color.text_secondary))
        }
        contentView.addView(tv)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.SettingSelectorView)
            try {
                setTitle(ta.getString(R.styleable.SettingSelectorView_selectorTitle) ?: "")
                val raw = ta.getString(R.styleable.SettingSelectorView_selectorEntries)
                if (!raw.isNullOrBlank()) entries = raw.split(",")
                tv.text = ta.getString(R.styleable.SettingSelectorView_selectorValue) ?: ""
            } finally { ta.recycle() }
        }

        setOnClickListener { showDialog() }
    }

    /** 设置候选项。 */
    fun setEntries(items: List<String>) { entries = items }

    /** 设置当前选中索引。 */
    fun setSelected(index: Int) {
        selectedIndex = index
        contentView.getChildAt(0).let { (it as TextView).text = entries.getOrNull(index) ?: "" }
    }

    private fun showDialog() {
        if (entries.isEmpty()) return
        AlertDialog.Builder(context)
            .setTitle(title)
            .setSingleChoiceItems(entries.toTypedArray(), selectedIndex) { dialog, which ->
                selectedIndex = which
                setSelected(which)
                onSelect?.invoke(which, entries[which])
                dialog.dismiss()
            }
            .show()
    }
}
