package com.example.movieapp.ui.frgaments.movies

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.domain.entities.ResultsDomainEntity
import com.example.movieapp.R
import com.example.movieapp.adapters.MovieAdapter
import com.example.movieapp.databinding.MoviesFragmentBinding
import com.example.movieapp.extensions.isInternetConnected
import com.example.movieapp.ui.frgaments.base.BaseFragment
import com.example.movieapp.utils.Constants
import com.example.movieapp.utils.LoadingState
import java.io.Serializable

class MoviesFragment : BaseFragment() {
    private lateinit var binding: MoviesFragmentBinding
    private val moviesViewModel: MoviesViewModel by viewModels()
    private var movieAdapter: MovieAdapter? = null
    private var movies = ArrayList<ResultsDomainEntity>()
    private var idLastItemSelectedFromSettingsMenu: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        restoreInstanceState(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.movies_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSettingMenu()
        setupMoviesRecyclerView()
        if (movies.isEmpty()) getMostPopularMovies()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        idLastItemSelectedFromSettingsMenu?.let {
            outState.putInt(
                Constants.ITEM_SETTING_MENU,
                it
            )
        }

        outState.putSerializable(Constants.MOVIES_LIST, movies as Serializable)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        savedInstanceState?.getInt(Constants.ITEM_SETTING_MENU)
            ?.let { idLastItemSelectedFromSettingsMenu = it }

        savedInstanceState?.getSerializable(Constants.MOVIES_LIST)?.let {
            movies = it as ArrayList<ResultsDomainEntity>
        }
    }

    override fun observeViewModel() {
        observeLoading()
        observeMovies()
        observeEdgeCase()
        observeFailure()
        observeException()
    }

    private fun setupSettingMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.setting_menu, menu)
                idLastItemSelectedFromSettingsMenu?.let { menu.findItem(it).isChecked = true }
                    ?: run {
                        idLastItemSelectedFromSettingsMenu = R.id.most_popular
                        menu.findItem(idLastItemSelectedFromSettingsMenu!!).isChecked = true
                    }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                idLastItemSelectedFromSettingsMenu = menuItem.itemId
                return when (menuItem.itemId) {
                    R.id.most_popular -> {
                        if (!menuItem.isChecked) getMostPopularMovies()
                        menuItem.isChecked = true
                        true
                    }
                    R.id.top_rated -> {
                        if (!menuItem.isChecked) getTopRatedMovies()
                        menuItem.isChecked = true
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupMoviesRecyclerView() {
        binding.rvMovies.layoutManager = GridLayoutManager(requireActivity(), 3)
        movieAdapter = MovieAdapter(movies) { movie ->
            val bundle = bundleOf(Constants.MOVIE_KEY to movie)
            view?.findNavController()
                ?.navigate(R.id.action_moviesFragment_to_movieDetailsFragment, bundle)
        }
        binding.rvMovies.adapter = movieAdapter
    }

    private fun getMostPopularMovies() {
        moviesViewModel.fetchMostPopularMovies(isInternetConnected())
    }

    private fun getTopRatedMovies() {
        moviesViewModel.fetchTopRatedMovies(isInternetConnected())
    }

    private fun isInternetConnected(): Boolean = requireActivity().application.isInternetConnected()

    private fun observeLoading() {
        moviesViewModel.loading.observe(requireActivity(), Observer { loadingState ->
            when (loadingState) {
                LoadingState.SHOW -> {
                    hideEdgeCase()
                    hideMoviesRecyclerView()
                    showProgress()
                }
                LoadingState.DISMISS -> hideProgress()
            }
            movieAdapter?.addMovies(movies)
        })
    }

    private fun observeMovies() {
        moviesViewModel.movies.observe(requireActivity(), Observer {
            hideEdgeCase()
            showMoviesRecyclerView()
            movies = it as ArrayList
            movieAdapter?.addMovies(movies)
        })
    }

    private fun observeEdgeCase() {
        moviesViewModel.edgeCase.observe(requireActivity(), Observer { message ->
            hideMoviesRecyclerView()
            showEdgeCase()
            binding.tvEdgeCase.text = message
        })
    }

    private fun observeFailure() {
        moviesViewModel.failure.observe(requireActivity(), Observer { errorResponse ->
            hideMoviesRecyclerView()
            showEdgeCase()
            binding.tvEdgeCase.text = errorResponse.status_message
        })
    }

    private fun observeException() {
        moviesViewModel.exception.observe(requireActivity(), Observer { throwable ->
            hideMoviesRecyclerView()
            showEdgeCase()
            binding.tvEdgeCase.text = throwable.localizedMessage
        })
    }

    private fun hideProgress() {
        binding.progressLoading.visibility = View.GONE
    }

    private fun showProgress() {
        binding.progressLoading.visibility = View.VISIBLE
    }

    private fun showEdgeCase() {
        binding.tvEdgeCase.visibility = View.VISIBLE
    }

    private fun hideEdgeCase() {
        binding.tvEdgeCase.visibility = View.GONE
    }

    private fun showMoviesRecyclerView() {
        binding.rvMovies.visibility = View.VISIBLE
    }

    private fun hideMoviesRecyclerView() {
        binding.rvMovies.visibility = View.GONE
    }
}