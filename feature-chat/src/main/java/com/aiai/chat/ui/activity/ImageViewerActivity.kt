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
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aiai.chat.R
import com.aiai.chat.ui.viewmodel.ImageViewerViewModel
import com.github.chrisbanes.photoview.PhotoView

/**
 * 图片查看页Activity
 *
 * 支持大图浏览、双指缩放、保存到相册、分享。
 */
class ImageViewerActivity : AppCompatActivity() {

    private lateinit var viewModel: ImageViewerViewModel

    private lateinit var photoView: PhotoView
    private lateinit var btnClose: ImageButton
    private lateinit var btnSave: ImageButton
    private lateinit var btnShare: ImageButton
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        viewModel = ViewModelProvider(this)[ImageViewerViewModel::class.java]

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL) ?: ""
        viewModel.initImage(imageUrl)

        initViews()
        initObservers()

        // 加载图片
        if (imageUrl.isNotEmpty()) {
            // Glide加载到PhotoView
        }
    }

    private fun initViews() {
        photoView = findViewById(R.id.photo_view)
        btnClose = findViewById(R.id.btn_close)
        btnSave = findViewById(R.id.btn_save)
        btnShare = findViewById(R.id.btn_share)
        progressBar = findViewById(R.id.progress_bar)

        btnClose.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            viewModel.saveImage()
        }

        btnShare.setOnClickListener {
            viewModel.shareImage()
        }
    }

    private fun initObservers() {
        viewModel.isLoading.observe(this) { loading ->
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }

        viewModel.isSaved.observe(this) { saved ->
            if (saved) {
                Toast.makeText(this, "已保存", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
    }
}
