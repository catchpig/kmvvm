## Gitee 地址:[kmvvm](https://gitee.com/catchpig/kmvvm)

[![](https://jitpack.io/v/com.gitee.catchpig/kmvvm.svg)](https://jitpack.io/#com.gitee.catchpig/kmvvm)

****

## Github 地址:[kmvvm](https://github.com/catchpig/kmvvm)

[![](https://jitpack.io/v/catchpig/kmvvm.svg)](https://jitpack.io/#catchpig/kmvvm)

****

## [CHANGE LOG](./CHANGE_lOG.md)

## 技术要点

+ ### 支持Flow+Retrofit+OkHttp实现链式http请求

+ ### 支持Rxjava+Retrofit+OkHttp实现链式http请求

+ ### 封装基类:BaseActivity、BaseVMActivity、BaseFragment、BaseVMFragment、RecycleAdapter、BaseViewModel

+ ### 引入LifeCycle，将ViewModel和Activity的生命周期绑定在一起

+ ### 将在Application中初始化移至到ContentProvider中,从而不用封装BaseApplication

+ ### APT(编译时注解)封装注解：Title、OnClickFirstDrawable、OnClickFirstText、OnClickSecondDrawable、OnClickSecondText、Prefs、PrefsField、StatusBar、FlowError、Adapter、GlobalConfig、ServiceApi

+ ### 封装工具扩展类：CalendarExt、ContextExt、DateExt、EditTextExt、GsonExt、RxJavaExt、StringExt、SnackbarExt

## 最低兼容:21

## Gradle

### 1. 在Project的build.gradle中添加

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

### 2. 在app的build.gradle的添加

```groovy
apply plugin: 'kotlin-kapt' // 使用 kapt 注解处理工具
apply plugin: 'kotlinx-serialization' //序列化
```

### 3. 在app的build.gradle的android下添加

```groovy
buildFeatures {
    viewBinding = true
}
```

### 4. 添加依赖

> Gitee

```groovy
implementation "com.gitee.catchpig.kmvvm:mvvm:last_version"
kapt "com.gitee.catchpig.kmvvm:compiler:last_version"
```

> Github

```groovy
implementation "com.github.catchpig.kmvvm:mvvm:last_version"
kapt "com.github.catchpig.kmvvm:compiler:last_version"
```

## 使用

### 1. 配置全部参数

```kotlin
interface IGlobalConfig {
    /**
     * 标题栏高度
     * @return Int
     */
    @DimenRes
    fun getTitleHeight(): Int

    /**
     * 标题栏的返回按钮资源
     * @return Int
     */
    @DrawableRes
    fun getTitleBackIcon(): Int

    /**
     * 标题栏背景颜色
     * @return Int
     */
    @ColorRes
    fun getTitleBackground(): Int

    /**
     * 标题栏文本颜色
     * @return Int
     */
    @ColorRes
    fun getTitleTextColor(): Int

    /**
     * 标题栏下方是否需要横线
     * @return Boolean
     */
    fun isShowTitleLine(): Boolean

    /**
     * 标题栏下方横线颜色
     * @return Int
     */
    @ColorRes
    fun getTitleLineColor(): Int

    /**
     * loading的颜色
     * @return Int
     */
    @ColorRes
    fun getLoadingColor(): Int

    /**
     * loading的背景颜色
     * @return Int
     */
    @ColorRes
    fun getLoadingBackground(): Int

    /**
     * RecyclerView的空页面ViewBinding
     * @param parent ViewGroup
     * @return ViewBinding
     */
    fun getRecyclerEmptyBanding(parent: ViewGroup): ViewBinding

    /**
     * 刷新每页加载个数
     * @return Int
     */
    fun getPageSize(): Int

    /**
     * 刷新起始页的index(有些后台设置的0,有些后台设置1)
     */
    fun getStartPageIndex(): Int
}
```

+ 实现[IGlobalConfig](./annotation/src/main/java/com/catchpig/annotation/IGlobalConfig.kt)
  接口,并在实现类上加上注解[GlobalConfig](./annotation/src/main/java/com/catchpig/annotation/GlobalConfig.kt)

> 使用示例:

```kotlin
@GlobalConfig
class MvvmGlobalConfig : IGlobalConfig {
    override fun getTitleHeight(): Int {
        return R.dimen.title_bar_height
    }

    override fun getTitleBackIcon(): Int {
        return R.drawable.back_black
    }

    override fun getTitleBackground(): Int {
        return R.color.colorPrimary
    }

    override fun getTitleTextColor(): Int {
        return R.color.white
    }

    override fun isShowTitleLine(): Boolean {
        return true
    }

    override fun getTitleLineColor(): Int {
        return R.color.color_black
    }

    override fun getLoadingColor(): Int {
        return R.color.color_black
    }

    override fun getLoadingBackground(): Int {
        return R.color.white
    }

    override fun getRecyclerEmptyBanding(parent: ViewGroup): ViewBinding {
        return LayoutEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getPageSize(): Int {
        return 16
    }

    override fun getStartPageIndex(): Int {
        return 1
    }
}
```

### 2. Activity

* 使用MVVM的继承BaseVMActivity
* 不使用MVVM的继承BaseActivity'

#### 2.1 标题注解使用

> 使用示例
>
> **Title其他注解参数,请看下方注解详情**

```kotlin
//设置标题的文字
@Title(R.string.child_title)
class ChildActivity : BaseVMActivity<ActivityChildBinding, ChildViewModel>() 
```

> **如果标题栏文字要根据接口显示不同的文字,也有接口设置**

```kotlin
class ChildActivity : BaseVMActivity<ActivityChildBinding, ChildViewModel>() {
    @OnClickFirstDrawable(R.drawable.more)
    fun clickFirstDrawable(v: View) {
        updateTitle("更改标题")
    }
}
```

#### 2.2 状态栏注解使用

> 使用示例
>
> **StatusBar其他注解参数,请看下方注解详情**

```kotlin
//弃用注解
@StatusBar(hide = true)
class FullScreenActivity : BaseActivity<ActivityFullScreenBinding>()
```

#### 2.3 标题右侧文字或图标按钮注解使用

> 使用示例
>
> **注解修饰的方法只能可以带View参数,也可以不带View参数,看自身的需求**

```kotlin
@Title(R.string.child_title)
class ChildActivity : BaseVMActivity<ActivityChildBinding, ChildViewModel>() {
    @OnClickFirstDrawable(R.drawable.more)
    fun clickFirstDrawable(v: View) {
        SnackbarManager.show(bodyBinding.root, "第一个图标按钮点击生效")
        updateTitle("nihao")
    }

    @OnClickFirstText(R.string.more)
    fun clickFirstText() {
        SnackbarManager.show(bodyBinding.root, "第一个文字按钮点击生效")
        updateTitle("12354")
    }

    @OnClickSecondDrawable(R.drawable.more)
    fun clickSecondDrawable(v: View) {
        SnackbarManager.show(bodyBinding.root, "第二个图标按钮点击生效")
        updateTitle("nihao")
    }

    @OnClickSecondText(R.string.more)
    fun clickSecondText() {
        SnackbarManager.show(bodyBinding.root, "第二个文字按钮点击生效")
        updateTitle("12354")
    }
}
```

#### 2.4 提示框

+ Android 11 之后,Toast已经不支持自定义Toast,原生的Toast是很难看的
+ 本框架使用SnackBar做提示框

> 使用示例

```kotlin
@OnClickSecondDrawable(R.drawable.more)
fun clickSecondDrawable(v: View) {
    snackBar("第二个图标按钮点击生效")
}
```

<img src="./images/activity_snackbar.jpg" style="zoom:30%;" />

### 3. Fragment

* 使用MVVM的继承BaseVMFragment
* 不使用MVVM的继承BaseFragment

#### 3.1 提示框

+ Android 11 之后,Toast已经不支持自定义Toast,原生的Toast是很难看的
+ 本框架使用SnackBar做提示框

> 使用示例

```kotlin
snackbar.setOnClickListener {
    snackBar("提示框")
}
```

<img src="./images/fragment_snackbar.jpg" style="zoom:30%;" />

### 4. RecycleView

+ Adapter可以继承RecycleAdapter来使用,并在类上添加注解[Adapter](./annotation/src/main/java/com/catchpig/annotation/Adapter.kt)
  ,RecycleAdapter使用了ViewBanding,只需要实现以下一个方法

> 使用示例

```kotlin
@Adapter
class UserAdapter(iPageControl: IPageControl) :
    RecyclerAdapter<User, ItemUserBinding>(iPageControl) {

    override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
        holder.viewBanding {
            name.text = m.name
        }
    }
}
```

### 5.刷新分页控件([RefreshRecyclerView](./mvvm/src/main/java/com/catchpig/mvvm/widget/refresh/RefreshRecyclerView.kt))

+ RefreshRecyclerView集成了RefreshLayoutWrapper+RecyclerView
+ 不用关心分页的逻辑,分页的刷新逻辑实现都在[RefreshLayoutWrapper](./mvvm/src/main/java/com/catchpig/mvvm/widget/refresh/RefreshLayoutWrapper.kt)
+ 只需要设置LayoutManager和RecyclerAdapter,提供了setLayoutManager和setAdapter方法
+ 在获取到数据的时候调用updateData方法
+ 获取数据失败的时候调用updateError方法
+ 如果使用了lifecycleFlowRefresh方法,updateData方法和updateError方法都不用关心
+ 提供自定义属性recycler_background(设置RecyclerView的背景色)

```
<declare-styleable name="RefreshRecyclerView">
    <attr name="recycler_background" format="color" />
</declare-styleable>
```

> 使用示例

```xml

<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto" android:layout_width="match_parent"
    android:layout_height="match_parent" android:orientation="vertical">

    <TextView android:layout_width="match_parent" android:layout_height="50dp"
        android:background="@color/colorPrimary" android:gravity="center" android:text="文章"
        android:textColor="@color/color_white" />

    <com.catchpig.mvvm.widget.refresh.RefreshRecyclerView android:id="@+id/refresh"
        android:layout_width="match_parent" android:layout_height="match_parent"
        app:recycler_background="#445467">

    </com.catchpig.mvvm.widget.refresh.RefreshRecyclerView>
</LinearLayout>
```

```kotlin
bodyBinding.refresh.run {
    setOnRefreshLoadMoreListener { nextPageIndex ->
        lifecycleFlowRefresh(viewModel.queryArticles(nextPageIndex), this)
    }
}
```

### 6. 网络请求

#### 6.1只需要是接口类上加上注解[ServiceApi](./annotation/src/main/java/com/catchpig/annotation/ServiceApi.kt),并使用NetManager.getService()获取对应的接口类

> 使用示例

```kotlin
@ServiceApi(
    baseUrl = "https://www.wanandroid.com/",
    responseConverter = ResponseBodyConverter::class,
    interceptors = [RequestInterceptor::class],
    debugInterceptors = [OkHttpProfilerInterceptor::class]
)
interface WanAndroidService {
    @GET("banner/json")
    suspend fun banner(): List<Banner>
}
```

```kotlin
object WanAndroidRepository {
    private val wanAndroidService = NetManager.getService(WanAndroidService::class.java)
    fun getBanners(): Flow<MutableList<Banner>> {
        //这里如果用flowOf的话,方法上面必须加上suspend关键字
        return flow {
            emit(wanAndroidService.queryBanner())
        }
    }
}
```

```kotlin
class IndexViewModel : BaseViewModel() {
    fun queryBanners(): Flow<MutableList<Banner>> {
        return WanAndroidRepository.getBanners()
    }
}
```

```kotlin
//Activity或者Fragment
lifecycleFlowLoadingView(viewModel.queryBanners()) {
    val images = mutableListOf<String>()
    this.forEach {
        images.add(it.imagePath)
    }
    bodyBinding.banner.run {
        setImages(images)
        start()
    }
}
```

#### 6.2 Response转换器

##### 6.2.1 一般Response发返回结果会是如下

```json
{
	code:"SUCCESS",
	errorMsg:"成功",
	data:...
}
```

##### 6.2.2 在code返回SUCEESSD的时候, 我们在Retrofit的Api接口里面只想拿到data的数据做返回,我们想在Converter里面处理掉code返回错误码的逻辑,就可以继承[BaseResponseBodyConverter](./mvvm/src/main/java/com/catchpig/network/converter/BaseResponseBodyConverter.kt),内部已经实现了将response转化为data的逻辑

> 代码示例

```kotlin
class ResponseBodyConverter :
    BaseResponseBodyConverter() {
    override fun getResultClass(): KClass<out BaseResponseData<JsonElement>> {
        return Result::class
    }

    override fun handlerErrorCode(errorCode: String, msg: String): Exception {
        return NullPointerException()
    }
}
```

##### 6.2.3 再将实现了BaseResponseBodyConverter的类加到ServiceApi注解的responseConverter属性上

##### 6.2.4 如果想直接拿response的结果作为网络请求的返回值,可以直接将[SerializationResponseBodyConverter](./mvvm/src/main/java/com/catchpig/network/converter/SerializationResponseBodyConverter.kt)加到ServiceApi注解的responseConverter属性上

#### 6.3 Activity和Fragment封装了网络请求方法(带lifecycleScope)

+ lifecycleFlowRefresh(flow: Flow<MutableList<T>>,refresh: RefreshRecyclerView)
  -刷新+RecycleView的网络请求封装
+ lifecycleFlow(flow: Flow<T>, callback: T.() -> Unit)-不带loading的网络请求封装
+ lifecycleFlowLoadingView(flow: Flow<T>, callback: T.() -> Unit)-带loadingView的网络请求封装
+ lifecycleFlowLoadingDialog(flow: Flow<T>, callback: T.() -> Unit)-带loadingDialog的网络请求封装

### 7. [注解使用](./REMARD_ANNOTATION.md)

### 8. [文件下载器](./REMARD_DOWNLOAD_MANAGER.md)

### 9. [工具库](./utils/README.md)

### 混淆

```properties
-keep class com.catchpig.annotation.enums.**

-keep class com.google.android.material.snackbar.Snackbar {*;}

-keep @com.catchpig.annotation.Adapter class * {*;}
-keep @com.catchpig.annotation.ServiceApi class * {*;}

-keep public class **.databinding.*Binding {*;}

-keep class **.*_Compiler {*;}

-keep class com.gyf.immersionbar.* {*;}
-dontwarn com.gyf.immersionbar.**

#序列化混淆
-if @kotlinx.serialization.Serializable class **
-keepclassmembers class <1> {
    static <1>$Companion Companion;
}

# Keep `serializer()` on companion objects (both default and named) of serializable classes.
-if @kotlinx.serialization.Serializable class ** {
    static **$* *;
}
-keepclassmembers class <1>$<3> {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep `INSTANCE.serializer()` of serializable objects.
-if @kotlinx.serialization.Serializable class ** {
    public static ** INSTANCE;
}
-keepclassmembers class <1> {
    public static <1> INSTANCE;
    kotlinx.serialization.KSerializer serializer(...);
}

# @Serializable and @Polymorphic are used at runtime for polymorphic serialization.
-keepattributes RuntimeVisibleAnnotations,AnnotationDefault
```

## 第三方库

### [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)-刷新控件

### [Immersionbar](https://github.com/gyf-dev/ImmersionBar)-状态栏

### [RxJava3](https://github.com/ReactiveX/RxJava)

### [RxAndroid](https://github.com/ReactiveX/RxAndroid)

### [OkHttp](https://github.com/square/okhttp)

### [Retrofit](https://github.com/square/retrofit)

### [Gson](https://github.com/google/gson)

### [kotlinpoet](https://github.com/square/kotlinpoet) - kt代码生成工具

### [AndroidUtilKTX](https://github.com/lulululbj/AndroidUtilCodeKTX) - 工具类

### [LoadingView](https://github.com/catch-pig/LoadingView) - Loading动画

### [coroutines](https://github.com/Kotlin/kotlinx.coroutines) - 协程

### [serialization](https://github.com/Kotlin/kotlinx.serialization)-序列化

## 其他

### 欢迎大家在issue上提出问题,我这边会不定时的看issue,解决问题