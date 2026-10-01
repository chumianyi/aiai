/*
 * Copyright (c) 2024 爱Ai (AiAi) App. All rights reserved.
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
package com.aiai.network.api

import com.aiai.network.model.request.PluginRequest
import com.aiai.network.model.response.ApiResponse
import com.aiai.network.model.response.PluginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 插件API服务接口。
 *
 * 提供插件列表查询、启用禁用、插件调用等功能。
 *
 * 接口列表：
 * - GET /plugin/list - 插件列表
 * - PUT /plugin/{id}/enable - 启用/禁用插件
 * - POST /plugin/{id}/invoke - 调用插件
 */
interface PluginApiService {

    /**
     * 获取插件列表。
     *
     * @param category 插件分类
     * @param onlyEnabled 是否仅返回已启用插件
     * @return 插件列表
     */
    @GET("plugin/list")
    suspend fun getPluginList(
        @Query("category") category: String = "all",
        @Query("only_enabled") onlyEnabled: Boolean = false,
    ): ApiResponse<List<PluginResponse>>

    /**
     * 启用/禁用插件。
     *
     * @param pluginId 插件ID
     * @param enabled 是否启用
     * @return 更新后的插件信息
     */
    @PUT("plugin/{pluginId}/enable")
    suspend fun togglePlugin(
        @Path("pluginId") pluginId: String,
        @Query("enabled") enabled: Boolean,
    ): ApiResponse<PluginResponse>

    /**
     * 调用插件。
     *
     * @param pluginId 插件ID
     * @param request 插件调用请求
     * @return 插件调用结果
     */
    @POST("plugin/{pluginId}/invoke")
    suspend fun invokePlugin(
        @Path("pluginId") pluginId: String,
        @Body request: PluginRequest,
    ): ApiResponse<Map<String, Any>>

    /**
     * 获取插件详情。
     *
     * @param pluginId 插件ID
     * @return 插件详情
     */
    @GET("plugin/{pluginId}")
    suspend fun getPluginDetail(@Path("pluginId") pluginId: String): ApiResponse<PluginResponse>

    /**
     * 获取插件市场列表。
     *
     * @param category 分类
     * @return 市场插件列表
     */
    @GET("plugin/marketplace")
    suspend fun getPluginMarketplace(
        @Query("category") category: String = "all",
    ): ApiResponse<List<PluginResponse>>

    /**
     * 安装插件。
     *
     * @param pluginId 插件ID
     * @return 安装结果
     */
    @POST("plugin/{pluginId}/install")
    suspend fun installPlugin(@Path("pluginId") pluginId: String): ApiResponse<PluginResponse>

    /**
     * 卸载插件。
     *
     * @param pluginId 插件ID
     * @return 操作结果
     */
    @DELETE("plugin/{pluginId}")
    suspend fun uninstallPlugin(@Path("pluginId") pluginId: String): ApiResponse<Unit>

    /**
     * 更新插件配置。
     *
     * @param pluginId 插件ID
     * @param config 配置Map
     * @return 操作结果
     */
    @PUT("plugin/{pluginId}/config")
    suspend fun updatePluginConfig(
        @Path("pluginId") pluginId: String,
        @Body config: Map<String, Any>,
    ): ApiResponse<Unit>
}
