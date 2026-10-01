/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.common.util.data

/**
 * YAML 工具类（占位实现）。
 *
 * 实际项目中可引入 org.yaml:snakeyaml 库实现完整功能。
 */
object YamlUtil {

    /** 将 Map 转换为 YAML 字符串（简化版）。 */
    fun toYaml(map: Map<String, Any?>): String {
        val sb = StringBuilder()
        map.forEach { (k, v) ->
            when (v) {
                is Map<*, *> -> {
                    sb.append("$k:\n")
                    v.forEach { (sk, sv) -> sb.append("  $sk: $sv\n") }
                }
                else -> sb.append("$k: $v\n")
            }
        }
        return sb.toString()
    }
}
