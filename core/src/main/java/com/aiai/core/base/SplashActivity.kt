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
import com.aiai.core.Router
import com.aiai.core.RoutePaths

/**
 * 启动页 Activity。
 *
 * 负责：
 * - 显示品牌动画
 * - 初始化检查
 * - 跳转主页或登录页
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 模拟启动延迟
        window.decorView.postDelayed({
            // 判断是否已登录
            if (com.aiai.core.AppConfig.isLoggedIn) {
                Router.navigate(this, RoutePaths.MAIN)
            } else {
                Router.navigate(this, RoutePaths.LOGIN)
            }
            finish()
        }, 1500)
    }
}
