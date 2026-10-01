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
package com.aiai.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * Mock数据拦截器，支持本地JSON Mock数据。
 *
 * 用于开发调试阶段，无需连接真实服务器即可测试UI和业务逻辑。
 * 通过匹配请求URL路径，返回预设的Mock JSON数据。
 *
 * Mock数据映射：
 * - /chat/send → 聊天响应
 * - /conversation/list → 会话列表
 * - /model/list → 模型列表
 * - /user/info → 用户信息
 * - /prompt/templates → 提示词模板列表
 * - /plugin/list → 插件列表
 * - /search → 搜索结果
 * - /image/generate → 图片生成结果
 * - /audio/tts → TTS结果
 * - /audio/stt → STT结果
 */
class MockInterceptor : Interceptor {

    companion object {
        private const val MOCK_STATUS_CODE = 200
        private const val MOCK_MESSAGE = "OK"
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    /** Mock数据映射表，key为URL路径关键词，value为Mock JSON字符串 */
    private val mockDataMap = mutableMapOf<String, String>()

    init {
        initMockData()
    }

    /**
     * 初始化Mock数据。
     */
    private fun initMockData() {
        mockDataMap["/chat/send"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "id": "chatcmpl-mock-001",
                    "object": "chat.completion",
                    "created": 1700000000,
                    "model": "gpt-4o",
                    "choices": [{
                        "index": 0,
                        "message": {
                            "role": "assistant",
                            "content": "这是Mock返回的聊天回复内容。爱Ai助手为您服务！"
                        },
                        "finish_reason": "stop"
                    }],
                    "usage": {
                        "prompt_tokens": 25,
                        "completion_tokens": 35,
                        "total_tokens": 60
                    }
                }
            }
        """.trimIndent()

        mockDataMap["/conversation/list"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "items": [
                        {
                            "id": "conv-001",
                            "title": "日常对话",
                            "model_name": "gpt-4o",
                            "created_at": "2024-01-01T10:00:00Z",
                            "updated_at": "2024-01-01T12:00:00Z",
                            "message_count": 10,
                            "is_pinned": true
                        },
                        {
                            "id": "conv-002",
                            "title": "代码编程",
                            "model_name": "gpt-4o",
                            "created_at": "2024-01-02T10:00:00Z",
                            "updated_at": "2024-01-02T11:00:00Z",
                            "message_count": 25,
                            "is_pinned": false
                        }
                    ],
                    "total": 2,
                    "page": 1,
                    "page_size": 20
                }
            }
        """.trimIndent()

        mockDataMap["/model/list"] = """
            {
                "code": 0,
                "message": "success",
                "data": [
                    {
                        "id": "gpt-4o",
                        "name": "GPT-4o",
                        "description": "最新多模态大模型",
                        "context_length": 128000,
                        "pricing": {"prompt": 0.005, "completion": 0.015}
                    },
                    {
                        "id": "gpt-4o-mini",
                        "name": "GPT-4o Mini",
                        "description": "轻量级快速模型",
                        "context_length": 128000,
                        "pricing": {"prompt": 0.00015, "completion": 0.0006}
                    }
                ]
            }
        """.trimIndent()

        mockDataMap["/user/info"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "id": "user-001",
                    "username": "aiai_user",
                    "avatar_url": "https://example.com/avatar.png",
                    "email": "user@aiai.com",
                    "plan": "premium",
                    "expires_at": "2025-12-31T23:59:59Z"
                }
            }
        """.trimIndent()

        mockDataMap["/prompt/templates"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "items": [
                        {
                            "id": "prompt-001",
                            "title": "写作助手",
                            "content": "请帮我写一篇关于{topic}的文章...",
                            "category": "writing",
                            "is_custom": false,
                            "is_favorite": true
                        }
                    ],
                    "total": 1
                }
            }
        """.trimIndent()

        mockDataMap["/plugin/list"] = """
            {
                "code": 0,
                "message": "success",
                "data": [
                    {
                        "id": "plugin-001",
                        "name": "网页搜索",
                        "description": "联网搜索最新信息",
                        "version": "1.0.0",
                        "is_enabled": true
                    }
                ]
            }
        """.trimIndent()

        mockDataMap["/search"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "results": [
                        {
                            "title": "爱Ai - 智能AI助手",
                            "url": "https://aiai.com",
                            "snippet": "爱Ai是一款智能AI对话助手应用...",
                            "score": 0.95
                        }
                    ],
                    "total": 1
                }
            }
        """.trimIndent()

        mockDataMap["/image/generate"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "id": "img-001",
                    "url": "https://example.com/generated.png",
                    "revised_prompt": "A beautiful sunset over mountains, digital art",
                    "created_at": 1700000000
                }
            }
        """.trimIndent()

        mockDataMap["/audio/tts"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "id": "tts-001",
                    "audio_url": "https://example.com/tts.mp3",
                    "duration_ms": 5000,
                    "format": "mp3"
                }
            }
        """.trimIndent()

        mockDataMap["/audio/stt"] = """
            {
                "code": 0,
                "message": "success",
                "data": {
                    "id": "stt-001",
                    "text": "这是语音识别的结果文本。",
                    "confidence": 0.98,
                    "duration_ms": 3000
                }
            }
        """.trimIndent()
    }

    /**
     * 拦截请求，如果匹配Mock数据则返回Mock响应。
     *
     * @param chain 拦截器链
     * @return 响应对象
     * @throws IOException 网络请求异常
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // 匹配Mock数据
        val mockBody = findMockData(url)
        if (mockBody != null) {
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(MOCK_STATUS_CODE)
                .message(MOCK_MESSAGE)
                .body(mockBody.toResponseBody(JSON_MEDIA_TYPE))
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Mock", "true")
                .build()
        }

        // 无匹配Mock，正常执行请求
        return chain.proceed(request)
    }

    /**
     * 根据URL查找对应的Mock数据。
     *
     * @param url 请求URL
     * @return Mock JSON字符串，未匹配返回null
     */
    private fun findMockData(url: String): String? {
        for ((key, value) in mockDataMap) {
            if (url.contains(key)) {
                return value
            }
        }
        return null
    }

    /**
     * 添加自定义Mock数据。
     *
     * @param path URL路径关键词
     * @param json Mock JSON字符串
     */
    fun addMockData(path: String, json: String) {
        mockDataMap[path] = json
    }

    /**
     * 清除所有Mock数据。
     */
    fun clearMockData() {
        mockDataMap.clear()
    }
}
