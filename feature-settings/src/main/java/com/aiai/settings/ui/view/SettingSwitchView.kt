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
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.CompoundButton
import android.widget.FrameLayout
import android.widget.Switch
import android.widget.TextView
import com.aiai.settings.R

/**
 * 开关设置项：标题 + 描述 + Switch。
 */
class SettingSwitchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val title: TextView
    private val subtitle: TextView
    private val switch: Switch

    /** 开关状态变化回调。 */
    var onCheckedChange: ((Boolean) -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_setting_switch, this, true)
        title = findViewById(R.id.tvTitle)
        subtitle = findViewById(R.id.tvSubtitle)
        switch = findViewById(R.id.switchBtn)

        attrs?.let {
            val ta = context.obtainStyledAttributes(it, R.styleable.SettingSwitchView)
            try {
                title.text = ta.getString(R.styleable.SettingSwitchView_switchTitle) ?: ""
                val sub = ta.getString(R.styleable.SettingSwitchView_switchSubtitle)
                if (sub.isNullOrBlank()) subtitle.visibility = GONE else subtitle.text = sub
                switch.isChecked = ta.getBoolean(R.styleable.SettingSwitchView_switchChecked, false)
                isEnabled = ta.getBoolean(R.styleable.SettingSwitchView_switchEnabled, true)
            } finally { ta.recycle() }
        }

        switch.setOnCheckedChangeListener { _, checked ->
            onCheckedChange?.invoke(checked)
        }
        setOnClickListener { switch.isChecked = !switch.isChecked }
    }

    fun setChecked(checked: Boolean) { switch.isChecked = checked }
    fun isChecked(): Boolean = switch.isChecked

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = resources.getDimension(R.dimen.item_height_large).toInt()
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY))
    }
}
