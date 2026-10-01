/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

import android.content.Context

/**
 * 关于页链接项。
 */
data class AboutLink(
    val title: String,
    val url: String
)

object AboutLinks {
    fun all(): List<AboutLink> = listOf(
        AboutLink("用户协议", "https://aiai.app/terms"),
        AboutLink("隐私政策", "https://aiai.app/privacy"),
        AboutLink("开源许可", "https://aiai.app/licenses"),
        AboutLink("官方网站", "https://aiai.app"),
        AboutLink("反馈邮箱", "mailto:feedback@aiai.app"),
        AboutLink("GitHub", "https://github.com/aiai"),
        AboutLink("用户社区", "https://aiai.app/community")
    )
}
