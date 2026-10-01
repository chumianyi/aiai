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
package com.aiai.core.crash

/**
 * 崩溃报告数据类。
 */
data class CrashReport(
    val time: Long,
    val threadName: String,
    val exceptionType: String,
    val message: String,
    val stackTrace: String,
    val deviceInfo: String,
    val appVersion: String,
    val memoryInfo: String
)
