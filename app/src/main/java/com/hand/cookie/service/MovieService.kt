package com.hand.cookie.service

import com.hand.cookie.data.NowPlayingRequest
import com.hand.cookie.data.NowPlayingResponse
import retrofit2.Call
import retrofit2.http.*

interface MovieService {
    @Headers("content-type: application/json")
    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<NowPlayingResponse>
}