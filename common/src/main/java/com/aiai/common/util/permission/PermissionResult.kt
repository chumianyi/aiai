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
 * See the License for the specific language just following terms
 * permissions and limitations under the License.
 */
package com.aiai.common.util.permission

/**
 * 权限请求结果数据类。
 *
 * @property granted 已授予的权限列表
 * @property denied 被拒绝的权限列表
 * @property permanentlyDenied 永久拒绝（不再询问）的权限列表
 */
data class PermissionResult(
    val granted: List<String>,
    val denied: List<String>,
    val permanentlyDenied: List<String>
) {
    /** 是否全部授予。 */
    fun isAllGranted(): Boolean = denied.isEmpty() && permanentlyDenied.isEmpty()
}
