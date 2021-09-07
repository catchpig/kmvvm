# [kotlin-mvp](https://github.com/catch-pig/kotlin-mvp)
[![](https://jitpack.io/v/catch-pig/kotlin-mvp.svg)](https://jitpack.io/#catch-pig/kotlin-mvp)

## 技术要点

### 1. RxJava+Retrofit+OkHttp实现链式http请求

### 2. 封装基类:BaseActivity、BasePresenterActivity、BaseFragment、BasePresenterFragment、RecycleAdapter、BasePresenter

### 3. 封装工具扩展类：CalendarExt、ContextExt、DateExt、EditTextExt、GsonExt、RxJavaExt、StringExt

### 4. 引入LifeCycle，将Presenter和Activity的生命周期绑定在一起

### 5. 将在Application中初始化移至到ContentProvider中,从而不用封装BaseApplication

### 6. AOP(面向切面)封装注解:TimeLog、ClickGap、MethodLog

### 7. APT(编译时注解)封装注解：OnClickFirstDrawable、OnClickFirstText、OnClickSecondDrawable、OnClickSecondText、Prefs、PrefsField、StatusBar

### 8. Koin对类的生命周期做一个管理

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
    
### 2. [AspectJX](https://github.com/HujiangTechnology/gradle_plugin_android_aspectjx)的使用请参考官方文档
    
```
dependencies {
    classpath 'com.github.franticn:gradle_plugin_android_aspectjx:2.0.10'
}
```
### 3. 在app的build.gradle的添加
```
apply plugin: 'kotlin-kapt' // 使用 kapt 注解处理工具

apply plugin: 'android-aspectjx'

aspectjx {
    exclude 'versions.9.module-info.class'
    exclude 'module-info.class' 
}
```
### 4. 添加依赖
```
implementation "com.github.catch-pig.kotlin-mvp:mvp:last_version"
kapt "com.github.catch-pig.kotlin-mvp:compiler:last_version"
```
## 使用

### 1. 在需要使用状态栏、标题栏、加载动画的主题中配置全局参数:
    
   |属性|类型|必须|默认|说明|
   |---|:---:|:---|:---|:---|
   |title_bar_back_icon|DrawableRes|是|无|标题栏的返回图标|
   |title_bar_background|ColorRes|是|无|标题栏的背景色|
   |title_bar_text_color|ColorRes|是|无|标题栏的文字颜色|
   |title_bar_show_line|boolean|否|false|标题栏的下方的线条是否显示|
   |loading_view_color|ColorRes|是|无|loading动画颜色|
   |loading_view_background|ColorRes|是|无|loading动画背景色|
   |recycle_view_empty_layout|LayoutRes|否|[emptyLayout](./mvp/src/main/res/layout/view_load_empty.xml)|列表空页面|
    
 > 使用示例:
    
    ```
    <style name="AppThemeBarStyle" parent="Theme.AppCompat.Light.NoActionBar">
        <!--全局标题栏和状态栏配置-->
        <item name="title_bar_background">@color/colorPrimary</item>
        <item name="title_bar_back_icon">@drawable/back</item>
        <item name="title_bar_text_color">@color/white</item>
        <item name="title_bar_show_line">false</item>
        <item name="loading_view_color">@color/colorAccent</item>
        <item name="loading_view_background">@color/white</item>
        <!--全局标题栏和状态栏配置-->
    </style>
    ```
### 2. Activity
  * 使用MVP的继承BasePresenterActivity
  * 不使用MVP的继承BaseActivity
### 3. Fragment
  * 使用MVP的继承BasePresenterFragment
  * 不使用MVP的继承BaseFragment
### 4. 如果使用RecycleView的时候,Adapter可以继承RecycleAdapter来使用
  > 在app的build.gradle的android下添加如下代码:
   ```
   //启用实验性功能
   androidExtensions {
       experimental = true
   }
   ```
    
  > 只需要实现以下两个方法
  
  ```
  class UserAdapter(iPageControl: IPageControl):RecyclerAdapter<User>(iPageControl) {
      override fun layoutId(): Int {
          return R.layout.item_user
      }
  
      override fun bindViewHolder(holder: CommonViewHolder, m: User, position: Int) {
          //使用的experimental之后,可以直接holder.控件ID,不需要holder.itemView.控件ID
          holder.name.text = m.name
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

#### 5.7 [TimeLog](./annotation/src/main/java/com/catchpig/annotation/TimeLog.kt)-打印方法和构造方法执行的时间

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|[LEVEL](./annotation/src/main/java/com/catchpig/annotation/TimeLog.kt)|否|[LEVEL.D](./annotation/src/main/java/com/catchpig/annotation/TimeLog.kt)|日志等级|

#### 5.8 [ClickGap](./annotation/src/main/java/com/catchpig/annotation/ClickGap.kt)-重复点击延时

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|Long|否|800毫秒|重复点击间隔|

#### 5.9 [Prefs](./annotation/src/main/java/com/catchpig/annotation/Prefs.kt)-SharedPreferences注解生成器

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|String|否|""|别名|
|mode|[PrefsMode](./annotation/src/main/java/com/catchpig/annotation/enums/PrefsMode.kt)|否|PrefsMode.MODE_PRIVATE|模式,对应PreferencesMode|

#### 5.10 [PrefsField](./annotation/src/main/java/com/catchpig/annotation/PrefsField.kt)-SharedPreferences字段注解

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|String|否|""|字段别名,如果为空则取修饰字段的参数名称|

#### 5.11 [MethodLog](./annotation/src/main/java/com/catchpig/annotation/MethodLog.kt)-打印方法和构造方法以及参数的值

|属性|类型|必须|默认|说明|
|---|:---:|:---|:---|:---|
|value|[LEVEL](./annotation/src/main/java/com/catchpig/annotation/MethodLog.kt)|否|[LEVEL.D](./annotation/src/main/java/com/catchpig/annotation/MethodLog.kt)|日志等级|


### 6. 刷新分页
    
#### 使用RefreshLayoutWrapper+RecyclerAdapter控件实现刷新功能
        
  + ***RefreshLayoutWrapper继承于[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout),具体使用请看SmartRefreshLayout官方文档,默认每页数据量为16,如果想修改每页数据量,可使用如下方法更改:***
        
    ```
    RefreshLayoutWrapper.pageSize = 16
    ```
  + ***RefreshLayoutWrapper实现了[IPageControl](./mvp/src/main/java/com/catchpig/mvp/widget/refresh/IPageControl.kt),可以通过调用接口内的方法类获取刷新控件的状态和更改状态***
    
    ```
    //获取刷新的状态
    iPageControl.getRefreshStatus()
    ```
  + ***RecyclerAdapter在实例化的时候传入IPageControl,
        获取数据成功之后,只需要调用autoUpdateList(list)方法,
        可以自动RefreshLayoutWrapper页码和刷新状态变化***
        
  + ***数据更新失败可以调用RecyclerAdapter.updateFailed()***
   
  + ***获取每页的数据量和下一页的页码,可以调用一下方法***
    
    ```
    //每页的数据量
    RecyclerAdapter.pageSize = 16
    //下一页的页码
    RecyclerAdapter.nextPageIndex = 1
    ```
     
### 7. 文件下载器([DownloadManager](./mvp/src/main/java/com/catchpig/mvp/manager/DownloadManager.kt)))
 + 在application的onCreate中startKoin,加入[downloadModule](./mvp/src/main/java/com/catchpig/mvp/di/AppModule.kt)
    
    ```
     startKoin {
     modules(downloadModule)
     }
    ```
        
   * downloadModule
   
        ```
        /**
         * 下载相关类的初始化管理
         */
        const val NAMED_DOWNLOAD = "download"
        val downloadModule = module {
            single(named(NAMED_DOWNLOAD)) { (downloadProgressListener: DownloadProgressListener)->
                DownloadInterceptor(downloadProgressListener)
            } bind Interceptor::class
        
            single(named(NAMED_DOWNLOAD)) { (downloadProgressListener: DownloadProgressListener,         timeout:Long)->
                OkHttpClient
                        .Builder()
                        .connectTimeout(timeout, TimeUnit.SECONDS)
                        .addInterceptor(get<Interceptor>())
                        .addInterceptor(get<Interceptor>(named(NAMED_DOWNLOAD)){         parametersOf(downloadProgressListener)})
                        .build()
            }
        
            single(named(NAMED_DOWNLOAD)) { (baseUrl:String,timeout:Long,downloadProgressListener:         DownloadProgressListener)->
                Retrofit
                        .Builder()
                        .baseUrl(baseUrl)
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .client(get(named(NAMED_DOWNLOAD)){ parametersOf(downloadProgressListener,timeout)})
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(DownloadService::class.java)
            }
        
            single {
                DownloadManager()
            }
        }
        ```
   
 + 调用下载方法download([DownloadInfo](./mvp/src/main/java/com/catchpig/mvp/bean/DownloadInfo.kt),[DownloadCallback](./mvp/src/main/java/com/catchpig/mvp/listener/DownloadCallback.kt))
    ```
    val downloadInfo = DownloadInfo("https://wanandroid.com/","blogimgs/2d120094-e1ee-47fb-a155-6eb4ca49d01f.apk")
    model.download(downloadInfo,object : DownloadCallback {
        override fun onStart() {
    
        }
    
        override fun onSuccess(path: String) {
            view.activity().installApk(path)
        }
    
        override fun onComplete() {
        }
    
        override fun onProgress(readLength: Long, countLength: Long) {
            view.setDownloadProgress((readLength*100/countLength).toInt())
        }
    
        override fun onError(t: Throwable) {
            println(t.message)
        }
    })
    ```
    * DownloadInfo
  
        ```
        data class DownloadInfo(
              /**
               * 域名
               */
              val baseUrl:String,
              /**
               * 下载地址
               */
              val url:String,
              /**
               * 连接超时时间(单位:秒)
               */
              val connectTimeout:Long = 5
        ){
                override fun toString(): String {
                        return "$baseUrl$url"
                }
        }
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
  


## 第三方库

### [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)-刷新控件

### [Immersionbar](https://github.com/gyf-dev/ImmersionBar)-状态栏

### [Koin](https://github.com/InsertKoinIO/koin)

### [RxJava3](https://github.com/ReactiveX/RxJava)

### [RxAndroid](https://github.com/ReactiveX/RxAndroid)

### [OkHttp](https://github.com/square/okhttp)

### [Retrofit](https://github.com/square/retrofit)

### [Gson](https://github.com/google/gson)

### [AndroidUtilKTX](https://github.com/lulululbj/AndroidUtilCodeKTX)-工具类

### [LoadingView](https://github.com/catch-pig/LoadingView)-Loading动画