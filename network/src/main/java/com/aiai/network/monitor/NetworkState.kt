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
package com.aiai.network.monitor

/**
 * 网络状态枚举。
 */
enum class NetworkState {
    /** 无网络连接 */
    NONE,

    /** WiFi连接 */
    WIFI,

    /** 移动网络 */
    CELLULAR,

    /** 以太网 */
    ETHERNET,

    /** 未知网络类型 */
    UNKNOWN;

    /**
     * 是否有网络连接。
     *
     * @return true如果不是NONE
     */
    fun isConnected(): Boolean = this != NONE

    /**
     * 是否为计量网络。
     *
     * @return true如果是CELLULAR
     */
    fun isMetered(): Boolean = this == CELLULAR
}
