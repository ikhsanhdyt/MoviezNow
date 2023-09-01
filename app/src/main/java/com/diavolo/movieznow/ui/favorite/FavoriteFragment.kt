package com.diavolo.movieznow.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.diavolo.model.Movie
import com.diavolo.movieznow.R
import com.diavolo.movieznow.common.utils.gone
import com.diavolo.movieznow.common.utils.visible
import com.diavolo.movieznow.data.Resource
import com.diavolo.movieznow.databinding.FragmentFavoriteBinding
import com.diavolo.movieznow.ui.favorite.viewModel.FavoriteViewModel
import com.diavolo.movieznow.ui.home.adapter.MovieListAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class FavoriteFragment : Fragment(R.layout.fragment_favorite),MovieListAdapter.OnItemClickListener {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val movieListAdapter: MovieListAdapter by inject()
    private val favoriteViewModel:FavoriteViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        initData()
    }

    private fun initData() {
        favoriteViewModel.fetchFavoriteMovies()
        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            favoriteViewModel.favoriteMoviesState.collect {
                handleFavoriteMoviesDataState(it)
            }
        }
    }

    private fun handleFavoriteMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }
            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                state.data?.let {
                    if (state.data.isNotEmpty()) {
                        loadMovies(state.data)
                    } else {
                        binding.tvNoData.visibility = View.VISIBLE

                    }
                }

            }
            Resource.Status.ERROR -> {
                binding.progressBar.gone()

            }
            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
                binding.tvNoData.visibility = View.VISIBLE
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            movieListAdapter.clear()
            movieListAdapter.fillList(it)
        }
    }

    private fun initView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        movieListAdapter.setOnMovieClickListener(this)

        binding.rvFragmentFavMovieList.adapter = movieListAdapter
    }

    override fun onItemClick(movie: Movie, container: View) {
        Snackbar.make(container, "Coming Soon", Snackbar.LENGTH_LONG).show()

    }

}