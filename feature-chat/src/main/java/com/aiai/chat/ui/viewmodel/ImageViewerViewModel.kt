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
package com.aiai.chat.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * 图片查看ViewModel
 *
 * 管理图片查看页面：大图浏览、缩放、保存、分享。
 */
class ImageViewerViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val app = application

    private val _imageUrl = MutableStateFlow("")
    val imageUrl: StateFlow<String> = _imageUrl.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    private val _saveProgress = MutableStateFlow(0)
    val saveProgress: StateFlow<Int> = _saveProgress.asStateFlow()

    /**
     * 初始化图片URL
     */
    fun initImage(url: String) {
        _imageUrl.value = url
    }

    /**
     * 保存图片到相册
     */
    fun saveImage() {
        val url = _imageUrl.value
        if (url.isEmpty() || _isSaved.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _saveProgress.value = 0
            try {
                withContext(Dispatchers.IO) {
                    val fileName = "aiai_${System.currentTimeMillis()}.jpg"
                    val dir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "AiAi"
                    ).apply { mkdirs() }
                    val file = File(dir, fileName)

                    URL(url).openStream().use { input ->
                        FileOutputStream(file).use { output ->
                            val buffer = ByteArray(4096)
                            var bytesRead: Int
                            var totalBytes = 0
                            val fileSize = URL(url).openConnection().contentLength

                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                totalBytes += bytesRead
                                if (fileSize > 0) {
                                    _saveProgress.value = (totalBytes * 100 / fileSize)
                                }
                            }
                        }
                    }
                    // 通知相册更新
                    val intent = Intent(android.hardware.Camera.ACTION_NEW_PICTURE)
                    intent.data = Uri.fromFile(file)
                    app.sendBroadcast(intent)
                }
                _isSaved.value = true
                Toast.makeText(app, "图片已保存到相册", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(app, "保存失败: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * 分享图片
     */
    fun shareImage() {
        val url = _imageUrl.value
        if (url.isEmpty()) return

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "来自爱Ai的AI生成图片: $url")
            type = "text/plain"
        }
        app.startActivity(Intent.createChooser(sendIntent, "分享图片").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }
}
