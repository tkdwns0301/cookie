package com.hand.cookie.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Response (
    val response: String
)

@Parcelize
data class NowPlayingResponse(
    var page: Int,
    @SerializedName("results")
    var movies: ArrayList<Movie>,
    var dates: Date,
    var total_pages: Int,
    var total_result: Int
) : Parcelable

@Parcelize
data class Movie(
    var poster_path: String,
    var adult: Boolean,
    var overview: String,
    var release_data: String,
    var genre_ids: List<Int>,
    var id: Int,
    var original_title: String,
    var original_language: String,
    var title: String,
    var backdrop_path: String,
    var popularity: Number,
    var vote_count: Int,
    var video: Boolean,
    var vote_average: Number
): Parcelable

@Parcelize
data class Date (
    var maximum: String,
    var minimum: String
) : Parcelable