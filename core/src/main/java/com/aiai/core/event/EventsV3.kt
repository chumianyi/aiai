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
package com.aiai.core.event

/**
 * 事件基类 V3。
 */
abstract class Event3 {
    val timestamp = System.currentTimeMillis()
}

/** 登录事件 V3。 */
class LoginEventV3 : Event3()

/** 登出事件 V3。 */
class LogoutEventV3 : Event3()

/** 网络事件 V3。 */
class NetworkEventV3(val available: Boolean) : Event3()
