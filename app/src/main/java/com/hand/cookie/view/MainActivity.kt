package com.hand.cookie.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import com.hand.cookie.BuildConfig
import com.hand.cookie.data.NowPlayingRequest
import com.hand.cookie.data.NowPlayingResponse
import com.hand.cookie.databinding.ActivityMainBinding
import com.hand.cookie.network.RetrofitClient
import com.hand.cookie.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var retrofit: Retrofit
    private lateinit var movieService: MovieService
    private lateinit var search: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()
        getNowPlayingMovie()
        init()

    }

    private fun init() {
        search = binding.svSearch

        initSearch()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        movieService = retrofit.create(MovieService::class.java)
    }

    private fun getNowPlayingMovie() {

        var nowPlayingService = movieService.getNowPlaying("${BuildConfig.TMDB_API_KEY}", "ko, en-US", 1)

        nowPlayingService.enqueue(object: Callback<NowPlayingResponse> {
            override fun onResponse(
                call: Call<NowPlayingResponse>,
                response: Response<NowPlayingResponse>
            ) {
                if(response.isSuccessful) {
                    val movies = response.body()!!.movies

                    Log.e("Movies: ", "$movies")
                }
            }

            override fun onFailure(call: Call<NowPlayingResponse>, t: Throwable) {
                Log.e("MoviesError", "$t")
            }
        })

    }

    private fun initSearch() {

    }
}