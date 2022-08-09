package com.hand.cookie.data

data class Request(
    val request: String
)

// 현재 상영중인 영화 리스트
data class NowPlayingRequest(
    val api_key: String,
    val language: String,
    val page: Int
)

