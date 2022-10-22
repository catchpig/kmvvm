![](https://img.shields.io/badge/dynamic/xml?color=green&label=download-release&query=%2F%2Fmetadata%2Fversioning%2Frelease&url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Fdownload%2Fmaven-metadata.xml)
![](https://img.shields.io/badge/dynamic/xml?color=green&label=download-snapshot&query=%2F%2Fmetadata%2Fversioning%2Flatest&url=https%3A%2F%2Fs01.oss.sonatype.org%2Fcontent%2Frepositories%2Fsnapshots%2Fio%2Fgithub%2Fcatchpig%2Fkmvvm%2Fdownload%2Fmaven-metadata.xml)

### 文件下载器

+ 支持RxJava和协程两种方式的文件下载,两种方式的接口是一样的,只是内部使用的逻辑不一样

+ 下载器默认有下载路径(默认下载路径请看[DownloadManager](./mvvm/src/main/java/com/catchpig/mvvm/manager/DownloadManager.kt)源码)

+ 可以自行设置下载路径,自行设置了下载路径,默认路径将不再被使用

  > 设置接口-setDownloadPath

  ```kotlin
  DownloadManager.setDownloadPath("${ContextManager.context.externalCacheDir!!.absolutePath}/kmvvmDownload")
  ```

  

#### 1. RxJava下载器-[RxJavaDownloadManager](./mvvm/src/main/java/com/catchpig/mvvm/listener/RxJavaDownloadManager.kt)

+ 使用RxJava下载器,必须要引入RxJava的依赖包和adapter-rxjava3依赖包

  ```groovy
  implementation("com.squareup.retrofit2:adapter-rxjava3:$retrofit2_version")
  
      //rxjava3
  implementation "io.reactivex.rxjava3:rxjava:$rxjava_version"
  implementation "io.reactivex.rxjava3:rxandroid:$rxandroid_version"
  ```

  

##### 单文件下载-回调返回下载路径和下载进度

> download(
>     downloadUrl: String,
>     callback: (path: String) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )

##### 单文件下载-回调返回下载File和下载进度

> ```kotlin
> downloadFile(
>     downloadUrl: String,
>     callback: (file: File) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )
> ```

##### 单文件下载-回调返回下载路径和下载进度

> ```kotlin
> multiDownload(
>     downloadUrls: Iterable<String>,
>     callback: (paths: MutableList<String>) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )
> ```

#### 2. 协程下载器 -[CoroutinesDownloadManager](./mvvm/src/main/java/com/catchpig/mvvm/listener/CoroutinesDownloadManager.kt)

##### 单文件下载-回调返回下载路径和下载进度

> download(
>     downloadUrl: String,
>     callback: (path: String) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )

##### 单文件下载-回调返回下载File和下载进度

> ```kotlin
> downloadFile(
>     downloadUrl: String,
>     callback: (file: File) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )
> ```

##### 单文件下载-回调返回下载路径和下载进度

> ```kotlin
> multiDownload(
>     downloadUrls: Iterable<String>,
>     callback: (paths: MutableList<String>) -> Unit,
>     progress: ((downloadProgress: DownloadProgress) -> Unit)? = null
> )
> ```

