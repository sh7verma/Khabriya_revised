package com.sdei.khabriya.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
//    const val BASE_URL = "https://app.pagalparrot.com/wp-json/wp/v2/"
    const val BASE_URL = "https://news.khabriya.in/wp-json/wp/v2/"
    private var retrofit: Retrofit? = null
    private var apiInterface: ApiInterface? = null

    val instance: ApiInterface?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(provideOkHttpClient())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
            }

            if (apiInterface == null) {
                apiInterface = retrofit!!.create(ApiInterface::class.java)
            }

            return apiInterface
        }

    //Creating OKHttpClient
    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build()
    }




}
