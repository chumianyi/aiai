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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.core.di

import android.app.Activity
import android.content.Context
import com.aiai.core.manager.ActivityResultManager
import com.aiai.core.manager.KeyboardManager
import com.aiai.core.manager.MediaManager
import com.aiai.core.manager.PermissionManager
import com.aiai.core.manager.PowerManager
import com.aiai.core.manager.ScreenManager
import com.aiai.core.manager.StorageManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * 管理器依赖注入模块。
 *
 * 提供各种管理器的单例实例。
 */
@Module
@InstallIn(ActivityComponent::class)
object ManagerModule {

    /**
     * 提供 ActivityResultManager。
     */
    @Provides
    @ActivityScoped
    fun provideActivityResultManager(): ActivityResultManager {
        return ActivityResultManager()
    }

    /**
     * 提供 PermissionManager。
     */
    @Provides
    @ActivityScoped
    fun providePermissionManager(@ApplicationContext context: Context): PermissionManager {
        return PermissionManager(context)
    }

    /**
     * 提供 KeyboardManager。
     */
    @Provides
    @ActivityScoped
    fun provideKeyboardManager(activity: Activity): KeyboardManager {
        return KeyboardManager(activity)
    }

    /**
     * 提供 ScreenManager。
     */
    @Provides
    @ActivityScoped
    fun provideScreenManager(activity: Activity): ScreenManager {
        return ScreenManager(activity)
    }

    /**
     * 提供 PowerManager。
     */
    @Provides
    @ActivityScoped
    fun providePowerManager(@ApplicationContext context: Context): PowerManager {
        return PowerManager(context)
    }

    /**
     * 提供 StorageManager。
     */
    @Provides
    @ActivityScoped
    fun provideStorageManager(@ApplicationContext context: Context): StorageManager {
        return StorageManager(context)
    }

    /**
     * 提供 MediaManager。
     */
    @Provides
    @ActivityScoped
    fun provideMediaManager(@ApplicationContext context: Context): MediaManager {
        return MediaManager(context)
    }
}
