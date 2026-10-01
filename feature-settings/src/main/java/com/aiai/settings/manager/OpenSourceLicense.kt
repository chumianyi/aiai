/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.settings.manager

/** 单个开源许可证条目。 */
data class OpenSourceLicense(
    val name: String,
    val copyright: String,
    val license: String,
    val url: String
)

/**
 * 开源许可证列表。
 *
 * 汇总应用所依赖的主要开源项目及其许可协议，供「关于」页展示。
 */
object OpenSourceLicenses {

    /** 全部开源项目。 */
    val all: List<OpenSourceLicense> = listOf(
        OpenSourceLicense("Kotlin", "JetBrains", "Apache 2.0", "https://kotlinlang.org/"),
        OpenSourceLicense("AndroidX Core", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("AppCompat", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("Material Components", "Google", "Apache 2.0", "https://github.com/material-components/material-components-android"),
        OpenSourceLicense("RecyclerView", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("ConstraintLayout", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("Lifecycle", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("Navigation", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("ViewPager2", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("Hilt", "Google", "Apache 2.0", "https://dagger.dev/hilt/"),
        OpenSourceLicense("Dagger", "Google", "Apache 2.0", "https://dagger.dev/"),
        OpenSourceLicense("Coroutines", "JetBrains", "Apache 2.0", "https://github.com/Kotlin/kotlinx.coroutines"),
        OpenSourceLicense("Retrofit", "Square", "Apache 2.0", "https://github.com/square/retrofit"),
        OpenSourceLicense("OkHttp", "Square", "Apache 2.0", "https://github.com/square/okhttp"),
        OpenSourceLicense("Gson", "Google", "Apache 2.0", "https://github.com/google/gson"),
        OpenSourceLicense("Glide", "BumpTech", "BSD, part MIT", "https://github.com/bumptech/glide"),
        OpenSourceLicense("Room", "Google", "Apache 2.0", "https://developer.android.com/jetpack/androidx"),
        OpenSourceLicense("DataStore", "Google", "Apache 2.0", "https://developer.android.com/topic/libraries/architecture/datastore"),
        OpenSourceLicense("MMKV", "Tencent", "BSD 3-Clause", "https://github.com/Tencent/MMKV"),
        OpenSourceLicense("Markwon", "Noties", "Apache 2.0", "https://github.com/noties/Markwon"),
        OpenSourceLicense("LeakCanary", "Square", "Apache 2.0", "https://github.com/square/leakcanary"),
        OpenSourceLicense("Timber", "Jake Wharton", "Apache 2.0", "https://github.com/JakeWharton/timber"),
        OpenSourceLicense("Moshi", "Square", "Apache 2.0", "https://github.com/square/moshi")
    )
}
