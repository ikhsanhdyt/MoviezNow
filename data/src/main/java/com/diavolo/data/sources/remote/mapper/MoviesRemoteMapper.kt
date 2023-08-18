package com.diavolo.data.sources.remote.mapper

import com.diavolo.data.sources.remote.model.RemoteGenreMoviesResponse
import com.diavolo.data.sources.remote.model.RemoteMoviesResponse
import com.diavolo.data.sources.remote.model.RemoteReviewListResponse
import com.diavolo.data.sources.remote.model.RemoteTrailerResponse
import com.diavolo.model.Genre
import com.diavolo.model.GenreResponse
import com.diavolo.model.Movie
import com.diavolo.model.MoviesResponse
import com.diavolo.model.Review
import com.diavolo.model.ReviewMoviesResponse
import com.diavolo.model.Trailer
import com.diavolo.model.TrailerResponse
import com.marcoscg.movies.model.utils.orDefault
import com.marcoscg.movies.model.utils.orFalse

class MoviesRemoteMapper {

    fun mapGenreFromRemote(remoteGenreMoviesResponse: RemoteGenreMoviesResponse): GenreResponse {
        return GenreResponse(remoteGenreMoviesResponse.results.map { remoteGenre ->
            Genre(
                remoteGenre.id, remoteGenre.name.orEmpty()
            )
        })
    }

    fun mapFromRemote(remoteMoviesResponse: RemoteMoviesResponse): MoviesResponse {
        return MoviesResponse(remoteMoviesResponse.page,
            remoteMoviesResponse.total_results,
            remoteMoviesResponse.total_pages,
            remoteMoviesResponse.results.map { remoteMovie ->
                Movie(
                    remoteMovie.popularity.orDefault(),
                    remoteMovie.vote_count.orDefault(),
                    remoteMovie.video.orFalse(),
                    remoteMovie.poster_path.orEmpty(),
                    remoteMovie.id,
                    remoteMovie.adult.orFalse(),
                    remoteMovie.backdrop_path.orEmpty(),
                    remoteMovie.original_language.orEmpty(),
                    remoteMovie.original_title.orEmpty(),
                    remoteMovie.title,
                    remoteMovie.vote_average.orDefault(),
                    remoteMovie.overview.orEmpty(),
                    remoteMovie.release_date.orEmpty()
                )
            })
    }

    fun mapReviewFromRemote(remoteReviewListResponse: RemoteReviewListResponse): ReviewMoviesResponse {
        return ReviewMoviesResponse(
            remoteReviewListResponse.id,
            remoteReviewListResponse.page,
            remoteReviewListResponse.reviews.map { remoteReview ->
                Review(
                    remoteReview.author,
                    remoteReview.content,
                    remoteReview.createdAt,
                    remoteReview.id,
                    remoteReview.updatedAt,
                    remoteReview.url,
                )
            },
            remoteReviewListResponse.totalPages,
            remoteReviewListResponse.totalResults,

            )
    }

    fun mapTrailerfromRemote(remoteTrailerResponse: RemoteTrailerResponse): TrailerResponse {
        return TrailerResponse(
            remoteTrailerResponse.id,
            remoteTrailerResponse.trailers.map { remoteTrailer ->
                Trailer(
                    remoteTrailer.id,
                    remoteTrailer.name,
                    remoteTrailer.key,
                    remoteTrailer.iso6391,
                    remoteTrailer.iso31661,
                    remoteTrailer.official,
                    remoteTrailer.publishedAt,
                    remoteTrailer.site,
                    remoteTrailer.size,
                    remoteTrailer.type,
                )
            },
        )
    }
}