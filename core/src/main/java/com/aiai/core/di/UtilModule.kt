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

import android.content.Context
import com.aiai.common.util.AlarmUtil
import com.aiai.common.util.BiometricUtil
import com.aiai.common.util.BluetoothUtil
import com.aiai.common.util.ClipboardUtil
import com.aiai.common.util.DocumentUtil
import com.aiai.common.util.InputMethodUtil
import com.aiai.common.util.LocationUtil
import com.aiai.common.util.MediaStoreUtil
import com.aiai.common.util.NfcUtil
import com.aiai.common.util.NotificationUtil
import com.aiai.common.util.SensorUtil
import com.aiai.common.util.TransitionUtil
import com.aiai.common.util.WifiUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * 工具类依赖注入模块。
 *
 * 提供各种工具类的依赖注入。
 */
@Module
@InstallIn(ActivityComponent::class)
object UtilModule {

    /**
     * 提供 BiometricUtil。
     */
    @Provides
    @ActivityScoped
    fun provideBiometricUtil(): BiometricUtil {
        return BiometricUtil
    }

    /**
     * 提供 NotificationUtil。
     */
    @Provides
    @ActivityScoped
    fun provideNotificationUtil(): NotificationUtil {
        return NotificationUtil
    }

    /**
     * 提供 AlarmUtil。
     */
    @Provides
    @ActivityScoped
    fun provideAlarmUtil(): AlarmUtil {
        return AlarmUtil
    }

    /**
     * 提供 ClipboardUtil。
     */
    @Provides
    @ActivityScoped
    fun provideClipboardUtil(): ClipboardUtil {
        return ClipboardUtil
    }

    /**
     * 提供 LocationUtil。
     */
    @Provides
    @ActivityScoped
    fun provideLocationUtil(): LocationUtil {
        return LocationUtil
    }

    /**
     * 提供 SensorUtil。
     */
    @Provides
    @ActivityScoped
    fun provideSensorUtil(): SensorUtil {
        return SensorUtil
    }

    /**
     * 提供 BluetoothUtil。
     */
    @Provides
    @ActivityScoped
    fun provideBluetoothUtil(): BluetoothUtil {
        return BluetoothUtil
    }

    /**
     * 提供 NfcUtil。
     */
    @Provides
    @ActivityScoped
    fun provideNfcUtil(): NfcUtil {
        return NfcUtil
    }

    /**
     * 提供 WifiUtil。
     */
    @Provides
    @ActivityScoped
    fun provideWifiUtil(): WifiUtil {
        return WifiUtil
    }

    /**
     * 提供 MediaStoreUtil。
     */
    @Provides
    @ActivityScoped
    fun provideMediaStoreUtil(): MediaStoreUtil {
        return MediaStoreUtil
    }

    /**
     * 提供 DocumentUtil。
     */
    @Provides
    @ActivityScoped
    fun provideDocumentUtil(): DocumentUtil {
        return DocumentUtil
    }

    /**
     * 提供 InputMethodUtil。
     */
    @Provides
    @ActivityScoped
    fun provideInputMethodUtil(): InputMethodUtil {
        return InputMethodUtil
    }

    /**
     * 提供 TransitionUtil。
     */
    @Provides
    @ActivityScoped
    fun provideTransitionUtil(): TransitionUtil {
        return TransitionUtil
    }
}
