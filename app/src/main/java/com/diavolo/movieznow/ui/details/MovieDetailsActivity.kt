package com.diavolo.movieznow.ui.details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.model.Review
import com.diavolo.model.ReviewMoviesResponse
import com.diavolo.model.Trailer
import com.diavolo.model.TrailerResponse
import com.diavolo.movieznow.R
import com.diavolo.movieznow.base.BaseActivity
import com.diavolo.movieznow.common.glide.load
import com.diavolo.movieznow.common.recyclerview.PaginationScrollListener
import com.diavolo.movieznow.common.utils.ColorUtils.darken
import com.diavolo.movieznow.common.utils.dp
import com.diavolo.movieznow.common.utils.gone
import com.diavolo.movieznow.common.utils.visible
import com.diavolo.movieznow.data.Resource
import com.diavolo.movieznow.databinding.ActivityMovieDetailsBinding
import com.diavolo.movieznow.ui.details.adapter.ReviewListAdapter
import com.diavolo.movieznow.ui.details.adapter.TrailerListAdapter
import com.diavolo.movieznow.ui.details.viewModel.MovieDetailsViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MovieDetailsActivity : BaseActivity(), TrailerListAdapter.OnItemClickListener,
    ReviewListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMovieDetailsBinding
    private val args: MovieDetailsActivityArgs by navArgs()

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    private val trailerListAdapter: TrailerListAdapter by inject()
    private val reviewListAdapter: ReviewListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initData()
    }

    private fun initData() {
        observeData()
    }

    private fun initView() {
        setupToolbar()
        clearStatusBar()

        setupPosterImage()
        setContentData()
        setupTrailerRecyclerView()
        setupReviewRecyclerView()

    }

    private fun observeData() {
        lifecycleScope.launch {
            movieDetailsViewModel.trailerMovieState.collect {
                handleTrailerDataState(it)
            }
        }
        lifecycleScope.launch {
            movieDetailsViewModel.reviewState.collect {
                handleReviewDataState(it)
            }
        }
        lifecycleScope.launch {
            movieDetailsViewModel.movieId.collect {
                movieDetailsViewModel.fetchTrailer()
                movieDetailsViewModel.fetchReview()
            }
        }
        lifecycleScope.launch {
            movieDetailsViewModel.favoritesState.collect {
                handleFavoriteMovieDataState(it)
            }
        }
    }

    private fun handleTrailerDataState(state: Resource<TrailerResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }

            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadTrailer(state.data)
            }

            Resource.Status.ERROR -> {
                binding.progressBar.gone()

//                Snackbar.make(
//                    binding.progressBar,
//                    getString(R.string.error_message_pattern, state.message),
//                    Snackbar.LENGTH_LONG
//                )
//                    .setAnchorId(R.id.bottom_navigation).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun loadTrailer(trailer: TrailerResponse?) {
        trailer?.let {
            trailerListAdapter.clear()
            trailerListAdapter.fillList(it.trailers)
        }

    }

    private fun handleReviewDataState(state: Resource<ReviewMoviesResponse>) {
        when (state.status) {
            Resource.Status.LOADING -> {
                binding.progressBar.visible()
            }

            Resource.Status.SUCCESS -> {
                binding.progressBar.gone()
                loadReviews(state.data)
            }

            Resource.Status.ERROR -> {
                binding.progressBar.gone()

//                Snackbar.make(
//                    binding.progressBar,
//                    getString(R.string.error_message_pattern, state.message),
//                    Snackbar.LENGTH_LONG
//                )
//                    .setAnchorId(R.id.bottom_navigation).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun handleFavoriteMovieDataState(state: Resource<Boolean>) {
        when (state.status) {
            Resource.Status.LOADING -> {}
            Resource.Status.SUCCESS -> {
                updateFavoriteButton(state.data)
            }

            Resource.Status.ERROR -> {
                Toast.makeText(this, "Error: ${state.message}", Toast.LENGTH_LONG).show()
            }

            Resource.Status.EMPTY -> {
                Timber.d("Empty state.")
            }
        }
    }

    private fun loadReviews(review: ReviewMoviesResponse?) {
        review?.let {
            reviewListAdapter.clear()
            reviewListAdapter.fillList(it.reviews)
        }

    }

    private fun updateFavoriteButton(data: Boolean?) {
        data?.let { favorite ->
            binding.favoriteFab.setImageResource(
                if (favorite)
                    R.drawable.ic_star_filled
                else
                    R.drawable.ic_star_border
            )
        }
    }

    private fun setContentData() {
        movieDetailsViewModel.updateMovieId(args.movie.id.toString())
        movieDetailsViewModel.fetchFavoriteMovieState(args.movie)

        binding.tvHeaderTitle.text = args.movie.title
        binding.tvHeaderRelease.text = getString(R.string.release_date, args.movie.release_date)
        binding.tvHeaderStar.rating = args.movie.vote_average.div(2).toFloat()
        binding.detailBodySummary.text = args.movie.overview

        binding.favoriteFab.setOnClickListener {
            movieDetailsViewModel.toggleFavorite(args.movie)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupPosterImage() {
        postponeEnterTransition()

        binding.ivActivityMovieDetails.transitionName = args.movie.id.toString()
        binding.ivActivityMovieDetails.load(
            url = ApiClient.POSTER_BASE_URL + args.movie.poster_path,
            width = 160.dp,
            height = 160.dp
        ) { color ->
            window?.statusBarColor = color.darken
            binding.collapsingToolbar.setBackgroundColor(color)
            binding.collapsingToolbar.setContentScrimColor(color)
            startPostponedEnterTransition()
        }
    }

    private fun setupTrailerRecyclerView() {
        trailerListAdapter.setOnTrailerClickListener(this)
        binding.rvTrailers.apply {
            layoutManager = LinearLayoutManager(
                this@MovieDetailsActivity, LinearLayoutManager.HORIZONTAL, false
            )

            adapter = this@MovieDetailsActivity.trailerListAdapter
        }


    }

    private fun setupReviewRecyclerView() {
        reviewListAdapter.setOnReviewClickListener(this)

        binding.rvReview.adapter = reviewListAdapter
        binding.rvReview.addOnScrollListener(object :
            PaginationScrollListener(binding.rvReview.linearLayoutManager) {
            override fun isLoading(): Boolean {

                return movieDetailsViewModel.reviewState.value.status == Resource.Status.LOADING
            }

            override fun isLastPage(): Boolean {
                return movieDetailsViewModel.isLastPage()
            }

            override fun loadMoreItems() {
                movieDetailsViewModel.fetchNextReviews()
            }
        })
    }

    override fun onItemClick(trailer: Trailer, container: View) {
    }

    override fun onItemClick(review: Review, container: View) {

    }
}