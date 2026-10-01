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
import com.aiai.settings.viewmodel.VOICE_ENGINES
import com.aiai.settings.viewmodel.VOICE_TONES
import com.aiai.settings.viewmodel.VoiceSettingsViewModel

/**
 * 语音输入设置：识别引擎、音色、语速、自动播放、快捷键。
 */
class VoiceSettingsActivity : AppCompatActivity() {

    private lateinit var vm: VoiceSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_settings)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        vm = ViewModelProvider(this)[VoiceSettingsViewModel::class.java]

        val engine = findViewById<SettingSelectorView>(R.id.selectEngine)
        engine.setEntries(VOICE_ENGINES)
        val tone = findViewById<SettingSelectorView>(R.id.selectTone)
        tone.setEntries(VOICE_TONES)
        val speed = findViewById<SliderView>(R.id.sliderSpeed)
        val autoPlay = findViewById<SettingSwitchView>(R.id.swAutoPlay)

        vm.uiState.observe(this) { s ->
            engine.setSelected(s.engineIndex)
            tone.setSelected(s.toneIndex)
            speed.setValue(s.speed * 10f)
            autoPlay.setChecked(s.autoPlay)
        }

        engine.onSelect = { i, _ -> vm.setEngine(i) }
        tone.onSelect = { i, _ -> vm.setTone(i) }
        speed.onValueChange = { vm.setSpeed(it / 10f) }
        autoPlay.onCheckedChange = { vm.setAutoPlay(it) }
    }
}
