package com.hand.cookie.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.WindowManager
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hand.cookie.BuildConfig
import com.hand.cookie.R
import com.hand.cookie.adapter.HomeAdapter
import com.hand.cookie.data.Movie
import com.hand.cookie.data.NowPlayingResponse
import com.hand.cookie.databinding.ActivityMainBinding
import com.hand.cookie.network.RetrofitClient
import com.hand.cookie.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var retrofit: Retrofit
    private lateinit var movieService: MovieService
    private lateinit var search: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioGroup: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRetrofit()
        init()


    }

    private fun init() {
        search = binding.svSearch
        recyclerView = binding.rvContent
        radioGroup = binding.radioGroup

        initSearch()
        initRecyclerView()
        initRadioGroup()
    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        movieService = retrofit.create(MovieService::class.java)
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        getNowPlayingMovie()
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rb_play -> {
                    getNowPlayingMovie()
                    Log.e("RB_PLAY", "Clicked")
                }
                R.id.rb_tobe -> Log.e("RB_TOBE", "Clicked")

            }
        }
    }

    private fun getNowPlayingMovie() {
        var nowPlayingService = movieService.getNowPlaying("${BuildConfig.TMDB_API_KEY}", "ko", 2)

        nowPlayingService.enqueue(object : Callback<NowPlayingResponse> {
            override fun onResponse(
                call: Call<NowPlayingResponse>,
                response: Response<NowPlayingResponse>
            ) {
                if (response.isSuccessful) {
                    val movies = response.body()!!.movies

                    setMovieAdapter(movies)

                    Log.e("Movies: ", "$movies")
                }
            }

            override fun onFailure(call: Call<NowPlayingResponse>, t: Throwable) {
                Log.e("MoviesError", "$t")
            }
        })
    }


    private fun setMovieAdapter(movies: ArrayList<Movie>) {
        if (movies.isNotEmpty()) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            val adapter = HomeAdapter(this, movies)

            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initSearch() {

    }



}