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
package com.aiai.settings.model

import android.graphics.Color
import androidx.annotation.DrawableRes

/**
 * 设置模块共享数据模型集合。
 *
 * 包含 API 配置、密钥、提示词模板、收藏项、插件、导出任务等领域模型，
 * 供 ViewModel / Manager / Adapter 之间传递使用。
 */

// region API 配置

/**
 * 一组完整的 API 配置。
 *
 * @property id 配置唯一标识
 * @property name 配置名称（用户可读）
 * @property baseUrl 接口基础地址
 * @property apiKey 已加密的 API 密钥（密文，由 KeyEncryptionManager 加解密）
 * @property modelName 默认模型名称
 * @property maxTokens 单次请求最大 token 数
 * @property temperature 采样温度 0~2
 * @property topP 核采样阈值
 * @property createdAt 创建时间戳
 * @property isActive 是否为当前启用配置
 */
data class ApiConfig(
    val id: String,
    val name: String,
    val baseUrl: String,
    val apiKey: String,
    val modelName: String,
    val maxTokens: Int = 2048,
    val temperature: Float = 0.7f,
    val topP: Float = 1.0f,
    val createdAt: Long = System.currentTimeMillis(),
    var isActive: Boolean = false
) {
    /** 密钥掩码展示，仅保留前 3 后 4 位。 */
    fun maskedKey(): String {
        if (apiKey.length <= 8) return "****"
        return "${apiKey.take(3)}****${apiKey.takeLast(4)}"
    }

    companion object {
        /** 生成新配置的唯一 id。 */
        fun newId(): String = "cfg_${System.currentTimeMillis()}_${(0..999999).random()}"

        /** 默认占位配置。 */
        fun default(): ApiConfig = ApiConfig(
            id = newId(),
            name = "默认配置",
            baseUrl = "https://api.openai.com/v1",
            apiKey = "",
            modelName = "gpt-4o-mini"
        )
    }
}

/**
 * API 连接测试结果。
 *
 * @property success 是否成功
 * @property latencyMs 往返延迟（毫秒）
 * @property httpCode HTTP 状态码
 * @property message 描述信息
 * @property serverTime 服务端返回时间
 */
data class ApiTestResult(
    val success: Boolean,
    val latencyMs: Long = 0,
    val httpCode: Int = 0,
    val message: String = "",
    val serverTime: String = ""
)

// endregion

// region 密钥管理

/**
 * 单条密钥信息（列表展示用，不含明文）。
 *
 * @property id 密钥 id
 * @property alias 别名
 * @property provider 提供商（OpenAI / Anthropic / 自定义）
 * @property encryptedValue 加密后的密文
 * @property createdAt 创建时间
 * @property lastUsedAt 最近使用时间
 * @property usageCount 累计使用次数
 * @property isExpired 是否已过期
 */
data class KeyInfo(
    val id: String,
    var alias: String,
    var provider: String,
    var encryptedValue: String,
    val createdAt: Long = System.currentTimeMillis(),
    var lastUsedAt: Long = 0L,
    var usageCount: Long = 0L,
    var isExpired: Boolean = false
) {
    companion object {
        fun newId(): String = "key_${System.currentTimeMillis()}_${(0..999999).random()}"
    }
}

// endregion

// region 提示词模板

/** 提示词分类。 */
enum class PromptCategory(val displayName: String) {
    GENERAL("通用"),
    WRITING("写作"),
    CODE("编程"),
    TRANSLATE("翻译"),
    SUMMARY("总结"),
    CREATIVE("创意"),
    BUSINESS("商务"),
    LEARNING("学习"),
    CUSTOM("自定义");

    companion object {
        fun fromName(name: String): PromptCategory =
            entries.firstOrNull { it.name == name } ?: CUSTOM
    }
}

/**
 * 提示词模板。
 *
 * @property id 模板 id
 * @property title 标题
 * @property content 提示词正文
 * @property category 分类
 * @property tags 标签
 * @property isBuiltin 是否内置
 * @property isFavorite 是否已收藏
 * @property useCount 使用次数
 * @property createdAt 创建时间
 * @property description 描述
 */
data class PromptTemplate(
    val id: String,
    var title: String,
    var content: String,
    var category: PromptCategory,
    var tags: List<String> = emptyList(),
    var isBuiltin: Boolean = false,
    var isFavorite: Boolean = false,
    var useCount: Long = 0L,
    val createdAt: Long = System.currentTimeMillis(),
    var description: String = ""
) {
    companion object {
        fun newId(): String = "prompt_${System.currentTimeMillis()}_${(0..999999).random()}"
    }
}

// endregion

// region 收藏

/** 收藏内容类型。 */
enum class FavoriteType {
    PROMPT,
    CHAT_MESSAGE,
    IMAGE,
    ARTICLE
}

/**
 * 收藏项。
 *
 * @property id 收藏 id
 * @property type 收藏类型
 * @property title 标题
 * @property summary 摘要
 * @property content 完整内容
 * @property coverUrl 封面
 * @property sourceId 来源业务 id（会话 id / 消息 id）
 * @property category 自定义分组
 * @property createdAt 收藏时间
 */
data class FavoriteItem(
    val id: String,
    var type: FavoriteType,
    var title: String,
    var summary: String,
    var content: String,
    var coverUrl: String? = null,
    var sourceId: String? = null,
    var category: String = "未分组",
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun newId(): String = "fav_${System.currentTimeMillis()}_${(0..999999).random()}"
    }
}

// endregion

// region 导出

/** 导出格式枚举。 */
enum class ExportFormat(val extension: String, val displayName: String) {
    TXT("txt", "纯文本"),
    MD("md", "Markdown"),
    JSON("json", "JSON"),
    PDF("pdf", "PDF");

