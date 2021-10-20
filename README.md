
## Gitee 地址:[kotlin-mvvm](https://gitee.com/catch-pig/kotlin-mvvm)
[![](https://jitpack.io/v/com.gitee.catch-pig/kotlin-mvvm.svg)](https://jitpack.io/#com.gitee.catch-pig/kotlin-mvvm)

****

## Github 地址:[kotlin-mvvm](https://github.com/catch-pig/kotlin-mvvm)
[![](https://jitpack.io/v/catch-pig/kotlin-mvvm.svg)](https://jitpack.io/#catch-pig/kotlin-mvvm)

****

## [CHANGE LOG](./CHANGE_lOG.md)

## 技术要点

### 1. RxJava+Retrofit+OkHttp实现链式http请求

### 2. 封装基类:BaseActivity、BaseVMActivity、BaseFragment、BaseVMFragment、RecycleAdapter、BaseViewModel

### 3. 封装工具扩展类：CalendarExt、ContextExt、DateExt、EditTextExt、GsonExt、RxJavaExt、StringExt、SnackbarExt

### 4. 引入LifeCycle，将ViewModel和Activity的生命周期绑定在一起

### 5. 将在Application中初始化移至到ContentProvider中,从而不用封装BaseApplication

### 6. APT(编译时注解)封装注解：OnClickFirstDrawable、OnClickFirstText、OnClickSecondDrawable、OnClickSecondText、Prefs、PrefsField、StatusBar、ObserverError、Adapter、GlobalConfig

## 最低兼容:21

## Gradle

### 1. 在Project的build.gradle中添加

```
allprojects {
     repositories {
       maven { url 'https://jitpack.io' }
     }
 }
```

### 2. 在app的build.gradle的添加

```
apply plugin: 'kotlin-kapt' // 使用 kapt 注解处理工具
```

### 3. 在app的build.gradle的android下添加

```
    buildFeatures {
        viewBinding = true
    }
```

### 4. 添加依赖
> Gitee

    implementation "com.gitee.catch-pig.kotlin-mvvm:mvvm:last_version"
    kapt "com.gitee.catch-pig.kotlin-mvvm:compiler:last_version"

> Github

    implementation "com.github.catch-pig.kotlin-mvvm:mvvm:last_version"
    kapt "com.github.catch-pig.kotlin-mvvm:compiler:last_version"

## 使用

### 1. 配置全部参数

```
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
}
```
+ 实现[IGlobalConfig](./mvvm/src/main/java/com/catchpig/mvvm/interfaces/IGlobalConfig.kt)接口,并在实现类上加上注解[GlobalConfig](./annotation/src/main/java/com/catchpig/annotation/GlobalConfig.kt)

> 使用示例:

```    
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
}
```

### 2. Activity

* 使用MVVM的继承BaseVMActivity
* 不使用MVVM的继承BaseActivity

### 3. Fragment

* 使用MVVM的继承BaseVMFragment
* 不使用MVVM的继承BaseFragment

### 4. RecycleView

+ Adapter可以继承RecycleAdapter来使用,并在类上添加注解[Adapter](./annotation/src/main/java/com/catchpig/annotation/Adapter.kt),RecycleAdapter使用了ViewBanding,只需要实现以下一个方法

> 使用示例

```
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

### 5. 注解使用

#### 5.1 [Title](./annotation/src/main/java/com/catchpig/annotation/Title.kt)-标题

|属性|类型|必须|默认|说明|
 |---|:---:|:---|:---|:---|
|value|StringRes|是|无|标题内容|
|backgroundColor|ColorRes|否|全局标题背景色|标题背景色|
|textColor|ColorRes|否|全局标题文字颜色|标题文字颜色|
|backIcon|DrawableRes|否|全局标题返回按钮图标|标题返回按钮图标|

#### 5.2 [OnClickFirstDrawable](./annotation/src/main/java/com/catchpig/annotation/OnClickFirstDrawable.kt)-标题上第一个图标按钮的点击事件

|属性|类型|必须|默认|说明|
 |---|:---:|:---|:---|:---|
|value|StringRes|是|无|按钮图片内容|

#### 5.3 [OnClickFirstText](./annotation/src/main/java/com/catchpig/annotation/OnClickFirstText.kt)-标题上第一个文字按钮的点击事件

|属性|类型|必须|默认|说明|
 |---|:---:|:---|:---|:---|
|value|StringRes|是|无|按钮文字内容|

#### 5.4 [OnClickSecondDrawable](./annotation/src/main/java/java/com/catchpig/annotation/OnClickSecondDrawable.kt)-标题上第二个图标按钮的点击事件

|属性|类型|必须|默认|说明|
 |---|:---:|:---|:---|:---|
|value|StringRes|是|无|按钮图片内容|

#### 5.5 [OnClickSecondText](./annotation/src/main/java/com/catchpig/annotation/OnClickSecondText.kt)-标题上第二个文字按钮的点击事件

|属性|类型|必须|默认|说明|
 |---|:---:|:---|:---|:---|
|value|StringRes|是|无|按钮文字内容|

#### 5.6 [StatusBar](./annotation/src/main/java/com/catchpig/annotation/StatusBar.kt)-状态栏

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|hide|boolean|否|false|隐藏状态栏|
|enabled|boolean|否|false|状态栏是否可用|
|transparent|boolean|否|false|状态栏透明|

#### 5.7 [Prefs](./annotation/src/main/java/com/catchpig/annotation/Prefs.kt)-SharedPreferences注解生成器

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|String|否|""|别名|
|mode|[PrefsMode](./annotation/src/main/java/com/catchpig/annotation/enums/PrefsMode.kt)|否|PrefsMode.MODE_PRIVATE|模式,对应PreferencesMode|

#### 5.8 [PrefsField](./annotation/src/main/java/com/catchpig/annotation/PrefsField.kt)-SharedPreferences字段注解

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|String|否|""|字段别名,如果为空则取修饰字段的参数名称|

#### 5.9 [ObserverError](./annotation/src/main/java/com/catchpig/annotation/ObserverError.kt)-ViewModel中的RxJava的onError方法统一处理

#### 5.10 [Adapter](./annotation/src/main/java/com/catchpig/annotation/Adapter.kt)-RecyclerAdapter的继承类注解，加上此注解之后可以自动找到对应的layout资源

#### 5.11 [GlobalConfig](./annotation/src/main/java/com/catchpig/annotation/GlobalConfig.kt)-全局参数配置

### 6. 刷新分页

#### 使用RefreshLayoutWrapper+RecyclerAdapter控件实现刷新功能

+ ***RefreshLayoutWrapper继承于[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout),具体使用请看SmartRefreshLayout官方文档,默认每页数据量为16,如果想修改每页数据量,可使用如下方法更改:***

  ```
  RefreshLayoutWrapper.pageSize = 16
  ```
+ ***RefreshLayoutWrapper实现了[IPageControl](./mvvm/src/main/java/com/catchpig/mvvm/widget/refresh/IPageControl.kt)
  ,可以通过调用接口内的方法类获取刷新控件的状态和更改状态***

  ```
  //获取刷新的状态
  iPageControl.getRefreshStatus()
  ```
+ ***RecyclerAdapter在实例化的时候传入IPageControl, 获取数据成功之后,只需要调用autoUpdateList(list)方法,
  可以自动RefreshLayoutWrapper页码和刷新状态变化***

+ ***数据更新失败可以调用RecyclerAdapter.updateFailed()***

+ ***获取每页的数据量和下一页的页码,可以调用一下方法***

  ```
  //每页的数据量
  RecyclerAdapter.pageSize = 16
  //下一页的页码
  RecyclerAdapter.nextPageIndex = 1
  ```

### 7. 文件下载器([DownloadManager](./mvvm/src/main/java/com/catchpig/mvvm/manager/DownloadManager.kt)))

+ 单文件下载方法download([DownloadCallback](./mvvm/src/main/java/com/catchpig/mvvm/listener/DownloadCallback.kt))
  ```
  DownloadManager.download(downloadUrl, {
          
      }, { readLength, countLength ->
          progressLiveData.value = (readLength * 100 / countLength).toInt()
      })
  ```
    * DownloadCallback

        ```
        interface DownloadCallback {
            /**
             * 开始下载
             */
            fun onStart()
        
            /**
             * 下载成功
             * @param path 本地保存的地址
             */
            fun onSuccess(path:String)
        
            /**
             * 下载完成
             */
            fun onComplete()
        
            /**
             * 下载进度
             * @param readLength 读取的进度
             * @param countLength 总进度
             */
            fun onProgress(readLength:Long,countLength:Long)
        
            /**
             * 下载错误
             * @param t 错误信息
             */
            fun onError(t:Throwable)
        }
        ```
+ 多文件下载方法multiDownload([MultiDownloadCallback](./mvvm/src/main/java/com/catchpig/mvvm/listener/MultiDownloadCallback.kt))
  ```
  DownloadManager.multiDownload(downloadUrls, {
          
      })
  ```
    * MultiDownloadCallback
        ```
        interface MultiDownloadCallback {
        /**
        * 开始下载
        */
        fun onStart()
    
        /**
        * 下载成功
        * @param paths 本地保存的地址集
        */
        fun onSuccess(paths:MutableList<String>)
    
        /**
        * 下载完成
        */
        fun onComplete()
    
        /**
        * 下载错误
        * @param t 错误信息
        */
        fun onError(t:Throwable)
        }
        ```

## 第三方库

### [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)-刷新控件

### [Immersionbar](https://github.com/gyf-dev/ImmersionBar)-状态栏

### [RxJava3](https://github.com/ReactiveX/RxJava)

### [RxAndroid](https://github.com/ReactiveX/RxAndroid)

### [OkHttp](https://github.com/square/okhttp)

### [Retrofit](https://github.com/square/retrofit)

### [Gson](https://github.com/google/gson)

### [AndroidUtilKTX](https://github.com/lulululbj/AndroidUtilCodeKTX)-工具类

### [LoadingView](https://github.com/catch-pig/LoadingView)-Loading动画

## 其他

### 欢迎大家在issue上提出问题,我这边会不定时的看issue,解决问题