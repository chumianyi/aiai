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
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.aiai.settings.R

/**
 * 标签输入组件：横向排列标签，支持删除，末尾有输入框。
 */
class TagInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val tags = mutableListOf<String>()
    private var maxTags = 8
    private val input = EditText(context).apply {
        hint = "输入后回车添加"
        setSingleLine(true)
        imeOptions = EditorInfo.IME_ACTION_DONE
        setPadding(dp(8f), dp(4f), dp(8f), dp(4f))
    }

    private val tagBg = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0x333D5AFE }
    private val tagTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFF3D5AFE.toInt(); textSize = sp(13f)
    }

    /** 标签列表变化回调。 */
    var onTagsChange: ((List<String>) -> Unit)? = null

    init {
        orientation = HORIZONTAL
        addView(input)
        input.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                addTag(input.text.toString())
                input.setText("")
                true
            } else false
        }
        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.TagInputView)
            try {
                input.hint = ta.getString(R.styleable.TagInputView_tagHint) ?: input.hint
                maxTags = ta.getInt(R.styleable.TagInputView_tagMax, maxTags)
            } finally { ta.recycle() }
        }
    }

    /** 添加标签。 */
    fun addTag(tag: String) {
        val t = tag.trim()
        if (t.isEmpty() || tags.size >= maxTags || tags.contains(t)) return
        tags.add(t)
        rebuild()
    }

    fun removeTag(tag: String) {
        tags.remove(tag)
        rebuild()
    }

    fun getTags(): List<String> = tags.toList()

    private fun rebuild() {
        removeAllViews()
        tags.forEach { t ->
            val tv = TextView(context).apply {
                text = "$t ×"
                setPadding(dp(10f), dp(4f), dp(10f), dp(4f))
                setBackgroundResource(R.drawable.bg_tag)
                setTextColor(0xFF3D5AFE.toInt())
                setOnClickListener { removeTag(t) }
            }
            addView(tv)
        }
        addView(input)
        onTagsChange?.invoke(tags)
    }

    private fun dp(v: Float): Int =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics).toInt()
    private fun sp(v: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
}
