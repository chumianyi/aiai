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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aiai.settings.R
import com.aiai.settings.ui.view.ProgressRingView
import com.aiai.settings.viewmodel.ExportViewModel

/**
 * 导出进度 Fragment：环形进度 + 当前文件名。
 */
class ExportProgressFragment : Fragment() {

    private lateinit var vm: ExportViewModel

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_export_progress, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        vm = ViewModelProvider(requireActivity())[ExportViewModel::class.java]
        val ring = v.findViewById<ProgressRingView>(R.id.ringProgress)
        val fileName = v.findViewById<TextView>(R.id.tvFileName)
        vm.uiState.observe(viewLifecycleOwner) { st ->
            ring.setProgress(st.progress.percent.toFloat())
            fileName.text = st.progress.fileName
        }
    }
}
