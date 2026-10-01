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
package com.aiai.data.util

import android.util.Patterns

/**
 * 数据验证器。
 *
 * 提供字段校验、完整性检查、冲突检测等功能。
 */
object DataValidator {

    /**
     * 验证结果。
     *
     * @property valid 是否有效
     * @property errors 错误信息列表
     */
    data class ValidationResult(
        val valid: Boolean,
        val errors: List<String> = emptyList()
    )

    /**
     * 验证邮箱格式。
     *
     * @param email 邮箱地址
     * @return true 表示有效
     */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * 验证手机号格式。
     *
     * @param phone 手机号
     * @return true 表示有效
     */
    fun isValidPhone(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches() && phone.length == 11
    }

    /**
     * 验证 URL 格式。
     *
     * @param url URL 地址
     * @return true 表示有效
     */
    fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    /**
     * 验证密码强度。
     *
     * @param password 密码
     * @return 强度等级（0-4）
     */
    fun getPasswordStrength(password: String): Int {
        var strength = 0

        if (password.length >= 8) strength++
        if (password.matches(Regex(".*[a-z].*"))) strength++
        if (password.matches(Regex(".*[A-Z].*"))) strength++
        if (password.matches(Regex(".*\\d.*"))) strength++
        if (password.matches(Regex(".*[!@#\$%^&*()].*"))) strength++

        return strength.coerceAtMost(4)
    }

    /**
     * 验证用户数据完整性。
     *
     * @param username 用户名
     * @param email 邮箱
     * @param password 密码
     * @return 验证结果
     */
    fun validateUserData(
        username: String,
        email: String,
        password: String
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (username.isBlank()) {
            errors.add("用户名不能为空")
        } else if (username.length < 2) {
            errors.add("用户名长度不能少于2个字符")
        }

        if (email.isBlank()) {
            errors.add("邮箱不能为空")
        } else if (!isValidEmail(email)) {
            errors.add("邮箱格式不正确")
        }

        if (password.isBlank()) {
            errors.add("密码不能为空")
        } else if (password.length < 6) {
            errors.add("密码长度不能少于6位")
        }

        return ValidationResult(errors.isEmpty(), errors)
    }

    /**
     * 验证对话数据完整性。
     *
     * @param title 对话标题
     * @param messagesCount 消息数量
     * @return 验证结果
     */
    fun validateConversationData(
        title: String,
        messagesCount: Int
    ): ValidationResult {
        val errors = mutableListOf<String>()

        if (title.isBlank()) {
            errors.add("对话标题不能为空")
        }

        if (messagesCount <= 0) {
            errors.add("对话至少需要一条消息")
        }

        return ValidationResult(errors.isEmpty(), errors)
    }

    /**
     * 检查字符串是否为空或空白。
     *
     * @param value 字符串
     * @param fieldName 字段名
     * @return 错误信息，null 表示通过
     */
    fun requireNotBlank(value: String?, fieldName: String): String? {
        return if (value.isNullOrBlank()) {
            "$fieldName 不能为空"
        } else {
            null
        }
    }

    /**
     * 检查字符串长度范围。
     *
     * @param value 字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @param fieldName 字段名
     * @return 错误信息，null 表示通过
     */
    fun requireLength(
        value: String?,
        minLength: Int,
        maxLength: Int,
        fieldName: String
    ): String? {
        val length = value?.length ?: 0
        return when {
            length < minLength -> "$fieldName 长度不能少于 $minLength 个字符"
            length > maxLength -> "$fieldName 长度不能超过 $maxLength 个字符"
            else -> null
        }
    }
}
