# CHANGE LOG

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