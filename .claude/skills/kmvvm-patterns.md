---
name: kmvvm-patterns
description: Android Kotlin MVVM 框架编码模式，从 kmvvm 项目 git 历史中提取
version: 1.0.0
source: local-git-analysis
analyzed_commits: 200
---

# kmvvm 项目编码模式

## 提交约定

本项目使用**中文提交信息**，遵循以下前缀规范：

| 前缀 | 用途 | 示例 |
|------|------|------|
| `feat:` | 新功能 | `feat: 迁移到 catchpig detekt 插件` |
| `fix:` | Bug 修复 | `fix: 修复前后台回调监听消息不回调的问题` |
| `chore:` | 维护任务 | `chore: 删除 detekt.gradle` |
| `docs:` | 文档更新 | `docs: 更新文档` |
| `refactor:` | 代码重构 | （较少使用） |
| `perf:` | 性能优化 | （较少使用） |

**高频提交模式**：
- `优化代码` - 代码质量改进
- `升级xxx版本` - 依赖版本升级
- `解决编译不过的问题` - 修复编译错误
- `更新文档` - README/CHANGELOG 更新

## 代码架构

### 模块结构

```
kmvvm/
├── app/          # 演示应用 (com.catchpig.kmvvm)
├── mvvm/         # 核心框架库
├── annotation/   # 注解定义
├── compiler/     # KSP 注解处理器
├── download/     # 文件下载能力库
├── utils/        # 通用扩展与工具
└── config/       # 配置文件 (detekt 等)
```

### 依赖关系

```
app ──────> mvvm ──────> annotation
         │          └──> utils
         │
         └──> download ────> utils
```

### 核心类组织

| 模块 | 核心类 | 说明 |
|------|--------|------|
| mvvm | `BaseActivity`, `BaseVMActivity` | Activity 基类 |
| mvvm | `BaseFragment`, `BaseVMFragment` | Fragment 基类 |
| mvvm | `BaseViewModel` | ViewModel 基类 |
| mvvm | `RecyclerAdapter` | 列表适配器基类 |
| mvvm | `NetManager` | 网络管理单例 |
| mvvm | `FlowExt.kt` | Flow 生命周期扩展 |
| annotation | `@ServiceApi`, `@Title`, `@GlobalConfig` | 注解定义 |
| compiler | `*Generator` | KSP 代码生成器 |

## 常用工作流

### 1. 添加新页面/功能

1. 在 `app/` 下创建页面包（按功能分包）
2. 创建 `XxxActivity`/`XxxFragment` + `XxxViewModel`
3. 使用 `@Title`、`@StatusBar` 等注解
4. 在 Repository 中用 `flow { emit(api.xxx()) }` 封装请求
5. 更新 `CHANGELOG.md`

### 2. 修改网络层

涉及文件（通常一起修改）：
- `mvvm/src/main/java/.../network/manager/NetManager.kt`
- `mvvm/src/main/java/.../network/converter/BaseResponseBodyConverter.kt`
- `app/src/main/java/.../network/ResponseBodyConverter.kt`

### 3. 修改 Flow 扩展

涉及文件（通常一起修改）：
- `mvvm/src/main/java/.../ext/FlowExt.kt`
- `mvvm/src/main/java/.../base/activity/BaseVMActivity.kt`
- `mvvm/src/main/java/.../base/fragment/BaseVMFragment.kt`
- `mvvm/src/main/java/.../base/view/BaseView.kt`

### 4. 修改 RecyclerAdapter

涉及文件（通常一起修改）：
- `mvvm/src/main/java/.../base/adapter/RecyclerAdapter.kt`
- `mvvm/src/main/java/.../base/activity/BaseActivity.kt`
- `mvvm/src/main/java/.../base/fragment/BaseFragment.kt`

### 5. 升级依赖版本

1. 修改 `gradle/libs.versions.toml`
2. 修改相关模块的 `build.gradle.kts`
3. 更新 `CHANGE_lOG.md` 或 `version.gradle`
4. 运行 `./gradlew clean assembleDebug` 验证

## 测试约定

- 测试目录：`src/androidTest/`（仪器化测试）、`src/test/`（单元测试）
- 测试框架：Android JUnit、Espresso
- **注意**：从 git 历史看，项目测试覆盖较少，暂无严格测试要求

## 代码生成约定

### KSP 注解处理器

| 注解 | 生成类 | 说明 |
|------|--------|------|
| `@ServiceApi` | `*ServiceApiImpl` | Retrofit 服务实现 |
| `@GlobalConfig` | `*GlobalConfigImpl` | 全局配置实现 |
| `@Title` | 标题栏代码 | Activity 标题栏 |

**修改注解后必须**：`./gradlew clean assembleDebug`

## 文件命名约定

- Activity: `XxxActivity.kt`
- Fragment: `XxxFragment.kt`
- ViewModel: `XxxViewModel.kt`
- Adapter: `XxxAdapter.kt`
- ItemBinding: `ItemXxxBinding.kt`
- Repository: `XxxRepository.kt`（object 单例）
- Service: `XxxService.kt`（Retrofit 接口）

## Gradle 配置约定

- 版本管理：`gradle/libs.versions.toml` + `version.gradle`
- 插件配置：`build.gradle.kts`（项目根目录和各模块）
- 属性配置：`gradle.properties`
- KSP 配置：`ksp.incremental=false`（增量编译问题排查时）
