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

import java.util.regex.Pattern

/**
 * 正则表达式工具类。
 *
 * 内置 20+ 常用正则：手机号、邮箱、身份证、URL、IP、车牌号等。
 */
object RegexUtil {

    /** 中国大陆手机号。 */
    private val PHONE = Pattern.compile("^1[3-9]\\d{9}$")

    /** 邮箱。 */
    private val EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    /** URL。 */
    private val URL = Pattern.compile("^(https?|ftp)://[^\\s/$.?#].[^\\s]*$")

    /** 身份证号（18 位）。 */
    private val ID_CARD = Pattern.compile("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$")

    /** IPv4。 */
    private val IPV4 = Pattern.compile("^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$")

    /** 纯数字。 */
    private val NUMBER = Pattern.compile("^-?\\d+(\\.\\d+)?$")

    /** 纯字母。 */
    private val ALPHA = Pattern.compile("^[a-zA-Z]+$")

    /** 纯中文。 */
    private val CHINESE = Pattern.compile("^[\\u4e00-\\u9fa5]+$")

    /** 车牌号。 */
    private val PLATE = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{4,5}[A-Z0-9挂学警港澳]$")

    /** 邮政编码。 */
    private val ZIP_CODE = Pattern.compile("^[1-9]\\d{5}$")

    /** QQ 号。 */
    private val QQ = Pattern.compile("^[1-9]\\d{4,10}$")

    /** 微信号。 */
    private val WECHAT = Pattern.compile("^[a-zA-Z][-_a-zA-Z0-9]{5,19}$")

    /** 密码（8-20 位，必须包含字母和数字）。 */
    private val PASSWORD = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,20}$")

    /** 强密码（8-20 位，包含大小写字母、数字、特殊字符）。 */
    private val STRONG_PASSWORD = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$")

    /** MAC 地址。 */
    private val MAC = Pattern.compile("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")

    /** 端口号。 */
    private val PORT = Pattern.compile("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")

    /** ISBN。 */
    private val ISBN = Pattern.compile("^(ISBN[-]?[0-9]{10}|ISBN[-]?[0-9]{13})$")

    /** 日期 yyyy-MM-dd。 */
    private val DATE = Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")

    /** 时间 HH:mm:ss。 */
    private val TIME = Pattern.compile("^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$")

    /** 中文姓名。 */
    private val CHINESE_NAME = Pattern.compile("^[\\u4e00-\\u9fa5·]{2,15}$")

    // region 校验方法

    fun isPhone(s: String) = PHONE.matcher(s).matches()
    fun isEmail(s: String) = EMAIL.matcher(s).matches()
    fun isUrl(s: String) = URL.matcher(s).matches()
    fun isIdCard(s: String) = ID_CARD.matcher(s).matches()
    fun isIpv4(s: String) = IPV4.matcher(s).matches()
    fun isNumber(s: String) = NUMBER.matcher(s).matches()
    fun isAlpha(s: String) = ALPHA.matcher(s).matches()
    fun isChinese(s: String) = CHINESE.matcher(s).matches()
    fun isPlate(s: String) = PLATE.matcher(s).matches()
    fun isZipCode(s: String) = ZIP_CODE.matcher(s).matches()
    fun isQQ(s: String) = QQ.matcher(s).matches()
    fun isWeChat(s: String) = WECHAT.matcher(s).matches()
    fun isPassword(s: String) = PASSWORD.matcher(s).matches()
    fun isStrongPassword(s: String) = STRONG_PASSWORD.matcher(s).matches()
    fun isMac(s: String) = MAC.matcher(s).matches()
    fun isPort(s: String) = PORT.matcher(s).matches()
    fun isIsbn(s: String) = ISBN.matcher(s).matches()
    fun isDate(s: String) = DATE.matcher(s).matches()
    fun isTime(s: String) = TIME.matcher(s).matches()
    fun isChineseName(s: String) = CHINESE_NAME.matcher(s).matches()

    // endregion
}
