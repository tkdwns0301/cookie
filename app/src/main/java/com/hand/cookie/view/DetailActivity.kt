package com.hand.cookie.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.hand.cookie.BuildConfig
import com.hand.cookie.R
import com.hand.cookie.data.DetailResponse
import com.hand.cookie.databinding.ActivityDetailBinding
import com.hand.cookie.network.RetrofitClient
import com.hand.cookie.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class DetailActivity: YouTubeBaseActivity() {
    private lateinit var binding: ActivityDetailBinding

    private lateinit var retrofit: Retrofit
    private lateinit var movieService: MovieService
    private lateinit var youtubeView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()
        getDetail("ko")
        init()

    }

    private fun init() {
        youtubeView = binding.youtubePlayerView

    }

    private fun initYoutubePlayer(key: String) {
        youtubeView.initialize("develop", object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider,
                player: YouTubePlayer,
                wasRestored: Boolean
            ) {
                if (!wasRestored) {
                    player.cueVideo(key)
                }
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        movieService = retrofit.create(MovieService::class.java)
    }

    private fun getDetail(language: String) {
        val movieId = intent.getIntExtra("movieId", 0)

        val detailService = movieService.getDetail(movieId, "${BuildConfig.TMDB_API_KEY}", "$language")

        detailService.enqueue(object: Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful) {
                    val results = response.body()!!.results

                    if(results.isEmpty()) {
                        getDetail("en")
                    } else {
                        initYoutubePlayer(results[0].key)

                    }


                    Log.e("results", "$results")

                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e("Detail Error", "$t")
            }
        })
    }
}

