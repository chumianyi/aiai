/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.aiai.settings.R

/**
 * 更新提示弹窗内容视图：版本信息、更新日志、下载进度条。
 */
class UpdateDialogView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_update_dialog, this, true)
    }

    /** 设置版本信息与日志。 */
    fun bind(version: String, changelog: List<String>, onDownload: () -> Unit) {
        findViewById<android.widget.TextView>(R.id.tvVersion).text = "发现新版本 $version"
        findViewById<android.widget.TextView>(R.id.tvChangelog).text =
            changelog.joinToString("\n") { "· $it" }
        findViewById<android.widget.Button>(R.id.btnDownload).setOnClickListener { onDownload() }
    }

    /** 更新下载进度 0~100。 */
    fun setProgress(percent: Int) {
        findViewById<android.widget.ProgressBar>(R.id.progress).progress = percent
    }
}
