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
package com.aiai.chat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.aiai.chat.R
import com.aiai.chat.data.model.StreamChunk
import com.aiai.chat.handler.StreamHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 流式输出测试页Fragment
 *
 * 用于测试SSE流式输出功能，展示实时接收效果。
 */
class StreamingTestFragment : Fragment() {

    private lateinit var etInput: EditText
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var tvOutput: TextView

    private val streamHandler = StreamHandler()
    private var isStreaming = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_streaming_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupListeners()
    }

    private fun initViews(view: View) {
        etInput = view.findViewById(R.id.et_test_input)
        btnStart = view.findViewById(R.id.btn_start_stream)
        btnStop = view.findViewById(R.id.btn_stop_stream)
        tvOutput = view.findViewById(R.id.tv_stream_output)
    }

    private fun setupListeners() {
        btnStart.setOnClickListener {
            startStreamTest()
        }

        btnStop.setOnClickListener {
            stopStream()
        }
    }

    /**
     * 开始流式测试
     */
    private fun startStreamTest() {
        val prompt = etInput.text.toString().ifBlank { "你好，请介绍一下自己" }
        tvOutput.text = ""
        isStreaming = true

        viewLifecycleOwner.lifecycleScope.launch {
            val mockResponse = buildString {
                append("收到你的问题：\"$prompt\"\n\n")
                append("这是一段模拟的流式输出测试。")
                append("每个文字都会逐个显示，模拟真实的SSE流式响应效果。\n\n")
                append("**功能测试清单：**\n")
                append("- [x] 流式文字输出\n")
                append("- [x] Markdown渲染\n")
                append("- [x] 代码块高亮\n")
                append("- [ ] 表格渲染\n")
                append("- [x] 自动滚动到底部\n\n")
                append("```kotlin\n")
                append("fun main() {\n")
                append("    println(\"Hello, AiAi!\")\n")
                append("}\n")
                append("```\n\n")
                append("测试完成！")
            }

            mockResponse.chunked(3).forEach { chunk ->
                if (!isStreaming) return@launch
                delay(30)
                tvOutput.append(chunk)
            }
            isStreaming = false
        }
    }

    /**
     * 停止流式输出
     */
    private fun stopStream() {
        isStreaming = false
        streamHandler.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        streamHandler.cancel()
    }

    companion object {
        fun newInstance(): StreamingTestFragment {
            return StreamingTestFragment()
        }
    }
}
