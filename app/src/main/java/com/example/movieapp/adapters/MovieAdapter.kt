package com.example.movieapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.entities.ResultsDomainEntity
import com.example.movieapp.R
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movies: ArrayList<ResultsDomainEntity>,
    private val itemClick: (ResultsDomainEntity) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder {
        return MoviesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.movie_item, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val movie = movies[position]
        holder.movieName.text = movie.title
        Picasso.get().load(
            movie.poster_path
        ).into(holder.moviePoster)

        holder.itemView.setOnClickListener {
            itemClick.invoke(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun addMovies(movieList: List<ResultsDomainEntity>) {
        movies = movieList as ArrayList<ResultsDomainEntity>
        notifyDataSetChanged()
    }

    class MoviesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moviePoster: ImageView = itemView.findViewById(R.id.img_movie_poster)
        val movieName: TextView = itemView.findViewById(R.id.tv_movie_name)
    }
}