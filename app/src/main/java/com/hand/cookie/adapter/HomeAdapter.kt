package com.hand.cookie.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hand.cookie.R
import com.hand.cookie.data.Movie
import com.hand.cookie.databinding.HomeItemBinding
import com.hand.cookie.view.DetailActivity
import kotlin.math.roundToInt

class HomeAdapter(
    private val context: Context,
    private val dataSet: ArrayList<Movie>
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = dataSet[position]

        if(movie.poster_path.isNullOrEmpty()) {
            holder.poster.setImageDrawable(context.getDrawable(R.drawable.ic_launcher_background))
        } else {
            Glide.with(holder.itemView.context)
                .load("https://image.tmdb.org/t/p/w500/${movie.poster_path}")
                .into(holder.poster)
        }

        holder.title.text = "${movie.title}"

        holder.container.setOnClickListener {
            moveDetail(movie.id)
            Log.e("MovieID", "${movie.id}")
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    private fun moveDetail(movieId: Int) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("movieId", movieId)
        context.startActivity(intent)
    }

    class ViewHolder(private val binding: HomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val poster: ImageView = binding.ivPoster
        val title: TextView = binding.tvTitle
        val container: ConstraintLayout = binding.clMovie
    }
}