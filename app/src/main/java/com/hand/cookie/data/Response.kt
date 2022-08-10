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

@Parcelize
data class DetailResponse(
    var id: Int,
    var results: List<Result>
): Parcelable

@Parcelize
data class Result(
    var iso_649_1: String,
    var iso_3166_1: String,
    var name: String,
    var key: String,
    var site: String,
    var size: Int,
    var type: String,
    var official: Boolean,
    var published_at: String,
    var id: String
) : Parcelable