    companion object {
        fun fromExtension(ext: String): ExportFormat =
            entries.firstOrNull { it.extension.equals(ext, ignoreCase = true) } ?: TXT
    }
}

/**
 * 待导出的会话摘要。
 *
 * @property conversationId 会话 id
 * @property title 会话标题
 * @property messageCount 消息条数
 * @property updatedAt 更新时间
 * @property checked 是否选中导出
 */
data class ExportConversation(
    val conversationId: String,
    var title: String,
    val messageCount: Int,
    val updatedAt: Long,
    var checked: Boolean = false
)

/**
 * 导出任务进度。
 *
 * @property total 总条目
 * @property processed 已处理
 * @property fileName 输出文件名
 * @property state 当前状态
 */
data class ExportProgress(
    val total: Int = 0,
    val processed: Int = 0,
    val fileName: String = "",
    val state: ExportState = ExportState.IDLE
) {
    /** 0~100 百分比。 */
    val percent: Int
        get() = if (total <= 0) 0 else (processed * 100 / total).coerceIn(0, 100)
}

/** 导出状态机。 */
enum class ExportState {
    IDLE,
    RUNNING,
    SUCCESS,
    FAILED,
    CANCELLED
}

// endregion

// region 插件

/** 插件运行状态。 */
enum class PluginStatus {
    NOT_INSTALLED,
    INSTALLING,
    INSTALLED,
    ENABLED,
    DISABLED,
    ERROR
}

/**
 * 插件信息。
 *
 * @property id 插件 id
 * @property name 名称
 * @property description 描述
 * @property version 版本
 * @property author 作者
 * @property iconRes 图标资源（内置插件用）
 * @property downloadSize 体积（字节）
 * @property status 状态
 * @property permissions 所需权限
 * @property isBuiltin 是否内置
 */
data class PluginInfo(
    val id: String,
    var name: String,
    var description: String,
    var version: String,
    var author: String,
    @DrawableRes var iconRes: Int = 0,
    var downloadSize: Long = 0L,
    var status: PluginStatus = PluginStatus.NOT_INSTALLED,
    var permissions: List<String> = emptyList(),
    var isBuiltin: Boolean = false
)

// endregion

// region 设置项 UI 模型

/** 设置列表条目类型。 */
sealed class SettingItem {
    abstract val key: String
    abstract val title: String

    /** 普通跳转项。 */
    data class Normal(
        override val key: String,
        override val title: String,
        val subtitle: String = "",
        @DrawableRes val iconRes: Int = 0,
        val target: Class<*>? = null
    ) : SettingItem()

    /** 开关项。 */
    data class Switch(
        override val key: String,
        override val title: String,
        val subtitle: String = "",
        @DrawableRes val iconRes: Int = 0,
        var checked: Boolean = false
    ) : SettingItem()

    /** 选择器项（右侧显示当前值，点击弹窗）。 */
    data class Selector(
        override val key: String,
        override val title: String,
        val entries: List<String> = emptyList(),
        var selectedIndex: Int = 0
    ) : SettingItem()

    /** 分组标题。 */
    data class Header(
        override val key: String,
        override val title: String
    ) : SettingItem()
}

/**
 * 主题模式。
 */
enum class ThemeMode(val displayName: String) {
    SYSTEM("跟随系统"),
    LIGHT("浅色模式"),
    DARK("深色模式"),
    AMOLED("AMOLED 深色");

    companion object {
        fun fromName(name: String): ThemeMode =
            entries.firstOrNull { it.name == name } ?: SYSTEM
    }
}

/** 字体大小档位（5 档）。 */
enum class FontScale(val scale: Float, val displayName: String) {
    SMALL(0.85f, "小"),
    MEDIUM(1.0f, "标准"),
    LARGE(1.15f, "大"),
    XLARGE(1.3f, "超大"),
    XXLARGE(1.5f, "特大");

    companion object {
        fun fromScale(scale: Float): FontScale =
            entries.firstOrNull { kotlin.math.abs(it.scale - scale) < 0.01f } ?: MEDIUM
    }
}

/** 气泡样式（5 种）。 */
enum class BubbleStyle(val displayName: String) {
    ROUNDED("圆角气泡"),
    SHARP("直角气泡"),
    SHADOW("投影气泡"),
    TRANSPARENT("半透明气泡"),
    MINIMAL("极简气泡");

    companion object {
        fun fromName(name: String): BubbleStyle =
            entries.firstOrNull { it.name == name } ?: ROUNDED
    }
}

/** 支持的语言。 */
enum class AppLanguage(val displayName: String, val tag: String) {
    SYSTEM("跟随系统", ""),
    ZH_CN("简体中文", "zh-CN"),
    EN("English", "en"),
    JA("日本語", "ja"),
    KO("한국어", "ko");

    companion object {
        fun fromTag(tag: String): AppLanguage =
            entries.firstOrNull { it.tag.equals(tag, ignoreCase = true) } ?: SYSTEM
    }
}

/** 默认占位颜色。 */
object ColorPalette {
    val DEFAULT_COLORS = listOf(
        Color.parseColor("#3D5AFE"),
        Color.parseColor("#E53935"),
        Color.parseColor("#D81B60"),
        Color.parseColor("#8E24AA"),
        Color.parseColor("#5E35B1"),
        Color.parseColor("#1E88E5"),
        Color.parseColor("#00ACC1"),
        Color.parseColor("#00897B"),
        Color.parseColor("#43A047"),
        Color.parseColor("#FB8C00"),
        Color.parseColor("#F4511E"),
        Color.parseColor("#607D8B")
    )
}
