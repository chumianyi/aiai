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
import com.aiai.settings.model.BubbleStyle
import com.aiai.settings.model.FontScale
import com.aiai.settings.model.ThemeMode
import com.aiai.settings.ui.view.BubbleStylePreview
import com.aiai.settings.ui.view.ColorPickerView
import com.aiai.settings.ui.view.SettingSelectorView
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.ui.view.ThemePreview
import com.aiai.settings.viewmodel.AppearanceSettingsViewModel

/**
 * 外观设置：深色模式、字体大小、气泡样式、主题色、动画。
 */
class AppearanceSettingsActivity : AppCompatActivity() {

    private lateinit var vm: AppearanceSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appearance_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[AppearanceSettingsViewModel::class.java]

        val themeMode = findViewById<SettingSelectorView>(R.id.selectThemeMode)
        themeMode.setEntries(ThemeMode.entries.map { it.displayName })
        val fontScale = findViewById<SettingSelectorView>(R.id.selectFont)
        fontScale.setEntries(FontScale.entries.map { it.displayName })
        val bubble = findViewById<SettingSelectorView>(R.id.selectBubble)
        bubble.setEntries(BubbleStyle.entries.map { it.displayName })
        val anim = findViewById<SettingSwitchView>(R.id.swAnim)
        val colorPicker = findViewById<ColorPickerView>(R.id.colorPicker)
        val preview = findViewById<BubbleStylePreview>(R.id.bubblePreview)
        val themePreview = findViewById<ThemePreview>(R.id.themePreview)

        vm.uiState.observe(this) { s ->
            themeMode.setSelected(ThemeMode.entries.indexOf(s.themeMode))
            fontScale.setSelected(FontScale.entries.indexOf(s.fontScale))
            bubble.setSelected(BubbleStyle.entries.indexOf(s.bubbleStyle))
            anim.setChecked(s.animEnabled)
            preview.setStyle(s.bubbleStyle)
            themePreview.setMode(s.themeMode)
        }

        themeMode.onSelect = { i, _ -> vm.setThemeMode(ThemeMode.entries[i]) }
        fontScale.onSelect = { i, _ -> vm.setFontScale(FontScale.entries[i]) }
        bubble.onSelect = { i, _ -> vm.setBubbleStyle(BubbleStyle.entries[i]) }
        anim.onCheckedChange = { vm.setAnimEnabled(it) }
        colorPicker.onColorSelected = { vm.setThemeColor(it) }
    }
}
