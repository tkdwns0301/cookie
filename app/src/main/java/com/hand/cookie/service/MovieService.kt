package com.hand.cookie.service

import com.hand.cookie.data.NowPlayingRequest
import com.hand.cookie.data.NowPlayingResponse
import retrofit2.Call
import retrofit2.http.*

interface MovieService {
    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<NowPlayingResponse>

    @GET("movie/upcoming")
    fun getUpcoming(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<NowPlayingResponse>

    @GET("search/movie")
    fun getSearch(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("query") query: String
    ): Call<NowPlayingResponse>
}