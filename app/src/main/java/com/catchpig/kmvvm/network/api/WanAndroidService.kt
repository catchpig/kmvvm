package com.catchpig.kmvvm.network.api

import com.catchpig.annotation.ServiceApi
import com.catchpig.kmvvm.entity.Article
import com.catchpig.kmvvm.entity.Banner
import com.catchpig.kmvvm.entity.PageData
import com.catchpig.kmvvm.entity.User
import com.catchpig.kmvvm.network.ResponseBodyConverter
import com.catchpig.kmvvm.network.interceptor.RequestInterceptor
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import retrofit2.http.*


@ServiceApi(
        baseUrl = "https://www.wanandroid.com/",
        responseConverter = ResponseBodyConverter::class,
        interceptors = [RequestInterceptor::class],
        debugInterceptors = [OkHttpProfilerInterceptor::class]
)
interface WanAndroidService {

    @POST("user/register")
    suspend fun register(@Body map: Map<String, String>): String

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
            @Field("username") username: String,
            @Field("password") password: String
    ): User

    @GET("banner/json")
    suspend fun queryBanner(): MutableList<Banner>

    @GET("article/list/{pageIndex}/json")
    suspend fun queryArticles(
            @Path("pageIndex") pageIndex: Int,
            @Query("page_size") pageSize: Int
    ): PageData<Article>
}