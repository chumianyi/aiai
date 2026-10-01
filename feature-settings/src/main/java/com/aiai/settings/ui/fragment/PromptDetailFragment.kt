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
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aiai.settings.R

/**
 * 提示词详情 Fragment：展示提示词正文、使用次数、复制按钮。
 */
class PromptDetailFragment : Fragment() {

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_prompt_detail, c, false)

    override fun onViewCreated(v: View, s: Bundle?) {
        val title = arguments?.getString("title") ?: ""
        val content = arguments?.getString("content") ?: ""
        v.findViewById<TextView>(R.id.tvDetailTitle).text = title
        v.findViewById<TextView>(R.id.tvDetailContent).text = content
        v.findViewById<Button>(R.id.btnCopy).setOnClickListener {
            val cm = requireContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)
                    as android.content.ClipboardManager
            cm.setPrimaryClip(android.content.ClipData.newPlainText("prompt", content))
        }
    }

    companion object {
        fun newInstance(title: String, content: String): PromptDetailFragment {
            return PromptDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("title", title)
                    putString("content", content)
                }
            }
        }
    }
}
