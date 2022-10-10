package com.example.movieapp.ui.frgaments.moviedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.domain.entities.ResultsDomainEntity

class MovieDetailsViewModel : ViewModel() {
    val movieOriginalTitle = MutableLiveData<String>()
    val moviePoster = MutableLiveData<String>()
    val movieOverview = MutableLiveData<String>()
    val movieVoteAverage = MutableLiveData<Float>()
    val releaseDate = MutableLiveData<String>()

    fun onGetMovieBundle(movieEntity: ResultsDomainEntity) {
        movieOriginalTitle.postValue(movieEntity.original_title)
        moviePoster.postValue(movieEntity.poster_path)
        movieOverview.postValue(movieEntity.overview)
        movieVoteAverage.postValue(movieEntity.vote_average)
        releaseDate.postValue(movieEntity.release_date)
    }
}