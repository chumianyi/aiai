/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aiai.settings.manager.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** 图片生成设置 UI 状态。 */
data class ImageGenUiState(
    val sizeIndex: Int = 0,
    val styleIndex: Int = 0,
    val count: Int = 1,
    val savePath: String = "/Pictures/AiAi",
    val watermark: Boolean = false
)

/** 默认尺寸候选。 */
val IMAGE_SIZES = listOf("512×512", "768×768", "1024×1024", "1024×1792", "1792×1024")

/** 默认风格候选。 */
val IMAGE_STYLES = listOf("写实", "油画", "动漫", "赛博朋克", "水墨", "3D 渲染")

/**
 * 图片生成设置 ViewModel。
 */
class ImageGenSettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val settings = SettingsManager.get(app)

    private val _uiState = MutableStateFlow(
        ImageGenUiState(
            sizeIndex = settings.getInt("img_size", 2),
            styleIndex = settings.getInt("img_style", 0),
            count = settings.getInt("img_count", 1),
            savePath = settings.getString("img_path", "/Pictures/AiAi"),
            watermark = settings.imageWatermark.value
        )
    )
    val uiState: StateFlow<ImageGenUiState> = _uiState.asStateFlow()

    fun setSize(index: Int) {
        settings.putInt("img_size", index)
        _uiState.value = _uiState.value.copy(sizeIndex = index)
    }

    fun setStyle(index: Int) {
        settings.putInt("img_style", index)
        _uiState.value = _uiState.value.copy(styleIndex = index)
    }

    fun setCount(n: Int) {
        settings.putInt("img_count", n.coerceIn(1, 9))
        _uiState.value = _uiState.value.copy(count = n.coerceIn(1, 9))
    }

    fun setSavePath(path: String) {
        settings.putString("img_path", path)
        _uiState.value = _uiState.value.copy(savePath = path)
    }

    fun setWatermark(enabled: Boolean) {
        settings.setImageWatermark(enabled)
        _uiState.value = _uiState.value.copy(watermark = enabled)
    }
}
