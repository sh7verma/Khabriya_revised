package com.sdei.khabriya.network

import com.sdei.khabriya.models.MenuResponse
import com.sdei.khabriya.models.news.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query


interface ApiInterface {

    @GET("posts")
    fun getAllNews(
        @Query("_embed") embed: String?,
        @Query("page") page: String?
    ): Call<List<NewsResponse?>?>?

//    https://app.pagalparrot.com/wp-json/wp/v2/
//    https://app.pagalparrot.com/wp-json/wp/v2/posts?=search[keyword]
    @GET("posts")
    fun getSearch(
        @Query("_embed") embed: String?,
        @Query("search") search: String?,
        @Query("page") page: String?
    ): Call<List<NewsResponse?>?>?

    @Headers("Content-Type: application/json")
    @GET("https://app.pagalparrot.com/wp-json/menus/v1/menus/5")
    fun getMenu(): Call<ArrayList<MenuResponse>>

    //https://app.pagalparrot.com/wp-json/wp/v2/posts?categories=4

    @GET("posts")
    fun getCategoryWise(@Query("categories") category: String?): Call<List<NewsResponse?>>?

    @GET("posts")
    fun getData(
        @Query("_embed") embed: String?,
        @Query("page") page: String?,
        @Query("categories") category: String?
    ): Call<List<NewsResponse?>?>?

}
