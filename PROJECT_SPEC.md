# 爱Ai (AiAi) 项目共享规范

## 项目基本信息
- 应用名称：爱Ai
- 包名基础：com.aiai
- 最低SDK：24
- 目标SDK：34
- 构建工具：Gradle Kotlin DSL
- 语言：Kotlin + Java 混合（以Kotlin为主）

## 模块结构与依赖关系

```
:app (com.aiai.app)
  ├── :feature-chat (com.aiai.chat)
  ├── :feature-settings (com.aiai.settings)
  ├── :data (com.aiai.data)
  ├── :network (com.aiai.network)
  ├── :core (com.aiai.core)
  └── :common (com.aiai.common)

:feature-chat
  ├── :data
  ├── :network
  ├── :core
  └── :common

:feature-settings
  ├── :data
  ├── :core
  └── :common

:data
  ├── :network
  ├── :core
  └── :common

:network
  ├── :core
  └── :common

:core
  └── :common

:common (无内部依赖)
```

## 各模块包名
- app: com.aiai.app
- core: com.aiai.core
- common: com.aiai.common
- network: com.aiai.network
- data: com.aiai.data
- feature-chat: com.aiai.chat
- feature-settings: com.aiai.settings

## 共享常量定义（core模块中定义，其他模块引用）
- API基础URL配置键："api_base_url"
- API密钥配置键："api_key"
- 模型名称配置键："model_name"
- 数据库名："aiai_database.db"
- MMKV ID："aiai_mmkv"
- SharedPreferences名："aiai_prefs"

## 数据模型共享约定
- 所有Entity放在 data模块 com.aiai.data.entity 包
- 所有DAO放在 data模块 com.aiai.data.dao 包
- 所有Repository接口放在 data模块 com.aiai.data.repository 包
- 所有Repository实现放在 data模块 com.aiai.data.repository.impl 包
- 所有API服务接口放在 network模块 com.aiai.network.api 包
- 所有请求模型放在 network模块 com.aiai.network.model.request 包
- 所有响应模型放在 network模块 com.aiai.network.model.response 包

## UI共享约定
- 所有Activity命名：XxxActivity
- 所有Fragment命名：XxxFragment
- 所有ViewModel命名：XxxViewModel
- 所有Adapter命名：XxxAdapter
- 所有自定义View命名：XxxView
- 布局文件命名：activity_xxx.xml / fragment_xxx.xml / item_xxx.xml / view_xxx.xml / dialog_xxx.xml
- 菜单文件命名：menu_xxx.xml
- drawable命名：bg_xxx / ic_xxx / selector_xxx / shape_xxx

## 资源共享约定
- 字符串资源统一在app模块的strings.xml中定义，其他模块通过引用使用
- 颜色资源统一在app模块的colors.xml中定义
- 尺寸资源统一在app模块的dimens.xml中定义
- 主题样式统一在app模块的themes.xml中定义

## 代码规范
- 使用Kotlin Coroutines + Flow处理异步
- 使用Hilt进行依赖注入
- 使用StateFlow/LiveData进行UI状态管理
- 所有网络请求在IO线程执行
- 所有UI操作在主线程执行
- 类、方法、字段必须有KDoc注释（公开API）
- 每个文件顶部必须有版权声明注释

## 代码量目标分配
- core模块：约8000行
- common模块：约17000行
- network模块：约12000行
- data模块：约13000行
- feature-chat模块：约25000行
- feature-settings模块：约20000行
- app模块+资源+测试+构建：约25000行
- 总计：≥120000行
