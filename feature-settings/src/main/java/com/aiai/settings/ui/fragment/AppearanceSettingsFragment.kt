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
package com.aiai.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.model.BubbleStyle
import com.aiai.settings.model.FontScale
import com.aiai.settings.model.ThemeMode
import com.aiai.settings.ui.view.BubbleStylePreview
import com.aiai.settings.ui.view.ColorPickerView
import com.aiai.settings.ui.view.SettingSelectorView
import com.aiai.settings.ui.view.SettingSwitchView
import com.aiai.settings.viewmodel.AppearanceSettingsViewModel

/**
 * 外观设置 Fragment。
 */
class AppearanceSettingsFragment : Fragment() {

    private lateinit var vm: AppearanceSettingsViewModel

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_appearance_settings, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(this)[AppearanceSettingsViewModel::class.java]
        val themeMode = v.findViewById<SettingSelectorView>(R.id.selectThemeMode)
        themeMode.setEntries(ThemeMode.entries.map { it.displayName })
        val font = v.findViewById<SettingSelectorView>(R.id.selectFont)
        font.setEntries(FontScale.entries.map { it.displayName })
        val bubble = v.findViewById<SettingSelectorView>(R.id.selectBubble)
        bubble.setEntries(BubbleStyle.entries.map { it.displayName })
        val anim = v.findViewById<SettingSwitchView>(R.id.swAnim)
        val color = v.findViewById<ColorPickerView>(R.id.colorPicker)
        val preview = v.findViewById<BubbleStylePreview>(R.id.bubblePreview)

        vm.uiState.observe(viewLifecycleOwner) { st ->
            themeMode.setSelected(ThemeMode.entries.indexOf(st.themeMode))
            font.setSelected(FontScale.entries.indexOf(st.fontScale))
            bubble.setSelected(BubbleStyle.entries.indexOf(st.bubbleStyle))
            anim.setChecked(st.animEnabled)
            preview.setStyle(st.bubbleStyle)
        }
        themeMode.onSelect = { i, _ -> vm.setThemeMode(ThemeMode.entries[i]) }
        font.onSelect = { i, _ -> vm.setFontScale(FontScale.entries[i]) }
        bubble.onSelect = { i, _ -> vm.setBubbleStyle(BubbleStyle.entries[i]) }
        anim.onCheckedChange = { vm.setAnimEnabled(it) }
        color.onColorSelected = { vm.setThemeColor(it) }
    }
}
