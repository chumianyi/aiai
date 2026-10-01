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
import android.widget.LinearLayout
import android.widget.TextView
import com.aiai.settings.model.FontScale

/**
 * 字体大小预览：展示不同字号下的示例文本。
 */
class FontSizePreview @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : LinearLayout(context, attrs, defStyle) {

    private val sample: TextView

    init {
        orientation = VERTICAL
        inflate(context, com.aiai.settings.R.layout.view_font_preview, this)
        sample = findViewById(com.aiai.settings.R.id.tvSample)
    }

    /** 应用字号档位。 */
    fun applyScale(scale: FontScale) {
        sample.textSize = 16f * scale.scale
    }
}
