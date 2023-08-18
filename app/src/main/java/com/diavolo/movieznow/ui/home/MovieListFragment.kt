package com.diavolo.movieznow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.diavolo.model.Genre
import com.diavolo.model.Movie
import com.diavolo.movieznow.R
import com.diavolo.movieznow.common.recyclerview.PaginationScrollListener
import com.diavolo.movieznow.common.utils.gone
import com.diavolo.movieznow.common.utils.setAnchorId
import com.diavolo.movieznow.common.utils.visible
import com.diavolo.movieznow.data.Resource
import com.diavolo.movieznow.databinding.FragmentMovieListBinding
import com.diavolo.movieznow.ui.home.master.MovieListAdapter
import com.diavolo.movieznow.ui.home.viewModel.MovieListViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class MovieListFragment : Fragment(R.layout.fragment_movie_list),
    MovieListAdapter.OnItemClickListener {
    private val movieListViewModel: MovieListViewModel by sharedViewModel()
    private val movieListAdapter: MovieListAdapter by inject()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        movieListViewModel.fetchGenreListMovies()

        setupRecyclerView()
        setupSwipeRefresh()


        viewLifecycleOwner.lifecycleScope.launch {
            movieListViewModel.movieListState.collect {
                handleMoviesDataState(it)
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            movieListViewModel.genreListState.collect {
                handleGenreDataState(it)
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {
            movieListViewModel.genreId.collect { updatedGenreId ->
                movieListViewModel.refreshMovies()
            }

        }

    }


    override fun onItemClick(movie: Movie, container: View) {
//        val action = PopularFragmentDirections.navigateToMovieDetails(id = movie.id, posterPath = movie.poster_path)
//        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(),
//            Pair.create(container, container.transitionName)
//        )
//
//        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        binding.genresChipGroup.removeAllViews()
        movieListViewModel.disposable?.dispose()
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.srlFragmentMovieList.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding.srlFragmentMovieList.isRefreshing = false
                loadMovies(state.data)
            }

            Resource.Status.ERROR -> {
                binding.srlFragmentMovieList.isRefreshing = false
                binding.pbFragmentMovieList.gone()
                Snackbar.make(
                    binding.srlFragmentMovieList,
                    getString(R.string.error_message_pattern, state.message),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorId(R.id.bottom_navigation).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun handleGenreDataState(state: Resource<List<Genre>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.srlFragmentMovieList.isRefreshing = true
            }

            Resource.Status.SUCCESS -> {
                binding.srlFragmentMovieList.isRefreshing = false
                loadChipGenres(state.data)
            }

            Resource.Status.ERROR -> {
                binding.srlFragmentMovieList.isRefreshing = false
                binding.pbFragmentMovieList.gone()
                Snackbar.make(
                    binding.srlFragmentMovieList,
                    getString(R.string.error_message_pattern, state.message),
                    Snackbar.LENGTH_LONG
                )
                    .setAnchorId(R.id.bottom_navigation).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun loadMovies(movies: List<Movie>?) {
        movies?.let {
            if (movieListViewModel.isFirstPage()) {
                // Remove previous movies
                movieListAdapter.clear()
            }

            movieListAdapter.fillList(it)
        }
    }

    private fun loadChipGenres(genres: List<Genre>?) {
        genres?.forEach {
            val genreChip = layoutInflater.inflate(
                R.layout.item_genre_chip,
                binding.genresChipGroup,
                false
            ) as Chip

            genreChip.tag = it.id
            genreChip.text = it.name
            genreChip.isCheckable = true

            genreChip.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    movieListViewModel.updateGenreId(genreChip.tag.toString())
                }
            }
            binding.genresChipGroup.addView(genreChip)

        }
    }

    private fun setupRecyclerView() {
        movieListAdapter.setOnMovieClickListener(this)

        binding.rvFragmentMovieList.adapter = movieListAdapter
        binding.rvFragmentMovieList.addOnScrollListener(object :
            PaginationScrollListener(binding.rvFragmentMovieList.linearLayoutManager) {
            override fun isLoading(): Boolean {
                val isLoading = binding.srlFragmentMovieList.isRefreshing

                if (isLoading) {
                    binding.pbFragmentMovieList.visible()
                } else {
                    binding.pbFragmentMovieList.gone()
                }

                return isLoading
            }

            override fun isLastPage(): Boolean {
                return movieListViewModel.isLastPage()
            }

            override fun loadMoreItems() {
                movieListViewModel.fetchNextMovies()
            }
        })
    }

    private fun setupSwipeRefresh() {
        binding.srlFragmentMovieList.setOnRefreshListener {
            movieListViewModel.refreshMovies()
        }
    }


}