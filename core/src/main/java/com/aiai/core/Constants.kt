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
 * See the License for the specific language permissions and
 * limitations under the License.
 */
package com.aiai.core

/**
 * 全局常量定义。
 *
 * 包含 API 配置键、数据库名、缓存目录、超时时间、默认值等。
 */
object Constants {

    // region API 配置键
    const val KEY_API_BASE_URL = "api_base_url"
    const val KEY_API_KEY = "api_key"
    const val KEY_MODEL_NAME = "model_name"
    const val KEY_API_TIMEOUT = "api_timeout"
    const val KEY_API_RETRY = "api_retry_count"
    const val KEY_API_HEADERS = "api_headers"
    const val KEY_API_VERSION = "api_version"
    // endregion

    // region 数据库
    const val DATABASE_NAME = "aiai_database.db"
    const val DATABASE_VERSION = 1
    const val TABLE_CHAT_MESSAGE = "chat_message"
    const val TABLE_CHAT_SESSION = "chat_session"
    const val TABLE_CONVERSATION = "conversation"
    // endregion

    // region 存储
    const val MMKV_ID = "aiai_mmkv"
    const val SP_NAME = "aiai_prefs"
    const val CACHE_DIR_NAME = "aiai_cache"
    const val IMAGE_CACHE_DIR = "image_cache"
    const val VIDEO_CACHE_DIR = "video_cache"
    const val LOG_CACHE_DIR = "log_cache"
    const val CRASH_CACHE_DIR = "crash_cache"
    // endregion

    // region 网络超时
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 60L
    const val WRITE_TIMEOUT = 60L
    const val PING_INTERVAL = 30L
    // endregion

    // region 默认值
    const val DEFAULT_PAGE_SIZE = 20
    const val DEFAULT_PAGE_INDEX = 1
    const val DEFAULT_DIALOG_DURATION = 300L
    const val DEFAULT_ANIM_DURATION = 250L
    const val DEFAULT_CLICK_DEBOUNCE = 500L
    const val DEFAULT_TEXT_SIZE = 14f
    const val DEFAULT_LINE_SPACING = 1.5f
    // endregion

    // region 分页
    const val PAGE_START_INDEX = 1
    const val PAGE_SIZE_DEFAULT = 20
    const val PAGE_SIZE_MAX = 100
    // endregion

    // region 线程名
    const val THREAD_MAIN = "main"
    const val THREAD_IO = "io"
    const val THREAD_DEFAULT = "default"
    // endregion

    // region 事件总线
    const val EVENT_LOGIN_SUCCESS = "event_login_success"
    const val EVENT_LOGOUT = "event_logout"
    const val EVENT_THEME_CHANGED = "event_theme_changed"
    const val EVENT_LANGUAGE_CHANGED = "event_language_changed"
    const val EVENT_NETWORK_CHANGED = "event_network_changed"
    const val EVENT_CHAT_MESSAGE_RECEIVED = "event_chat_message_received"
    const val EVENT_CHAT_SEND_SUCCESS = "event_chat_send_success"
    const val EVENT_CHAT_SEND_FAILED = "event_chat_send_failed"
    const val EVENT_SESSION_UPDATED = "event_session_updated"
    const val EVENT_CONFIG_CHANGED = "event_config_changed"
    // endregion

    // region 错误码
    const val CODE_SUCCESS = 200
    const val CODE_UNAUTHORIZED = 401
    const val CODE_FORBIDDEN = 403
    const val CODE_NOT_FOUND = 404
    const val CODE_SERVER_ERROR = 500
    // endregion

    // region 缓存
    const val CACHE_MAX_SIZE = 50L * 1024 * 1024 // 50MB
    const val CACHE_MAX_FILE_COUNT = 200
    const val CACHE_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L // 7天
    // endregion

    // region 日志
    const val LOG_MAX_FILE_SIZE = 5L * 1024 * 1024 // 5MB
    const val LOG_MAX_FILE_COUNT = 7
    const val LOG_DIR = "logs"
    // endregion
}
