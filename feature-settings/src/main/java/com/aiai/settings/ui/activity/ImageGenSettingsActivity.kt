/*
 * Copyright (c) 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.SettingSelectorView
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.ui.view.SliderView
import com.aiai.settings.viewmodel.IMAGE_SIZES
import com.aiai.settings.viewmodel.IMAGE_STYLES
import com.aiai.settings.viewmodel.ImageGenSettingsViewModel

/**
 * 图片生成设置：默认尺寸、风格、数量、保存路径、水印。
 */
class ImageGenSettingsActivity : AppCompatActivity() {

    private lateinit var vm: ImageGenSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_gen_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[ImageGenSettingsViewModel::class.java]

        val size = findViewById<SettingSelectorView>(R.id.selectSize)
        size.setEntries(IMAGE_SIZES)
        val style = findViewById<SettingSelectorView>(R.id.selectStyle)
        style.setEntries(IMAGE_STYLES)
        val count = findViewById<SliderView>(R.id.sliderCount)
        val watermark = findViewById<SettingSwitchView>(R.id.swWatermark)

        vm.uiState.observe(this) { s ->
            size.setSelected(s.sizeIndex)
            style.setSelected(s.styleIndex)
            count.setValue(s.count.toFloat())
            watermark.setChecked(s.watermark)
        }

        size.onSelect = { i, _ -> vm.setSize(i) }
        style.onSelect = { i, _ -> vm.setStyle(i) }
        count.onValueChange = { vm.setCount(it.toInt()) }
        watermark.onCheckedChange = { vm.setWatermark(it) }
    }
}
