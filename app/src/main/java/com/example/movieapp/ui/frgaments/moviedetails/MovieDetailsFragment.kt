package com.example.movieapp.ui.frgaments.moviedetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDetailsFragmentBinding
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.ui.frgaments.base.BaseFragment

class MovieDetailsFragment : BaseFragment() {
    private lateinit var binding: MovieDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.movie_details_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun observeViewModel() {
        TODO("Not yet implemented")
    }
}