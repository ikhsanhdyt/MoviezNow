package com.diavolo.movieznow.ui.home

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.diavolo.model.Genre
import com.diavolo.model.Movie
import com.diavolo.movieznow.R
import com.diavolo.movieznow.common.recyclerview.PaginationScrollListener
import com.diavolo.movieznow.common.utils.gone
import com.diavolo.movieznow.common.utils.setAnchorId
import com.diavolo.movieznow.common.utils.visible
import com.diavolo.movieznow.data.Resource
import com.diavolo.movieznow.databinding.FragmentMovieListBinding
import com.diavolo.movieznow.ui.home.adapter.ImageSliderAdapter
import com.diavolo.movieznow.ui.home.adapter.MovieListAdapter
import com.diavolo.movieznow.ui.home.viewModel.MovieListViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import androidx.core.util.Pair as UtilPair


/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class MovieListFragment : Fragment(R.layout.fragment_movie_list),
    MovieListAdapter.OnItemClickListener, ImageSliderAdapter.OnItemClickListener {
    private val movieListViewModel: MovieListViewModel by sharedViewModel()
    private val movieListAdapter: MovieListAdapter by inject()

    private val imageSliderAdapter: ImageSliderAdapter by inject()

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private lateinit var dots: ArrayList<TextView>

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
        setupViewPager()


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
            movieListViewModel.genreId.collect {
                movieListViewModel.refreshMovies()
            }

        }

    }

    override fun onItemClick(movie: Movie, container: View) {
        val action = MovieListFragmentDirections.navigateToMovieDetails(
            movie = movie
        )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            UtilPair.create(container, container.transitionName)
        )

        findNavController().navigate(action, ActivityNavigatorExtras(options))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        movieListViewModel.disposable?.dispose()
    }

    private fun handleMoviesDataState(state: Resource<List<Movie>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
                binding.tvNoData.visible()
            }

            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                binding.tvNoData.gone()
                loadCarousel(state.data)
                loadMovies(state.data)
            }

            Resource.Status.ERROR -> {
                binding.tvNoData.gone()
                binding.progressBar.gone()
                Toast.makeText(requireContext(), "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
                binding.tvNoData.visible()
            }
        }
    }

    private fun handleGenreDataState(state: Resource<List<Genre>>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }

            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadChipGenres(state.data)
            }

            Resource.Status.ERROR -> {
                binding.progressBar.gone()

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

    private fun loadCarousel(movies: List<Movie>?) {
        dots = ArrayList()
        movies?.let {
            val takeFour = movies.take(4)
            imageSliderAdapter.clear()
            imageSliderAdapter.fillList(takeFour)

            setIndicator()

            Timber.tag("MovieListFragment-loadCarousel").e(dots.toString())
            Timber.tag("MovieListFragment-loadCarousel").e(dots.size.toString())
        }
    }

    private fun setIndicator() {
        dots.clear()
        binding.llDotsIndicator.removeAllViews()
        for (i in 0 until 4) {
            dots.add(TextView(requireContext()))
            dots[i].text = Html.fromHtml("&#9679", Html.FROM_HTML_MODE_LEGACY).toString()
            dots[i].textSize = 18f
            binding.llDotsIndicator.addView(dots[i])
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

            genreChip.setOnCheckedChangeListener { _, b ->
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
                return movieListViewModel.movieListState.value.status == Resource.Status.LOADING

            }

            override fun isLastPage(): Boolean {
                return movieListViewModel.isLastPage()
            }

            override fun loadMoreItems() {
                movieListViewModel.fetchNextMovies()
            }
        })
    }

    private fun setupViewPager() {
        imageSliderAdapter.setOnSliderClickListener(this)

        binding.vpCarousel.adapter = imageSliderAdapter

        binding.vpCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selectedDot(position)
                super.onPageSelected(position)
            }
        })

    }

    private fun selectedDot(position: Int) {
        for (i in 0 until 4) {
            if (i == position) {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
            } else {
                dots[i].setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
            }
        }
    }

}