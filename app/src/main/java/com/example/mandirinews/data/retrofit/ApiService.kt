package com.example.mandirinews.data.retrofit

import com.example.mandirinews.data.response.NewsResponse
import retrofit2.http.*

interface ApiService {

    @GET("top-headlines?country=us")
    suspend fun getHeadlineNews(
        @Query("apiKey") apiKey: String = "0a0ee8f1339c4786ab0dbb922f32abcb"
    ): NewsResponse

    @GET("everything")
    suspend fun getAllNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "0a0ee8f1339c4786ab0dbb922f32abcb"
    ): NewsResponse
}