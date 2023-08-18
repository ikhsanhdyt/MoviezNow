package com.diavolo.movieznow.ui.details

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.model.Trailer
import com.diavolo.model.TrailerResponse
import com.diavolo.movieznow.R
import com.diavolo.movieznow.base.BaseActivity
import com.diavolo.movieznow.common.glide.load
import com.diavolo.movieznow.common.recyclerview.PaginationScrollListener
import com.diavolo.movieznow.common.utils.ColorUtils.darken
import com.diavolo.movieznow.common.utils.dp
import com.diavolo.movieznow.common.utils.gone
import com.diavolo.movieznow.common.utils.setAnchorId
import com.diavolo.movieznow.common.utils.visible
import com.diavolo.movieznow.data.Resource
import com.diavolo.movieznow.databinding.ActivityMovieDetailsBinding
import com.diavolo.movieznow.ui.details.adapter.ReviewListAdapter
import com.diavolo.movieznow.ui.details.adapter.TrailerListAdapter
import com.diavolo.movieznow.ui.details.viewModel.MovieDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MovieDetailsActivity : BaseActivity(), TrailerListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMovieDetailsBinding
    private val args: MovieDetailsActivityArgs by navArgs()

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModel()
    private val trailerListAdapter: TrailerListAdapter by inject()
    private val reviewListAdapter: ReviewListAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        clearStatusBar()

        setupPosterImage()
        setContentData()
        setupTrailerRecyclerView()


        lifecycleScope.launch {
            movieDetailsViewModel.trailerMovieState.collect {
                handleTrailerDataState(it)
            }
        }
        lifecycleScope.launch {
            movieDetailsViewModel.movieId.collect {
                movieDetailsViewModel.fetchTrailer()
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


    private fun setContentData() {
        movieDetailsViewModel.updateMovieId(args.movie.id.toString())
        binding.tvHeaderTitle.text = args.movie.title
        binding.tvHeaderRelease.text = getString(R.string.release_date, args.movie.release_date)
        binding.tvHeaderStar.rating = args.movie.vote_average.div(2).toFloat()
        binding.detailBodySummary.text = args.movie.overview
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
            layoutManager =
                LinearLayoutManager(this@MovieDetailsActivity, LinearLayoutManager.HORIZONTAL, false)

            adapter = this@MovieDetailsActivity.trailerListAdapter
        }


    }

    override fun onItemClick(trailer: Trailer, container: View) {
    }
}