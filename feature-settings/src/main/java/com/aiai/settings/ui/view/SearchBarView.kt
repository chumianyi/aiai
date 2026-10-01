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
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import com.aiai.settings.R

/**
 * 搜索栏：输入框 + 清除按钮 + 可选语音按钮。
 */
class SearchBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val edit: EditText
    private val clear: ImageView
    private val voice: ImageView

    /** 输入文本变化回调（debounce 由调用方处理）。 */
    var onQueryChange: ((String) -> Unit)? = null
    /** 语音按钮点击。 */
    var onVoiceClick: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_search_bar, this, true)
        edit = findViewById(R.id.etSearch)
        clear = findViewById(R.id.ivClear)
        voice = findViewById(R.id.ivVoice)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.SearchBarView)
            try {
                edit.hint = ta.getString(R.styleable.SearchBarView_searchHint) ?: "搜索"
                voice.visibility = if (ta.getBoolean(R.styleable.SearchBarView_searchShowVoice, false)) VISIBLE else GONE
            } finally { ta.recycle() }
        }

        clear.setOnClickListener { edit.setText("") }
        voice.setOnClickListener { onVoiceClick?.invoke() }
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clear.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                onQueryChange?.invoke(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun setQuery(q: String) { edit.setText(q) }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = resources.getDimension(R.dimen.search_bar_height).toInt()
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }
}
