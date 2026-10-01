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
 * 默认参数描述。
 */
object DefaultParams {

    data class Param(val name: String, val value: String)

    fun all(): List<Param> = listOf(
        Param("温度", "0.7"),
        Param("最大 Tokens", "2048"),
        Param("Top P", "0.9"),
        Param("频率惩罚", "0.0"),
        Param("存在惩罚", "0.0"),
        Param("系统提示", "你是友好的助手")
    )
}
