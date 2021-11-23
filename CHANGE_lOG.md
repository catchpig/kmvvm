# CHANGE LOG

### v0.3.0(2021.11.23)

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