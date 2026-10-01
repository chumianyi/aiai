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
package com.aiai.data.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 数据验证工具。
 *
 * 提供各种数据格式验证功能。
 */
object Validator {

    private const val TAG = "Validator"

    /**
     * 验证邮箱格式。
     *
     * @param email 邮箱字符串
     * @return true如果格式正确
     */
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * 验证URL格式。
     *
     * @param url URL字符串
     * @return true如果格式正确
     */
    fun isValidUrl(url: String): Boolean {
        return android.util.Patterns.WEB_URL.matcher(url).matches()
    }

    /**
     * 验证手机号格式。
     *
     * @param phone 手机号
     * @return true如果格式正确
     */
    fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    /**
     * 验证API Key格式。
     *
     * @param apiKey API Key
     * @return true如果格式合理
     */
    fun isValidApiKey(apiKey: String): Boolean {
        return apiKey.length >= 20 && apiKey.length <= 200
    }

    /**
     * 验证Temperature范围。
     *
     * @param temperature 温度值
     * @return true如果在合理范围
     */
    fun isValidTemperature(temperature: Double): Boolean {
        return temperature in 0.0..2.0
    }

    /**
     * 验证TopP范围。
     *
     * @param topP top_p值
     * @return true如果在合理范围
     */
    fun isValidTopP(topP: Double): Boolean {
        return topP in 0.0..1.0
    }

    /**
     * 验证MaxTokens范围。
     *
     * @param maxTokens 最大token数
     * @return true如果在合理范围
     */
    fun isValidMaxTokens(maxTokens: Int): Boolean {
        return maxTokens in 1..128000
    }

    /**
     * 验证会话标题长度。
     *
     * @param title 标题
     * @return true如果长度合理
     */
    fun isValidTitle(title: String): Boolean {
        return title.isNotBlank() && title.length <= 100
    }

    /**
     * 验证消息内容不为空。
     *
     * @param content 内容
     * @return true如果有效
     */
    fun isValidMessageContent(content: String): Boolean {
        return content.isNotBlank() && content.length <= 100000
    }

    /**
     * 验证文件名。
     *
     * @param fileName 文件名
     * @return true如果有效
     */
    fun isValidFileName(fileName: String): Boolean {
        return fileName.isNotBlank() &&
            fileName.length <= 255 &&
            !fileName.contains("/") &&
            !fileName.contains("\\")
    }

    /**
     * 验证分页参数。
     *
     * @param page 页码
     * @param pageSize 每页数量
     * @return true如果参数有效
     */
    fun isValidPaging(page: Int, pageSize: Int): Boolean {
        return page >= 1 && pageSize in 1..100
    }

    /**
     * 验证模型名称。
     *
     * @param modelName 模型名称
     * @return true如果有效
     */
    fun isValidModelName(modelName: String): Boolean {
        return modelName.isNotBlank() && modelName.length <= 100
    }
}
