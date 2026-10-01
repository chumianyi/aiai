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
import dagger.hilt.android.components.ApplicationComponent

/**
 * DataSource 绑定模块（占位）。
 *
 * 实际 DataSource 在 data/network 模块中绑定。
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class DataSourceModule {
    // 实际绑定在 data 模块中完成
}
