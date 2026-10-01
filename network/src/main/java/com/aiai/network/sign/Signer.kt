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
package com.aiai.network.sign

/**
 * 签名器接口。
 *
 * 定义请求签名的标准接口，支持不同签名算法（HMAC、RSA等）。
 */
interface Signer {

    /**
     * 对数据进行签名。
     *
     * @param data 待签名的数据
     * @param secret 签名密钥
     * @return 签名字符串（通常为Base64或Hex编码）
     */
    fun sign(data: String, secret: String): String

    /**
     * 验证签名。
     *
     * @param data 原始数据
     * @param signature 待验证的签名
     * @param secret 签名密钥
     * @return true如果签名有效
     */
    fun verify(data: String, signature: String, secret: String): Boolean
}
