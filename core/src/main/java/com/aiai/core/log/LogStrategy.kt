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
 * 日志策略接口。
 *
 * 不同策略实现不同输出方式（控制台、文件、远程等）。
 */
interface LogStrategy {

    /** 输出日志。 */
    fun log(level: LogLevel, tag: String, message: String, tr: Throwable? = null)

    /** 日志是否启用。 */
    fun isLoggable(level: LogLevel): Boolean
}
