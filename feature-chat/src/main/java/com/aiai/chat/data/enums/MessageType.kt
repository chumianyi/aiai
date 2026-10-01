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
 * 消息类型枚举
 *
 * 定义聊天消息的具体内容类型，每种类型对应不同的ViewHolder和渲染方式。
 */
enum class MessageType {
    /** 纯文本消息，支持Markdown渲染 */
    TEXT,

    /** 图片消息，支持本地图片和网络图片 */
    IMAGE,

    /** 文件消息，支持各类附件文件 */
    FILE,

    /** 语音消息，支持录制和播放 */
    VOICE,

    /** 代码块消息，带语法高亮和复制功能 */
    CODE,

    /** 系统提示消息（如"已开始新对话"） */
    SYSTEM,

    /** 错误消息（网络错误、生成失败等） */
    ERROR,

    /** 加载中消息（流式输出中的占位） */
    LOADING,

    /** 引用消息，引用之前的某条消息 */
    QUOTE,

    /** 表格消息 */
    TABLE,

    /** 任务列表消息 */
    TASK_LIST,

    /** 混合消息（包含多种内容块） */
    MIXED
}
