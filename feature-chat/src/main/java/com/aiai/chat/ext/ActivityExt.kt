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
package com.aiai.chat.ext

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity扩展函数集合
 */

/**
 * Activity显示Toast
 */
fun AppCompatActivity.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * Activity显示长Toast
 */
fun AppCompatActivity.toastLong(message: String) {
    toast(message, Toast.LENGTH_LONG)
}

/**
 * 跳转Activity（带参数）
 */
inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivity(vararg params: Pair<String, String>) {
    val intent = Intent(this, T::class.java)
    params.forEach { (key, value) ->
        intent.putExtra(key, value)
    }
    startActivity(intent)
}

/**
 * 跳转Activity并结束当前
 */
inline fun <reified T : AppCompatActivity> AppCompatActivity.startActivityAndFinish(vararg params: Pair<String, String>) {
    startActivity<T>(*params)
    finish()
}

/**
 * 隐藏状态栏
 */
fun AppCompatActivity.hideStatusBar() {
    window.decorView.systemUiVisibility = (
        android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )
    supportActionBar?.hide()
}

/**
 * 显示状态栏
 */
fun AppCompatActivity.showStatusBar() {
    window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_VISIBLE
    supportActionBar?.show()
}

/**
 * 设置状态栏颜色
 */
fun AppCompatActivity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}
