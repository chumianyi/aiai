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
package com.aiai.common.util.validate

/**
 * 综合校验工具类。
 *
 * 提供常用业务校验：手机号、密码强度、身份证校验位等。
 */
object ValidatorUtil {

    /** 校验身份证最后一位校验码是否正确。 */
    fun isValidIdCard(idCard: String): Boolean {
        if (!RegexUtil.isIdCard(idCard)) return false
        val weights = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
        val checkCodes = charArrayOf('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
        var sum = 0
        for (i in 0 until 17) {
            sum += (idCard[i] - '0') * weights[i]
        }
        return checkCodes[sum % 11] == idCard[17].uppercaseChar()
    }

    /** 密码强度等级：0=弱，1=中，2=强。 */
    fun passwordStrength(password: String): Int {
        var score = 0
        if (password.length >= 8) score++
        if (password.matches(Regex(".*[a-z].*"))) score++
        if (password.matches(Regex(".*[A-Z].*"))) score++
        if (password.matches(Regex(".*\\d.*"))) score++
        if (password.matches(Regex(".*[@$!%*?&].*"))) score++
        return when {
            score >= 5 -> 2
            score >= 3 -> 1
            else -> 0
        }
    }

    /** 判断字符串是否为有效手机号（带段号校验）。 */
    fun isValidPhone(phone: String): Boolean {
        if (!RegexUtil.isPhone(phone)) return false
        val prefix = phone.substring(0, 3)
        val validPrefixes = setOf(
            "130","131","132","133","134","135","136","137","138","139",
            "150","151","152","153","155","156","157","158","159",
            "170","171","172","173","175","176","177","178",
            "180","181","182","183","184","185","186","187","188","189",
            "190","191","192","193","195","196","197","198","199"
        )
        return prefix in validPrefixes
    }
}
