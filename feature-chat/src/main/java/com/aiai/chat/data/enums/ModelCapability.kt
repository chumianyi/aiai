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
 * 模型能力枚举
 *
 * 标识AI模型支持的功能能力。
 */
enum class ModelCapability {
    /** 支持文本对话 */
    TEXT_CHAT,

    /** 支持图片生成 */
    IMAGE_GENERATION,

    /** 支持图片理解/多模态 */
    IMAGE_UNDERSTANDING,

    /** 支持函数调用/工具调用 */
    FUNCTION_CALL,

    /** 支持流式输出 */
    STREAMING,

    /** 支持长上下文 */
    LONG_CONTEXT,

    /** 支持代码生成 */
    CODE_GENERATION,

    /** 支持语音识别 */
    SPEECH_TO_TEXT,

    /** 支持语音合成 */
    TEXT_TO_SPEECH,

    /** 支持网页浏览 */
    WEB_BROWSING
}
