# CHANGE LOG
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

+ 将网络请求的GsonConverterFactory替换为SerializationCoverterFactory

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