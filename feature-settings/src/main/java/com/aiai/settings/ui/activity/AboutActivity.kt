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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.aiai.settings.R
import com.aiai.settings.manager.UpdateChecker
import kotlinx.coroutines.launch

/**
 * 关于页：版本信息、开源许可、用户协议、检查更新。
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val version = findViewById<TextView>(R.id.tvVersion)
        version.text = "v${UpdateChecker.get(this).currentVersion()}"

        findViewById<android.view.View>(R.id.btnCheckUpdate).setOnClickListener {
            lifecycleScope.launch {
                Toast.makeText(this@AboutActivity, "正在检查…", Toast.LENGTH_SHORT).show()
                val info = UpdateChecker.get(this@AboutActivity).check()
                if (info.hasUpdate) {
                    Toast.makeText(this@AboutActivity, "发现新版本 ${info.latestVersion}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@AboutActivity, "已是最新版本", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
