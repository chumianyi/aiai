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
package com.aiai.core.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.SingletonComponent

/**
 * Network 模块（占位引用）。
 *
 * 实际网络模块的依赖配置在 :network 模块中。
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {
    // 实际网络配置在 network 模块中
}
