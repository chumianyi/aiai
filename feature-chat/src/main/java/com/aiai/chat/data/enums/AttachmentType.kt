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
 * 附件类型枚举
 */
enum class AttachmentType {
    /** 图片附件 */
    IMAGE,

    /** 文件附件 */
    FILE,

    /** 拍照获取 */
    CAMERA,

    /** 语音录制 */
    VOICE,

    /** 位置信息 */
    LOCATION,

    /** 视频 */
    VIDEO
}
