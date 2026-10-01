/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.chat.manager

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * 分享管理器
 *
 * 管理内容分享到其他应用的功能。
 */
class ShareManager(private val context: Context) {

    enum class ShareType {
        TEXT,
        IMAGE,
        FILE,
        LINK
    }

    data class ShareTarget(
        val packageName: String,
        val appName: String,
        val iconRes: Int
    )

    private val _shareTargets = MutableStateFlow<List<ShareTarget>>(emptyList())
    val shareTargets: StateFlow<List<ShareTarget>> = _shareTargets.asStateFlow()

    fun shareText(title: String, content: String) {
        // 调用系统分享
    }

    fun shareImage(imageUri: String) {
        // 分享图片
    }

    fun shareFile(filePath: String) {
        // 分享文件
    }

    fun shareLink(url: String, title: String) {
        // 分享链接
    }

    fun getShareTargets(): List<ShareTarget> {
        return _shareTargets.value
    }
}
