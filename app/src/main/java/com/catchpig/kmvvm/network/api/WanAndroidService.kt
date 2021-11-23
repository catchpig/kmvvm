package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.entity.Article
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.entity.PageData
import com.catchpig.kmvvm.network.RequestInterceptor
import com.catchpig.kmvvm.network.WanAndroidConverterFactory
import io.reactivex.rxjava3.core.Flowable
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


@ServiceApi(
    baseUrl = "https://www.wanandroid.com/",
    factory = WanAndroidConverterFactory::class,
    interceptors = [RequestInterceptor::class]
)
interface WanAndroidService {

    @GET("banner/json")
    suspend fun queryBanner(): MutableList<Banner>

    @GET("article/list/{pageIndex}/json")
    suspend fun queryArticles(
        @Path("pageIndex") pageIndex: Int,
        @Query("page_size") pageSize: Int
    ): PageData<Article>
}