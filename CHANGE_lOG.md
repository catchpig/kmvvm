# CHANGE LOG
### v1.1.0(2023.6.23)

+ 增加DialogFragment基类

### v1.0.9(2023.6.21)

+ 删除lifecycle相关的废弃方法
+ BaseView中增加snackBar接口

### v1.0.8(2023.6.4)

+ 调整状态的enable属性默认值
+ 增加Toast的扩展函数
+ 增加全局标题字体样式设置

### v1.0.7(2023.5.28)

+ PrefsGenerator增加同步和异步保存数据方法

### v1.0.6(2023.3.26)

+ ViewModel增加Flow的lifecycle扩展方法

### v1.0.5(2023.3.21)

+ 修复FragmentTransaction.replace异常

### v1.0.4(2023.1.13)

+ 增加RecyclerAdapter获取全部数据源的方法

### v1.0.3(2022.12.7)

+ 下载进度分发有rxjava改为handler

### v1.0.2(2022.11.8)

+ 修复ksp编译ServiceApi注解异常问题

### v1.0.1(2022.11.5)

+ 优化ksp
+ 增加加载失败页面全局配置

### v1.0.0(2022.10.31)

+ 迁移kapt至ksp,提升编译速度
+ 删除编译时注解Adapter
+ 调整view_root布局

### v0.5.3(2022.10.22)

+ 下载模块改成单独的module
+ utils移除android.permission.REQUEST_INSTALL_PACKAGES权限

### v0.5.1(2022.10.15)

+ 使用startup替换自定义ContentProvider,优化启动
+ 将BaseVMActivity和BaseVMFragment的lifecycle一系列协程方法抽成Flow的扩展方法
+ 调整日志输出格式

### v0.5.0(2022.10.14)

+ lifecycleFlow函数增加异常的回调

### v0.4.9(2022.10.13)

+ fix 调用startKtActivity崩溃问题

### v0.4.8(2022.9.24)

+ 改造单例为静态内部类单例,保证线程安全

### v0.4.7(2022.9.24)

+ 改造单例为静态内部类模式
+ 将SnackBar的手移到扩展函数里面

### v0.4.6(2022.8.30)

+ 日志增加方法名和行号的打印

### v0.4.5(2022.5.2)

+ 移除RxJava和retrofit的rxjava转换器的依赖

### v0.4.4(2022.4.21)

+ 优化下载代码

### v0.4.3(2022.4.20)

+ 优化下载代码

### v0.4.2(2022.4.19)

+ 文件下载增加协程方式

### v0.4.1(2022.1.14)

+ 多文件下载增加进度回调接口
+ 优化问文件下载接口

### v0.4.0(2021.12.19)

+ 优化代码


### v0.3.9(2021.12.17)

+ 优化Retrofit的Serialization,将ServiceApi注解的responseConverter属性的继承基类SerializationConverter

### v0.3.8(2021.12.16)

+ 将网络请求的GsonConverterFactory替换为SerializationConverterFactory

### v0.3.7(2021.12.11)

+ 优化网络请求框架
+ 1.将注解ServiceApi的factory参数改为responseConverter
+ 2.增加封装类GsonResponseBodyConverter
+ 3.增加封装抽象类BaseResponseBodyConverter

### v0.3.6(2021.12.7)

+ 优化刷新控件
+ 弃用IAdapterListControl接口
+ 删除IPageControl接口中的pageSize,nextPageIndex属性和getRefreshStatus方法
+ 封装控件RefreshRecyclerView

### v0.3.5(2021.12.6)

+ 解决单独添加footer崩溃问题(#I4L4U2)

### v0.3.4(2021.12.5)

+ 解决RecycleAdapter添加footer数据下标越界的崩溃问题

### v0.3.3(2021.12.4)

+ 注解ServiceApi增减debugInterceptors参数,只有NetManager.setDebug(true)的情况下,拦截器才会添加到OkHttp里面

### v0.3.2(2021.11.24)

+ IFlowError接口增加onBaseFragmentError和onBaseActivityError接口回调

### v0.3.1(2021.11.23)

+ 解决多个注解ServiceApi,apt生成代码错误问题

### v0.2.9(2021.11.21)

+ fragment和activity中增加snackbar的位置参数

### v0.2.8(2021.11.21)

+ 解决fragment弹出的snackBar在fragment下方的问题
+ snackBar弹出可以设置位置(Gravity)

### v0.2.7(2021.11.20)

+ 将ServiceApi注解从GlobalCompiler中分离出来,因为ServiceApi注解有可能在module中使用
+ 将注解ObserverError改为FlowError

### v0.2.6(2021.11.10)

+ fragment调整loadingview的实现
+ activity和fragment增加lifecycleScope+flow关于刷新列表的封装方法lifecycleFlowRefresh

### v0.2.5(2021.11.7)

+ activity和fragment封装不带loading的flow+lifecycleScope的方法(lifecycleFlow)

### v0.2.4(2021.11.6)

+ 修改lifecycle和Flow封装的方法名

### v0.2.3(2021.10.29)

+ 网络请求支持FLow回调
+ loading和hideLoading在activity中处理

### v0.2.2(2021.10.25)

+ 解决StatusBar注解设置全屏状态栏和透明状态不起作用的问题

### v0.2.1(2021.10.24)

+ NetManger增加debug模式的设置

### v0.2.0(2021.10.23)

+ 增加Retrofit+OkHttp+RxJava的ServiceApi注解

### v0.1.9(2021.10.21)

+ 将分页个数和分页起始页放到全局变量参数里面,提供了getPageSize和getStartPageIndex接口

### v0.1.8(2021.10.20)

+ 将全部参数由style配置改为实现IGlobalConfig接口,并在实现类上加上注解GlobalConfig

### v0.1.7(2021.10.17)

+ IObserverError的onError参数BaseViewModel改为IBaseViewModel
+ IBaseViewModel增加sendMessage接口
+ RecyclerAdapter的子类如果没有使用注解Adapter,将会抛出AptRecyclerAdapterException异常