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
 * 设置项状态。
 */
data class SettingState(
    val key: String,
    val title: String,
    val subtitle: String = "",
    val type: SettingType,
    val value: Any? = null,
    val enabled: Boolean = true
)

enum class SettingType {
    SWITCH, SELECTOR, SLIDER, INPUT, NAVIGATION, COLOR, INFO
}
