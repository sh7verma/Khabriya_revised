package com.sdei.khabriya.network

import com.sdei.khabriya.models.MenuResponse
import com.sdei.khabriya.models.news.NewsResponse
import com.sdei.khabriya.models.tv.ChannelModel
import com.sdei.khabriya.models.tv.TvLanguageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiInterface {

    @GET("posts")
    fun getAllNews(
        @Query("_embed") embed: String?,
        @Query("page") page: String?
    ): Call<List<NewsResponse?>?>?

    @GET("posts")
    fun getSearch(
        @Query("search") search: String?,
        @Query("page") page: String?
    ): Call<List<NewsResponse?>?>?

    @Headers("Content-Type: application/json")
    @GET("https://news.khabriya.in/wp-json/menus/v1/menus/5")
    fun getMenu(): Call<ArrayList<MenuResponse>>

    @GET("https://news.khabriya.in/wp-json/wp/v2/posts")
    fun getCategoryWise(@Query("categories") category: String?): Call<List<NewsResponse?>>?

    @Headers("Secret-Key: AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @GET("https://tv.khabriya.in/admin/list-lagnuage.php")
    fun getTvLanguageList(): Call<ArrayList<TvLanguageModel>>?

    @Headers("Secret-Key: AJT_Lbim_0f6bd8a808ea3e9996b3aee1900aa2e8")
    @GET("https://tv.khabriya.in/admin/fetch-api.php")
    fun getChannelList(@Query("add_language") category: String?): Call<ArrayList<ChannelModel>>?

    @GET("posts")
    fun getData(
        @Query("_embed") embed: String?,
        @Query("page") page: String?,
        @Query("categories") category: String?
    ): Call<List<NewsResponse?>?>?

}
