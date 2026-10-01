/*
 * Copyright (c) 2026 爱Ai (AiAi)
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
package com.aiai.chat.data.enums

/**
 * 会话状态枚举
 */
enum class ConversationStatus {
    /** 活跃会话 */
    ACTIVE,

    /** 已归档会话 */
    ARCHIVED,

    /** 已删除会话 */
    DELETED,

    /** 正在生成回复中 */
    GENERATING
}
