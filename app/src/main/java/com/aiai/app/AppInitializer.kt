/*
 * Copyright (c) 2024 AiAi. All rights reserved.
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
package com.aiai.app

import android.content.Context
import androidx.startup.Initializer
import com.aiai.core.util.LogUtil

/**
 * 应用启动初始化器
 *
 * 使用 Jetpack Startup 进行依赖初始化，在ContentProvider阶段提前完成。
 */
class AppInitializer : Initializer<Unit> {

    companion object {
        private const val TAG = "AppInitializer"
    }

    override fun create(context: Context) {
        LogUtil.d(TAG, "AppInitializer create")
        // LogUtil在Application中初始化，这里做额外准备工作
        // 例如预加载资源、预热数据库连接等
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
