# [kotlin-mvp](https://github.com/catch-pig/kotlin-mvvm)
[![](https://jitpack.io/v/com.gitee.catch-pig/kotlin-mvvm.svg)](https://jitpack.io/#com.gitee.catch-pig/kotlin-mvvm)

## 技术要点

### 1. RxJava+Retrofit+OkHttp实现链式http请求

### 2. 封装基类:BaseActivity、BaseVMActivity、BaseFragment、BaseVMFragment、RecycleAdapter、BaseViewModel

### 3. 封装工具扩展类：CalendarExt、ContextExt、DateExt、EditTextExt、GsonExt、RxJavaExt、StringExt、SnackbarExt

### 4. 引入LifeCycle，将ViewModel和Activity的生命周期绑定在一起

### 5. 将在Application中初始化移至到ContentProvider中,从而不用封装BaseApplication

### 6. APT(编译时注解)封装注解：OnClickFirstDrawable、OnClickFirstText、OnClickSecondDrawable、OnClickSecondText、Prefs、PrefsField、StatusBar

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
```
implementation "com.gitee.catch-pig.kotlin-mvp:mvp:last_version"
kapt "com.gitee.catch-pig.kotlin-mvp:compiler:last_version"
```
## 使用

### 1. 在需要使用状态栏、标题栏、加载动画的主题中配置全局参数:
    
   |属性|类型|必须|默认|说明|
   |---|:---:|:---|:---|:---|
   |title_bar_height|dimension|是|无|标题栏高度|
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
        <item name="title_bar_height">80dp</item>
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
  * 使用MVP的继承BaseVMActivity
  * 不使用MVP的继承BaseActivity
### 3. Fragment
  * 使用MVP的继承BaseVMFragment
  * 不使用MVP的继承BaseFragment
### 4. 如果使用RecycleView的时候,Adapter可以继承RecycleAdapter来使用
    
  > adapter使用了ViewBanding,只需要实现以下两个方法
  
  ```
  override fun itemViewBanding(): Class<ItemUserBinding> {
        return ItemUserBinding::class.java
    }

  override fun bindViewHolder(holder: CommonViewHolder<ItemUserBinding>, m: User, position: Int) {
    holder.viewBanding {
        it.name.text = m.name
    }
    setOnItemClickListener(object : OnItemClickListener<User> {
        override fun itemClick(id: Int, m: User, position: Int) {

        }
    })
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
 
 + 单文件下载方法download([DownloadInfo](./mvp/src/main/java/com/catchpig/mvp/bean/DownloadInfo.kt),[DownloadCallback](./mvp/src/main/java/com/catchpig/mvp/listener/DownloadCallback.kt))
    ```
    DownloadManager.download(downloadUrl, {
            
        }, { readLength, countLength ->
            progressLiveData.value = (readLength * 100 / countLength).toInt()
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