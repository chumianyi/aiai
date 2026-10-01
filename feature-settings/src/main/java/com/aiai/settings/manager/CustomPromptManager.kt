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
import com.aiai.settings.model.PromptTemplate
import com.aiai.settings.model.PromptCategory

/**
 * 自定义提示词 CRUD。
 */
class CustomPromptManager(context: Context) {

    private val prefs = context.getSharedPreferences("aiai_custom_prompts", Context.MODE_PRIVATE)
    private val gson = com.google.gson.Gson()

    fun save(template: PromptTemplate) {
        val set = prefs.getStringSet("prompts", emptySet())!!.toMutableSet()
        set.add(gson.toJson(template))
        prefs.edit().putStringSet("prompts", set).apply()
    }

    fun delete(id: String) {
        val list = all().filter { it.id != id }
        prefs.edit().putStringSet("prompts", list.map { gson.toJson(it) }.toSet()).apply()
    }

    fun all(): List<PromptTemplate> {
        val set = prefs.getStringSet("prompts", emptySet()) ?: return emptyList()
        return set.mapNotNull {
            try { gson.fromJson(it, PromptTemplate::class.java) } catch (_: Exception) { null }
        }
    }
}
