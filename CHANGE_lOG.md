# CHANGE LOG

### v0.1.8(2021.10.20)

+ 将全部参数由style配置改为实现IGlobalConfig接口,并在实现类上加上注解GlobalConfig

### v0.1.7(2021.10.17)

+ IObserverError的onError参数BaseViewModel改为IBaseViewModel
+ IBaseViewModel增加sendMessage接口
+ RecyclerAdapter的子类如果没有使用注解Adapter,将会抛出AptRecyclerAdapterException异常