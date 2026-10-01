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
import com.aiai.core.util.LogUtil
import com.tencent.mmkv.MMKV
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 应用级Hilt依赖注入Module
 *
 * 提供应用级单例依赖。
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMMKV(): MMKV {
        return MMKV.mmkvWithID(AppConstants.MMKV_ID)
    }

    @Provides
    @Singleton
    fun provideFeatureFlagManager(@ApplicationContext context: Context): FeatureFlagManager {
        return FeatureFlagManager(context)
    }

    @Provides
    @Singleton
    fun provideGlobalExceptionHandler(@ApplicationContext context: Context): GlobalExceptionHandler {
        return GlobalExceptionHandler(context)
    }

    @Provides
    @Singleton
    fun provideAppLifecycleObserver(): AppLifecycleObserver {
        return AppLifecycleObserver()
    }
}
