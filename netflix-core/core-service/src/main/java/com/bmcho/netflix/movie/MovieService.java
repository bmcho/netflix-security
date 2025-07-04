package com.bmcho.netflix.movie;

import com.bmcho.netfilx.movie.*;
import com.bmcho.netflix.exception.ErrorCode;
import com.bmcho.netflix.exception.NetflixException;
import com.bmcho.netflix.movie.response.MovieBatchResponse;
import com.bmcho.netflix.movie.response.MovieResponse;
import com.bmcho.netflix.movie.response.PageableMoviesBatchResponse;
import com.bmcho.netflix.movie.response.PageableMoviesResponse;
import com.bmcho.netflix.movie.validator.UserMovieDownloadValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService implements FetchMovieUseCase, InsertMovieUseCase, DownloadMovieUseCase, LikeMovieUseCase {

    private final TmdbMoviePort tmdbMoviePort;
    private final PersistenceMoviePort persistenceMoviePort;
    private final DownloadMoviePort downloadMoviePort;
    private final LikeMoviePort likeMoviePort;
    private final UserMovieDownloadValidatorFactory userMovieDownloadValidatorFactory;

    @Override
    public PageableMoviesBatchResponse fetchFromClient(int page) {
        TmdbPageableMovies tmdbPageableMovies = tmdbMoviePort.fetchPageable(page);
        return new PageableMoviesBatchResponse(
            tmdbPageableMovies.tmdbMovies().stream()
                .map(movie -> new MovieBatchResponse(
                    movie.movieName(),
                    movie.isAdult(),
                    movie.genre(),
                    movie.overview(),
                    movie.releaseAt()
                )).collect(Collectors.toList()),
            tmdbPageableMovies.page(),
            tmdbPageableMovies.hasNext()
        );
    }

    @Override
    public PageableMoviesResponse fetchFromDb(int page) {
        List<NetflixMovie> moviesByPageAndSize = persistenceMoviePort.fetchByPageAndSize(page, 10);

        return new PageableMoviesResponse(
            moviesByPageAndSize.stream().map(movie -> new MovieResponse(
                movie.movieId(),
                movie.movieName(),
                movie.isAdult(),
                StringToList(movie.genre()),
                movie.overview(),
                movie.releasedAt()
            )).toList(),
            page,
            true
        );
    }

    @Override
    public void insert(List<MovieResponse> movies) {
        movies.forEach(movie -> {
            NetflixMovie netflixMovie = NetflixMovie.builder()
                .movieName(movie.movieName())
                .isAdult(movie.isAdult())
                .overview(movie.overview())
                .releasedAt(movie.releaseAt())
                .genre(String.join(",", movie.genre()))
                .build();

            persistenceMoviePort.insert(netflixMovie);
        });
    }

    @Override
    public String downloadMovie(String userId, String role, String movieId) {
        long currentDownloadCount = downloadMoviePort.downloadCntToday(userId);
        if (!userMovieDownloadValidatorFactory.getValidator(role).validate(currentDownloadCount)) {
            throw new NetflixException(ErrorCode.NO_MORE_MOVIE_DOWNLOAD);
        }

        NetflixMovie movie = persistenceMoviePort.findByMovieId(movieId);
        downloadMoviePort.save(UserMovieDownload.newDownload(userId, movieId));
        return movie.movieName();
    }

    @Override
    public void likeMovie(String userId, String movieId) {
        Optional<UserMovieLike> movieLike = likeMoviePort.findByUserIdAndMovieId(userId, movieId);
        if (movieLike.isEmpty()) {
            likeMoviePort.save(UserMovieLike.newLike(userId, movieId));
        } else {
            UserMovieLike userMovieLike = movieLike.get();
            userMovieLike.like();
            likeMoviePort.save(userMovieLike);
        }
    }

    @Override
    public void unlikeMovie(String userId, String movieId) {
        Optional<UserMovieLike> movieLike = likeMoviePort.findByUserIdAndMovieId(userId, movieId);
        if (movieLike.isEmpty()) {
            likeMoviePort.save(UserMovieLike.newLike(userId, movieId));
        } else {
            UserMovieLike userMovieLike = movieLike.get();
            userMovieLike.unlike();
            likeMoviePort.save(userMovieLike);
        }
    }

    private static List<String> StringToList(String genre) {
        return Optional.ofNullable(genre)
            .map(stringGenre -> Arrays.asList(stringGenre.split(",")))
            .orElse(Collections.emptyList());
    }
}
