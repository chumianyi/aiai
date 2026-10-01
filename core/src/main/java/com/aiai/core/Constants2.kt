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
package com.aiai.core

/**
 * 全局常量。
 */
object Constants2 {
    const val DB_NAME = "aiai_database.db"
    const val MMKV_ID = "aiai_mmkv"
    const val SP_NAME = "aiai_prefs"

    const val KEY_API_BASE_URL = "api_base_url"
    const val KEY_API_KEY = "api_key"
    const val KEY_MODEL_NAME = "model_name"

    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    const val PAGE_SIZE = 20
    const val CACHE_SIZE = 10 * 1024 * 1024L

    const val DEFAULT_LANGUAGE = "zh"
    const val DEFAULT_THEME = 0
}
