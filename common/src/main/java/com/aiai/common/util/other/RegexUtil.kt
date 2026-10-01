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

import java.util.regex.Pattern

/**
 * 正则工具类。
 */
object RegexUtil {

    /** 手机号。 */
    private val PHONE = Pattern.compile("^1[3-9]\\d{9}$")

    /** 邮箱。 */
    private val EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** URL。 */
    private val URL = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")

    /** 身份证。 */
    private val ID_CARD = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")

    /** IP。 */
    private val IP = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")

    /** 是否为手机号。 */
    fun isPhone(s: String): Boolean = PHONE.matcher(s).matches()

    /** 是否为邮箱。 */
    fun isEmail(s: String): Boolean = EMAIL.matcher(s).matches()

    /** 是否为 URL。 */
    fun isUrl(s: String): Boolean = URL.matcher(s).matches()

    /** 是否为身份证。 */
    fun isIdCard(s: String): Boolean = ID_CARD.matcher(s).matches()

    /** 是否为 IP。 */
    fun isIp(s: String): Boolean = IP.matcher(s).matches()
}
