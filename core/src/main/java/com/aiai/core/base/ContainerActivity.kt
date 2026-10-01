/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
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
 * See the License for the specific permissions and
 * limitations under the License.
 */
package com.aiai.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aiai.core.R

/**
 * 通用 Fragment 容器 Activity。
 *
 * 用于承载单个 Fragment，通过 extra 指定 Fragment 类名。
 */
class ContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        val fragmentClass = intent.getStringExtra(EXTRA_FRAGMENT_CLASS) ?: return
        val fragment = try {
            Class.forName(fragmentClass).newInstance() as Fragment
        } catch (e: Exception) {
            return
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    companion object {
        const val EXTRA_FRAGMENT_CLASS = "extra_fragment_class"
    }
}
