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
 * 动画速度选项。
 */
object AnimationSpeeds {

    data class Speed(val name: String, val factor: Float)

    fun all(): List<Speed> = listOf(
        Speed("关闭", 0f),
        Speed("快速", 0.7f),
        Speed("标准", 1.0f),
        Speed("慢速", 1.5f)
    )
}
