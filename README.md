# 爱Ai (AiAi) - 智能对话助手

爱Ai 是一款基于Android的AI对话应用，支持多种大语言模型，提供流畅自然的对话体验。

## 目录

- [项目介绍](#项目介绍)
- [功能特性](#功能特性)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [环境要求](#环境要求)
- [构建步骤](#构建步骤)
- [配置说明](#配置说明)
- [使用说明](#使用说明)
- [代码规范](#代码规范)
- [测试说明](#测试说明)
- [贡献指南](#贡献指南)
- [许可证](#许可证)
- [更新日志](#更新日志)
- [FAQ](#faq)

## 项目介绍

爱Ai（AiAi）是一款开源的Android AI聊天应用，允许用户通过自定义API接口连接多种大语言模型服务。应用注重隐私安全，所有聊天数据默认存储在本地，不上传云端。

### 核心价值

- **隐私优先**：所有数据本地存储，保护用户隐私
- **灵活配置**：支持自定义API地址、密钥和模型参数
- **流畅体验**：流式输出、Markdown渲染、代码高亮
- **多语言**：支持中文和英文界面
- **主题丰富**：支持浅色/深色/动态取色主题

## 功能特性

### 对话功能
- [x] 多模型对话支持（GPT、Claude、Gemini等）
- [x] 流式输出（逐字显示AI回复）
- [x] Markdown格式渲染
- [x] 代码块语法高亮
- [x] 消息复制、重新生成、朗读
- [x] 对话历史管理（新建、重命名、删除、导出）
- [x] 消息搜索

### 提示词库
- [x] 内置多种场景提示词模板
- [x] 自定义提示词创建
- [x] 提示词分类和搜索
- [x] 收藏常用提示词

### 设置功能
- [x] API接口配置（URL、密钥、模型）
- [x] 模型参数调节（温度、最大Token、Top P）
- [x] 主题模式切换（浅色/深色/跟随系统）
- [x] 字体大小调节
- [x] 语言切换（中文/英文）
- [x] 通知设置
- [x] 数据备份与恢复

### 其他特性
- [x] 桌面小组件（快速新建对话）
- [x] 应用快捷方式（长按图标）
- [x] 深链接支持
- [x] 分享接收（外部应用分享文本到爱Ai）
- [x] 全局崩溃捕获
- [x] 功能开关管理

## 技术栈

### 开发语言
- **Kotlin** - 主要开发语言
- **Java** - 兼容部分第三方库

### 构建工具
- **Gradle** 8.2.2 - 构建系统
- **Android Gradle Plugin** 8.2.2
- **KSP / KAPT** - 注解处理

### 架构与依赖注入
- **MVVM** - 架构模式
- **Hilt** 2.50 - 依赖注入框架
- **Coroutines + Flow** - 异步处理
- **StateFlow / LiveData** - UI状态管理

### 网络
- **Retrofit** 2.9.0 - HTTP客户端
- **OkHttp** 4.12.0 - 底层网络库
- **Gson** 2.10.1 - JSON解析
- **OkHttp-SSE** - 流式请求支持

### 本地存储
- **Room** 2.6.1 - 数据库ORM
- **MMKV** 1.3.4 - 键值存储
- **DataStore** 1.0.0 - 偏好存储

### UI
- **Material3** 1.11.0 - 设计组件
- **Navigation Component** 2.7.6 - 页面导航
- **ViewPager2** - 视图滑动
- **RecyclerView** - 列表
- **SwipeRefreshLayout** - 下拉刷新

### 渲染
- **Markwon** 4.6.2 - Markdown渲染
- **Glide** 4.16.0 - 图片加载
- **Media3 (ExoPlayer)** - 音频播放

### 测试
- **JUnit** 4.13.2 - 单元测试
- **Espresso** 3.5.1 - UI测试
- **MockK** 1.13.8 - Mock框架
- **Turbine** - Flow测试
- **Truth** - 断言库

## 项目结构

```
AiAi/
├── app/                    # 应用主模块
│   ├── src/main/
│   │   ├── java/com/aiai/app/     # Kotlin源码
│   │   ├── res/                   # 资源文件
│   │   └── AndroidManifest.xml    # 清单文件
│   ├── src/test/                  # 单元测试
│   └── src/androidTest/           # UI测试
├── core/                   # 核心工具模块
├── common/                 # 通用扩展模块
├── network/                # 网络请求模块
├── data/                   # 数据层模块
├── feature-chat/           # 聊天功能模块
├── feature-settings/        # 设置功能模块
├── gradle/                 # Gradle版本目录
├── build.gradle.kts        # 项目构建文件
├── settings.gradle.kts     # 模块配置
└── gradle.properties       # Gradle属性
```

## 环境要求

- **Android Studio** Hedgehog | 2023.1.1 或更高版本
- **JDK** 17
- **Android SDK** compileSdk 34, minSdk 24
- **Gradle** 8.2+
- **内存** 8GB以上推荐
- **磁盘空间** 2GB以上

## 构建步骤

1. 克隆仓库：
   ```bash
   git clone https://github.com/aiai/aiai-android.git
   cd AiAi
   ```

2. 使用Android Studio打开项目，等待Gradle同步完成。

3. 构建Debug版本：
   ```bash
   ./gradlew assembleDebug
   ```

4. 构建Release版本：
   ```bash
   ./gradlew assembleRelease
   ```

5. 安装到设备：
   ```bash
   ./gradlew installDebug
   ```

## 配置说明

### API接口配置

首次使用需要配置API信息：

1. 打开应用，进入「设置」
2. 选择「API接口配置」
3. 填写以下信息：

| 配置项 | 说明 | 默认值 |
|--------|------|--------|
| API Base URL | API服务地址 | https://api.openai.com/v1/ |
| API Key | 身份验证密钥 | （无默认值，需用户填写） |
| Model Name | 使用的模型名称 | gpt-3.5-turbo |

### 模型参数配置

| 参数 | 说明 | 范围 | 默认值 |
|------|------|------|--------|
| Temperature | 创造性/随机性 | 0.0 - 2.0 | 0.7 |
| Max Tokens | 最大回复长度 | 1 - 128000 | 2048 |
| Top P | 核采样 | 0.0 - 1.0 | 1.0 |
| Stream | 流式输出 | 开/关 | 开 |

### 支持的模型

- OpenAI: GPT-3.5 Turbo, GPT-4, GPT-4 Turbo, GPT-4o
- Anthropic: Claude 3, Claude 3.5 Sonnet
- Google: Gemini Pro
- 其他兼容OpenAI API格式的服务

## 使用说明

### 开始对话
1. 打开应用，自动进入对话页
2. 在底部输入框输入消息
3. 点击发送按钮或按回车发送
4. AI将流式返回回复

### 管理对话
- 点击左上角菜单查看对话列表
- 长按对话项可进行重命名、删除、导出操作
- 下拉刷新对话列表

### 使用提示词
1. 进入提示词库
2. 选择分类浏览提示词
3. 点击「使用此提示词」快速开始对话

### 自定义设置
- 进入「设置」-「主题」切换深色模式
- 进入「设置」-「语言」切换中英文
- 进入「设置」-「字体大小」调整文字大小

## 代码规范

### Kotlin编码规范
- 遵循 [Kotlin官方编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 类名使用大驼峰（PascalCase）
- 方法名和变量名使用小驼峰（camelCase）
- 常量使用全大写下划线分隔（UPPER_SNAKE_CASE）
- 私有属性以下划线开头

### 资源命名规范
- 布局文件：`activity_xxx.xml` / `fragment_xxx.xml` / `item_xxx.xml`
- drawable：`bg_xxx.xml` / `ic_xxx.xml` / `selector_xxx.xml`
- 字符串：小写下划线分隔
- 颜色：语义化命名

### 注释规范
- 公开API必须有KDoc注释
- 类注释说明职责和用法
- 复杂逻辑添加行内注释

## 测试说明

### 单元测试
运行所有单元测试：
```bash
./gradlew test
```

单元测试覆盖：
- 字符串扩展工具
- 集合扩展工具
- 日期时间工具
- 加密工具（AES、RSA、MD5、SHA）
- 正则表达式工具
- JSON序列化
- 文件操作工具
- 重试机制

### UI测试
运行所有UI测试：
```bash
./gradlew connectedAndroidTest
```

UI测试覆盖：
- 主界面导航
- 聊天页面交互
- 设置页面
- API配置页
- 会话列表
- 搜索功能
- 提示词库
- 关于页面
- 主题切换
- 语言切换

## 贡献指南

我们欢迎任何形式的贡献！

### 贡献流程
1. Fork本仓库
2. 创建特性分支：`git checkout -b feature/amazing-feature`
3. 提交更改：`git commit -m 'Add amazing feature'`
4. 推送分支：`git push origin feature/amazing-feature`
5. 创建Pull Request

### 贡献内容
- Bug修复
- 新功能开发
- 文档完善
- 测试用例补充
- 国际化翻译

### 提交规范
- 使用约定式提交（Conventional Commits）
- `feat:` 新功能
- `fix:` Bug修复
- `docs:` 文档变更
- `refactor:` 重构
- `test:` 测试相关
- `chore:` 构建/工具变更

## 许可证

本项目采用 Apache License 2.0 许可证。

```
Copyright (c) 2024 AiAi. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
```

## 更新日志

### v1.0.0 (2024-01-01)
- 首次发布
- 支持多模型对话
- Markdown渲染和代码高亮
- 提示词库
- 主题和语言切换
- 桌面小组件
- 数据本地存储

## FAQ

### Q: 应用需要联网吗？
A: 是的，应用需要连接网络才能与AI模型服务器通信。聊天数据本身存储在本地。

### Q: 我的聊天记录会被上传吗？
A: 不会。所有聊天记录默认存储在您的设备本地数据库中，不会上传到任何服务器。

### Q: 支持哪些AI模型？
A: 任何兼容OpenAI API格式的模型服务都可以使用，包括OpenAI GPT系列、Anthropic Claude、Google Gemini等。

### Q: 如何获取API Key？
A: 请前往您使用的AI服务提供商官网申请API密钥。例如，OpenAI的API Key在 platform.openai.com 创建。

### Q: 应用免费吗？
A: 应用本身完全免费开源，但您使用AI模型服务时可能需要向服务商支付费用。

### Q: 支持离线使用吗？
A: 不支持。AI对话需要连接模型服务器，离线状态下无法使用对话功能。

### Q: 如何导出聊天记录？
A: 在对话列表中长按某条对话，选择「导出」即可保存为文件。

### Q: 忘记API Key怎么办？
A: 您可以在API配置页面重新输入。API Key不会被明文显示，出于安全考虑会被遮盖。

---

如有其他问题，请提交Issue或发送邮件至 support@aiai.app。
