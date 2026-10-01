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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.network.model.response

import com.google.gson.annotations.SerializedName

/**
 * 版本响应模型集合。
 *
 * 包含版本号、更新日志、下载地址、强制更新等版本相关响应。
 */
sealed class VersionResponse {

    /**
     * 基础响应。
     */
    open class BaseResponse(
        @SerializedName("code")
        open val code: Int = 0,
        @SerializedName("message")
        open val message: String = ""
    )

    /**
     * 版本信息。
     *
     * @property version 版本号
     * @property versionCode 版本代码
     * @property changelog 更新日志
     * @property downloadUrl 下载地址
     * @property fileSize 文件大小（字节）
     * @property forceUpdate 是否强制更新
     * @property minSupportedVersion 最低支持版本
     * @property publishedAt 发布时间
     */
    data class VersionInfo(
        @SerializedName("version")
        val version: String = "",
        @SerializedName("version_code")
        val versionCode: Int = 0,
        @SerializedName("changelog")
        val changelog: String = "",
        @SerializedName("download_url")
        val downloadUrl: String = "",
        @SerializedName("file_size")
        val fileSize: Long = 0,
        @SerializedName("force_update")
        val forceUpdate: Boolean = false,
        @SerializedName("min_supported_version")
        val minSupportedVersion: String = "",
        @SerializedName("published_at")
        val publishedAt: Long = 0
    )

    /**
     * 检查更新响应。
     *
     * @property hasUpdate 是否有更新
     * @property latestVersion 最新版本信息
     */
    data class CheckUpdateResponse(
        @SerializedName("has_update")
        val hasUpdate: Boolean = false,
        @SerializedName("latest_version")
        val latestVersion: VersionInfo = VersionInfo()
    ) : BaseResponse()

    /**
     * 更新日志响应。
     *
     * @property changelogs 版本更新日志列表
     */
    data class ChangelogResponse(
        @SerializedName("changelogs")
        val changelogs: List<VersionInfo> = emptyList()
    ) : BaseResponse()

    /**
     * 最新版本响应。
     */
    data class VersionInfoResponse(
        @SerializedName("version")
        val version: VersionInfo = VersionInfo()
    ) : BaseResponse()

    /**
     * 版本历史响应。
     *
     * @property list 版本列表
     * @property total 总数
     * @property page 当前页
     * @property pageSize 每页数量
     */
    data class VersionHistoryResponse(
        @SerializedName("list")
        val list: List<VersionInfo> = emptyList(),
        @SerializedName("total")
        val total: Int = 0,
        @SerializedName("page")
        val page: Int = 1,
        @SerializedName("page_size")
        val pageSize: Int = 20
    ) : BaseResponse()
}
