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
    val time: String,
    val threadName: String,
    val message: String,
    val exceptionClass: String,
    val stackTrace: String,
    val deviceBrand: String,
    val deviceModel: String,
    val sdkInt: Int,
    val release: String,
    val usedMemoryMB: Long,
    val maxMemoryMB: Long
) {
    override fun toString(): String {
        return buildString {
            append("=== Crash Report ===\n")
            append("Time: $time\n")
            append("Thread: $threadName\n")
            append("Exception: $exceptionClass\n")
            append("Message: $message\n")
            append("Device: $deviceBrand $deviceModel (API $sdkInt, Android $release)\n")
            append("Memory: ${usedMemoryMB}MB / ${maxMemoryMB}MB\n")
            append("Stack:\n$stackTrace\n")
            append("=== End ===\n")
        }
    }
}
