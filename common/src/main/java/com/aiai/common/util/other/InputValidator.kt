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
package com.aiai.common.util.other

import android.util.Patterns
import java.util.regex.Pattern

/**
 * 输入校验工具类。
 *
 * 提供各种输入格式校验。
 */
object InputValidator {

    /** 校验手机号。 */
    fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^1[3-9]\\d{9}$"))
    }

    /** 校验邮箱。 */
    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /** 校验 URL。 */
    fun isValidUrl(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }

    /** 校验密码强度（至少8位，包含字母和数字）。 */
    fun isStrongPassword(password: String): Boolean {
        return password.length >= 8 &&
            password.matches(Regex(".*[A-Za-z].*")) &&
            password.matches(Regex(".*\\d.*"))
    }

    /** 校验昵称（2-20位中文/字母/数字）。 */
    fun isValidNickname(nickname: String): Boolean {
        return nickname.length in 2..20 &&
            nickname.matches(Regex("^[\\u4e00-\\u9fa5a-zA-Z0-9_]+$"))
    }

    /** 校验身份证号。 */
    fun isValidIdCard(idCard: String): Boolean {
        return idCard.matches(Regex("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
    }

    /** 校验 IP 地址。 */
    fun isValidIp(ip: String): Boolean {
        return Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$"
        ).matcher(ip).matches()
    }
}
