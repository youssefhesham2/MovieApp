package com.example.movieapp.ui.frgaments.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.domain.entities.ResultsDomainEntity
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailsFragmentBinding
import com.example.movieapp.ui.frgaments.base.BaseFragment
import com.example.movieapp.ui.frgaments.movies.MoviesViewModel
import com.example.movieapp.utils.Constants
import com.squareup.picasso.Picasso

class MovieDetailsFragment : BaseFragment() {
    private lateinit var binding: MovieDetailsFragmentBinding
    private var moviesEntity: ResultsDomainEntity? = null
    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.movie_details_fragment, container, false)
        moviesEntity = arguments?.getSerializable(Constants.MOVIE_KEY) as ResultsDomainEntity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        moviesEntity?.let { movieDetailsViewModel.onGetMovieBundle(it) }
    }

    override fun observeViewModel() {
        observeMovieOriginalTitle()
        observeMoviePoster()
        observeMovieOverview()
        observeMovieVoteAverage()
        observeMovieReleaseDate()
    }

    private fun observeMovieOriginalTitle() {
        movieDetailsViewModel.movieOriginalTitle.observe(
            requireActivity(),
            Observer { originalTitle ->
                binding.tvMovieTitle.text = originalTitle
            })
    }

    private fun observeMoviePoster() {
        movieDetailsViewModel.moviePoster.observe(requireActivity(), Observer { moviePosterUrl ->
            Picasso.get().load(moviePosterUrl).into(binding.imgMoviePoster)
        })
    }

    private fun observeMovieOverview() {
        movieDetailsViewModel.movieOverview.observe(requireActivity(), Observer { overview ->
            binding.tvMovieOverview.text = overview
        })
    }

    private fun observeMovieVoteAverage() {
        movieDetailsViewModel.movieVoteAverage.observe(requireActivity(), Observer { voteAverage ->
            binding.movieRatingBar.rating = voteAverage
        })
    }

    private fun observeMovieReleaseDate() {
        movieDetailsViewModel.releaseDate.observe(requireActivity(), Observer { releaseDate ->
            binding.tvMovieReleaseDate.text = releaseDate
        })
    }
}