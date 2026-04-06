# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

kmvvm 是一个 Android Kotlin MVVM 架构框架，最低兼容 API 21。核心模块：

- **app**: 演示应用 (`com.catchpig.kmvvm`)
- **mvvm**: 核心框架库（BaseActivity/BaseVMActivity、BaseViewModel、Flow 扩展、网络封装）
- **annotation**: 注解定义（Title、ServiceApi、GlobalConfig、FlowError、Prefs 等）
- **compiler**: KSP 注解处理器
- **download**: 文件下载能力库
- **utils**: 通用扩展与工具

## 构建命令

```bash
# 编译 Debug 版本
./gradlew assembleDebug

# 编译 Release 版本
./gradlew assembleRelease

# 清理并重新编译（修改注解或 KSP 配置后必须用这个）
./gradlew clean assembleDebug

# 运行单元测试
./gradlew test

# 查看依赖树
./gradlew :app:dependencies
```

## 架构约定

### ViewModel 规范

- 继承 `com.catchpig.mvvm.base.viewmodel.BaseViewModel`
- 暴露数据用 `Flow<T>`，通过 `lifecycle*` 扩展收集
- Loading 状态通过 `BaseViewModel` 的 LiveData（`loadingDialogLiveData`、`loadingViewLiveData`）由 Activity/Fragment 统一处理，不直接持有 View/Context

```kotlin
fun queryBanners(): Flow<MutableList<Banner>> = repository.getBanners()
```

### Activity / Fragment 规范

- 有 ViewModel：`BaseVMActivity<VB, VM>` 或 `BaseVMFragment<VB, VM>`（泛型顺序：ViewBinding、ViewModel）
- 无 ViewModel：`BaseActivity<VB>` 或 `BaseFragment<VB>`
- 必须实现：`initParam()`、`initView()`、有数据流时在 `initFlow()` 中调用 `onFailedReload { ... }`
- 用 `viewModel.xxx().lifecycle*(this) { }` 收集 Flow，不在主线程阻塞或 runBlocking

### Flow 与生命周期扩展

| 扩展 | 用途 |
|------|------|
| `lifecycleLoadingDialog` | 带 Loading 弹窗 |
| `lifecycleLoadingView` | 带页面内 LoadingView，`showFailedView` 控制失败页 |
| `lifecycleRefresh` | 列表刷新/加载更多 |
| `lifecycle` | 普通 Flow 收集 |

失败重试：在 `initFlow()` 里 `onFailedReload { 再次调用请求 }`，且对应 `lifecycle*` 传 `showFailedView = true`

### Repository 规范

- 用 `object` 或单例组织，内部用 `NetManager.getService(...)` 获取 API
- 方法返回 `Flow<T>`，用 `flow { emit(api.xxx()) }` 封装
- 不处理 Loading/错误 UI，由 ViewModel + View 层处理

### Adapter 规范

- 继承 `RecyclerAdapter<T, ItemXxxBinding>`
- 实现 `viewBinding(parent)` 与 `bindViewHolder(holder, m, position)`
- 在 `bindViewHolder` 里用 `holder.viewBanding { }` 更新 item

## 网络层

### Retrofit 接口

```kotlin
@ServiceApi(
    baseUrl = "https://xxx/",
    responseConverter = ResponseBodyConverter::class,
    interceptors = [RequestInterceptor::class],
    debug = true
)
interface WanAndroidService {
    @GET("banner/json")
    suspend fun queryBanner(): MutableList<Banner>
}
```

- 接口方法用 `suspend` 返回最终数据类型
- 通过 `NetManager.getService(XxxService::class.java)` 获取实例
- 若后端为 `{ code, errorMsg, data }` 结构：继承 `BaseResponseBodyConverter`

## KSP 注解

| 注解 | 作用 |
|------|------|
| `@Title(resId)` | Activity/Fragment 标题 |
| `@StatusBar` | 状态栏显隐/样式 |
| `@OnClickFirstDrawable / @OnClickFirstText` | 标题栏右侧第一图标/文字点击 |
| `@OnClickSecondDrawable / @OnClickSecondText` | 标题栏右侧第二图标/文字点击 |
| `@ServiceApi` | Retrofit 接口配置 |
| `@GlobalConfig` | 全局配置（标在实现 `IGlobalConfig` 的类上） |
| `@FlowError` | Flow 错误处理策略 |

**修改注解或 `IGlobalConfig` 实现后必须 Rebuild（clean + assemble）**，否则生成的代码不会更新。

## 全局配置

- 实现 `IGlobalConfig` 的类放在 `config/` 包，加 `@GlobalConfig`
- 配置内容：标题栏高度/背景/颜色、Loading 颜色、空页/失败页 ViewBinding、分页 pageSize/startPageIndex 等

## 包名约定

- 业务按功能/页面分包：`child/`、`article/`、`index/`、`main/` 等
- 同一页面：`XxxActivity`、`XxxViewModel`、`XxxFragment` 放同一包
- 网络：API 在 `network/api/`，转换器/拦截器在 `network/`，实体在 `entity/`
- 全局配置：`config/`

## 技术规范

- **ViewBinding 是强制要求**，禁止在业务布局中写 `tools:viewBindingIgnore="true"`
- 提示用 `snackBar(...)`，不用 Toast
- 混淆需保留注解与生成类（详见 README 混淆小节）
- Gradle 配置在 `gradle/libs.versions.toml`，通过 `libs.xxx` 引用
- KSP 增量编译需在 `gradle.properties` 中设置 `ksp.incremental=false`

## 模块依赖关系

```
app ──────> mvvm ──────> annotation
         │          └──> utils
         │
         └──> download ────> utils
```
