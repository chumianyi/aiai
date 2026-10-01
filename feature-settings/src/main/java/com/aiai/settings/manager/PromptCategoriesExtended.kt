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
 * 提示词分类（扩展）。
 */
object PromptCategoriesExtended {

    data class Category(val name: String, val icon: String)

    fun all(): List<Category> = listOf(
        Category("写作", "✍"),
        Category("编程", "💻"),
        Category("翻译", "🌐"),
        Category("学习", "📚"),
        Category("生活", "🏠"),
        Category("职场", "💼"),
        Category("创意", "🎨"),
        Category("其他", "📌")
    )
}
