/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.log

/**
 * 日志配置。
 */
data class LogConfig(
    var enabled: Boolean = true,
    var minLevel: LogLevel = LogLevel.DEBUG,
    var tag: String = "AiAi",
    var consoleEnabled: Boolean = true,
    var fileEnabled: Boolean = false
)
