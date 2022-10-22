![](https://img.shields.io/badge/dynamic/xml?color=green&label=annotation-release&query=%2F%2Fmetadata%2Fversioning%2Frelease&url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Fannotation%2Fmaven-metadata.xml)
![](https://img.shields.io/badge/dynamic/xml?color=green&label=annotation-snapshot&query=%2F%2Fmetadata%2Fversioning%2Flatest&url=https%3A%2F%2Fs01.oss.sonatype.org%2Fcontent%2Frepositories%2Fsnapshots%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Fannotation%2Fmaven-metadata.xml)

### 注解

#### 1. [Title](./src/main/java/com/catchpig/annotation/Title.kt)-标题

| 属性              | 类型          | 必须  | 默认         | 说明       |
|:----------------|:------------|:----|:-----------|:---------|
| value           | StringRes   | 是   | 无          | 标题内容     |
| backgroundColor | ColorRes    | 否   | 全局标题背景色    | 标题背景色    |
| textColor       | ColorRes    | 否   | 全局标题文字颜色   | 标题文字颜色   |
| backIcon        | DrawableRes | 否   | 全局标题返回按钮图标 | 标题返回按钮图标 |

#### 2. [OnClickFirstDrawable](./src/main/java/com/catchpig/annotation/OnClickFirstDrawable.kt)-标题上第一个图标按钮的点击事件

| 属性    | 类型          | 必须  | 默认  | 说明     |
|:------|:------------|:----|:----|:-------|
| value | DrawableRes | 是   | 无   | 按钮图片资源 |

#### 3. [OnClickFirstText](./src/main/java/com/catchpig/annotation/OnClickFirstText.kt)-标题上第一个文字按钮的点击事件

| 属性    | 类型        | 必须  | 默认  | 说明     |
|:------|:----------|:----|:----|:-------|
| value | StringRes | 是   | 无   | 按钮文字内容 |

#### 4. [OnClickSecondDrawable](./src/main/java/java/com/catchpig/annotation/OnClickSecondDrawable.kt)-标题上第二个图标按钮的点击事件

| 属性     | 类型          | 必须  | 默认  | 说明     |
|:-------|:------------|:----|:----|:-------|
| value  | DrawableRes | 是   | 无   | 按钮图片资源 |

#### 5. [OnClickSecondText](./src/main/java/com/catchpig/annotation/OnClickSecondText.kt)-标题上第二个文字按钮的点击事件

| 属性     | 类型        | 必须  | 默认  | 说明     |
|:-------|:----------|:----|:----|:-------|
| value  | StringRes | 是   | 无   | 按钮文字内容 |

#### 6. [StatusBar](./src/main/java/com/catchpig/annotation/StatusBar.kt)-状态栏

| 属性          | 类型      | 必须  | 默认    | 说明      |
|:------------|:--------|:----|:------|:--------|
| hide        | boolean | 否   | false | 隐藏状态栏   |
| enabled     | boolean | 否   | false | 状态栏是否可用 |
| transparent | boolean | 否   | false | 状态栏透明   |

#### 7. [Prefs](./src/main/java/com/catchpig/annotation/Prefs.kt)-SharedPreferences注解生成器

| 属性    | 类型                                                                      | 必须  | 默认                     | 说明                   |
|:------|:------------------------------------------------------------------------|:----|:-----------------------|:---------------------|
| value | String                                                                  | 否   | ""                     | 别名                   |
| mode  | [PrefsMode](./src/main/java/com/catchpig/annotation/enums/PrefsMode.kt) | 否   | PrefsMode.MODE_PRIVATE | 模式,对应PreferencesMode |

#### 8. [PrefsField](./src/main/java/com/catchpig/annotation/PrefsField.kt)-SharedPreferences字段注解

| 属性    | 类型     | 必须  | 默认  | 说明                   |
|:------|:-------|:----|:----|:---------------------|
| value | String | 否   | ""  | 字段别名,如果为空则取修饰字段的参数名称 |

#### 9. [FlowError](./src/main/java/com/catchpig/annotation/FlowError.kt)-Activity和Fragment中的Flow的onError方法统一处理

#### 10. [Adapter](./src/main/java/com/catchpig/annotation/Adapter.kt)-RecyclerAdapter的继承类注解，加上此注解之后可以自动找到对应的layout资源

#### 11. [GlobalConfig](./src/main/java/com/catchpig/annotation/GlobalConfig.kt)-全局参数配置

+ 注解在IGlobalConfig接口的实现类上面

#### 12. [ServiceApi](./src/main/java/com/catchpig/annotation/ServiceApi.kt)-网络请求接口注解类

| 属性                | 类型          | 必须  | 默认          | 说明                                                |
|:------------------|:------------|:----|:------------|:--------------------------------------------------|
| baseUrl           | String      | 是   | 无           | retrofit的baseurl                                  |
| responseConverter | Converter   | 是   | 无           | 接收数据转换器                                           |
| connectTimeout    | Long        | 否   | 5000        | http的超时时间                                         |
| readTimeout       | Long        | 否   | 5000        | http读取超时时间                                        |
| interceptors      | Interceptor | 否   | Interceptor | http拦截器                                           |
| debugInterceptors | Interceptor | 否   | Interceptor | debug模式下的http拦截器,只有NetManager.setDebug(true),才会生效 |
| rxJava            | Boolean     | 是   | false       | 是否使用RxJava接收网络请求数据                                |
