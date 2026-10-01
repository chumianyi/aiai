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
package com.aiai.chat.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aiai.chat.R

/**
 * 全屏图片生成结果页Activity
 *
 * 展示AI生成的图片结果，支持多张切换、下载、重新生成。
 */
class FullscreenImageActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var btnClose: ImageButton
    private lateinit var btnDownload: ImageButton
    private lateinit var btnRegenerate: ImageButton
    private lateinit var tvPrompt: TextView
    private lateinit var tvPageIndicator: TextView
    private lateinit var progressBar: ProgressBar

    private var imageUrls: List<String> = emptyList()
    private var currentPosition: Int = 0
    private var prompt: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_image)

        imageUrls = intent.getStringArrayListExtra(EXTRA_IMAGE_URLS) ?: emptyList()
        currentPosition = intent.getIntExtra(EXTRA_POSITION, 0)
        prompt = intent.getStringExtra(EXTRA_PROMPT) ?: ""

        initViews()
        showImage(currentPosition)
    }

    private fun initViews() {
        imageView = findViewById(R.id.iv_fullscreen)
        btnClose = findViewById(R.id.btn_close)
        btnDownload = findViewById(R.id.btn_download)
        btnRegenerate = findViewById(R.id.btn_regenerate)
        tvPrompt = findViewById(R.id.tv_prompt)
        tvPageIndicator = findViewById(R.id.tv_page_indicator)
        progressBar = findViewById(R.id.progress_bar)

        tvPrompt.text = prompt

        btnClose.setOnClickListener { finish() }

        btnDownload.setOnClickListener {
            // 下载当前图片
        }

        btnRegenerate.setOnClickListener {
            // 重新生成
        }
    }

    private fun showImage(position: Int) {
        if (position < 0 || position >= imageUrls.size) return
        currentPosition = position
        tvPageIndicator.text = "${position + 1} / ${imageUrls.size}"

        // 加载图片
        progressBar.visibility = View.VISIBLE
    }

    companion object {
        const val EXTRA_IMAGE_URLS = "extra_image_urls"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_PROMPT = "extra_prompt"
    }
}
