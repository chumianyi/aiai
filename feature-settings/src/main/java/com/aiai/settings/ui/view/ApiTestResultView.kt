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
import android.widget.TextView
import com.aiai.settings.R
import com.aiai.settings.model.ApiTestResult

/**
 * API 测试结果展示：成功/失败图标 + 延迟 + 详情。
 */
class ApiTestResultView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private val tvStatus: TextView
    private val tvDetail: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_api_test_result, this, true)
        tvStatus = findViewById(R.id.tvStatus)
        tvDetail = findViewById(R.id.tvDetail)
    }

    /** 绑定测试结果。 */
    fun bind(result: ApiTestResult) {
        tvStatus.text = if (result.success) "连接成功" else "连接失败"
        tvStatus.setTextColor(context.getColor(if (result.success) R.color.success else R.color.error))
        tvDetail.text = "延迟 ${result.latencyMs} ms  ·  HTTP ${result.httpCode}  ·  ${result.message}"
    }
}
