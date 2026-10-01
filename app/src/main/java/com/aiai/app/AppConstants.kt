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

/**
 * 应用级常量定义
 */
object AppConstants {

    // === Meta ===
    const val APP_NAME = "爱Ai"
    const val APP_PACKAGE = "com.aiai.app"
    const val APP_VERSION = "1.0.0"

    // === MMKV keys ===
    const val MMKV_ID = "aiai_mmkv"
    const val PREF_NAME = "aiai_prefs"

    // === SharedPreferences keys ===
    const val KEY_API_BASE_URL = "api_base_url"
    const val KEY_API_KEY = "api_key"
    const val KEY_MODEL_NAME = "model_name"
    const val KEY_THEME_MODE = "theme_mode"
    const val KEY_LANGUAGE = "language"
    const val KEY_FONT_SIZE = "font_size"
    const val KEY_FIRST_LAUNCH = "first_launch"
    const val KEY_ONBOARDING_DONE = "onboarding_done"
    const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
    const val KEY_SOUND_ENABLED = "sound_enabled"
    const val KEY_VIBRATE_ENABLED = "vibrate_enabled"
    const val KEY_STREAM_RESPONSE = "stream_response"
    const val KEY_MAX_TOKENS = "max_tokens"
    const val KEY_TEMPERATURE = "temperature"
    const val KEY_TOP_P = "top_p"
    const val KEY_SYSTEM_PROMPT = "system_prompt"
    const val KEY_DYNAMIC_COLOR = "dynamic_color"
    const val KEY_BUBBLE_STYLE = "bubble_style"
    const val KEY_MARKDOWN_RENDER = "markdown_render"
    const val KEY_CODE_HIGHLIGHT = "code_highlight"
    const val KEY_AUTO_SCROLL = "auto_scroll"
    const val KEY_SAVE_HISTORY = "save_history"
    const val KEY_CLEAR_CACHE_ON_EXIT = "clear_cache_on_exit"
    const val KEY_LAST_BACKUP_TIME = "last_backup_time"
    const val KEY_LAST_CLEAR_CACHE_TIME = "last_clear_cache_time"

    // === Database ===
    const val DB_NAME = "aiai_database.db"
    const val DB_VERSION = 1

    // === Network ===
    const val DEFAULT_BASE_URL = "https://api.openai.com/v1/"
    const val DEFAULT_MODEL = "gpt-3.5-turbo"
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 60L
    const val WRITE_TIMEOUT_SECONDS = 60L
    const val MAX_RETRY = 3
    const val RETRY_DELAY_MS = 1000L

    // === Notification ===
    const val NOTIFICATION_CHANNEL_CHAT = "channel_chat"
    const val NOTIFICATION_CHANNEL_DEFAULT = "channel_default"
    const val NOTIFICATION_ID_CHAT = 1001
    const val NOTIFICATION_ID_SERVICE = 1002

    // === Request codes ===
    const val REQUEST_PICK_IMAGE = 2001
    const val REQUEST_PICK_FILE = 2002
    const val REQUEST_CAMERA = 2003
    const val REQUEST_STORAGE_PERMISSION = 2004
    const val REQUEST_AUDIO_PERMISSION = 2005
    const val REQUEST_NOTIFICATION_PERMISSION = 2006

    // === Result codes ===
    const val RESULT_SETTINGS_CHANGED = 3001

    // === Fragment tags ===
    const val TAG_CHAT_FRAGMENT = "tag_chat_fragment"
    const val TAG_SETTINGS_FRAGMENT = "tag_settings_fragment"

    // === Limits ===
    const val MAX_MESSAGE_LENGTH = 32000
    const val MAX_CONVERSATION_TITLE_LENGTH = 50
    const val MAX_PROMPT_LENGTH = 2000
    const val MIN_TEMPERATURE = 0.0
    const val MAX_TEMPERATURE = 2.0
    const val DEFAULT_TEMPERATURE = 0.7
    const val DEFAULT_MAX_TOKENS = 2048
    const val DEFAULT_TOP_P = 1.0

    // === DeepLink ===
    const val DEEPLINK_SCHEME = "aiai"
    const val DEEPLINK_HOST = "app"

    // === FileProvider ===
    const val FILE_PROVIDER_AUTHORITY = "com.aiai.app.fileprovider"

    // === Backup ===
    const val BACKUP_FILENAME = "aiai_backup.json"
    const val EXPORT_FILENAME_PREFIX = "aiai_export_"
}
