package com.hand.cookie.view

import android.content.Context
import android.inputmethodservice.KeyboardView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
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

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var search: SearchView
    private lateinit var retrofit: Retrofit
    private lateinit var movieService: MovieService
    private lateinit var recyclerView: RecyclerView
    private lateinit var radioGroup: RadioGroup
    private lateinit var movieListView: ConstraintLayout

    private var page = 1
    private var totalMovies = ArrayList<Movie>()

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
        movieListView = binding.clMovieList

        radioGroup.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        recyclerView.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }

        initRecyclerView()
        initRadioGroup()
        initSearch()
    }

    private fun initSearch() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    radioGroup.visibility = View.VISIBLE
                    recyclerView.layoutManager = GridLayoutManager(
                        applicationContext,
                        2,
                        GridLayoutManager.HORIZONTAL,
                        false
                    )
                    page = 1
                    totalMovies = ArrayList()
                    getNowPlayingMovie()
                } else {
                    radioGroup.visibility = View.GONE
                    recyclerView.layoutManager =
                        GridLayoutManager(applicationContext, 3, GridLayoutManager.VERTICAL, false)
                    page = 1
                    totalMovies = ArrayList<Movie>()
                    getSearchMovie(newText.toString())
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {

                return true
            }
        })

    }

    private fun initRetrofit() {
        retrofit = RetrofitClient.getInstance()
        movieService = retrofit.create(MovieService::class.java)
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        getNowPlayingMovie()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    getNowPlayingMovie()
                }
            }
        })
    }

    private fun initRadioGroup() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_play -> {
                    page = 1
                    totalMovies = ArrayList<Movie>()
                    getNowPlayingMovie()
                }
                R.id.rb_tobe -> {
                    page = 1
                    totalMovies = ArrayList<Movie>()
                    getUpcomingMovies()
                }

            }
        }
    }

    private fun getNowPlayingMovie() {

        var nowPlayingService =
            movieService.getNowPlaying("${BuildConfig.TMDB_API_KEY}", "ko", page++, "KR")

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

    private fun getUpcomingMovies() {
        var upcomingService =
            movieService.getUpcoming("${BuildConfig.TMDB_API_KEY}", "ko", page++, "KR")

        upcomingService.enqueue(object : Callback<NowPlayingResponse> {
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

    private fun getSearchMovie(query: String) {
        var searchService =
            movieService.getSearch("${BuildConfig.TMDB_API_KEY}", "ko", page++, "KR", query)

        searchService.enqueue(object : Callback<NowPlayingResponse> {
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
        totalMovies.addAll(movies)

        if (totalMovies.isNotEmpty()) {
            val recyclerViewState = recyclerView.layoutManager?.onSaveInstanceState()
            val adapter = HomeAdapter(this, totalMovies)
            recyclerView.adapter = adapter
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
            adapter.notifyDataSetChanged()
        }
    }

}