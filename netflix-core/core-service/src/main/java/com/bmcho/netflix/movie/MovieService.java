package com.bmcho.netflix.movie;

import com.bmcho.netfilx.movie.PersistenceMoviePort;
import com.bmcho.netfilx.movie.TmdbMoviePort;
import com.bmcho.netfilx.movie.TmdbPageableMovies;
import com.bmcho.netflix.movie.response.MovieResponse;
import com.bmcho.netflix.movie.response.PageableMoviesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService implements FetchMovieUseCase, InsertMovieUseCase {

    private final TmdbMoviePort tmdbMoviePort;
    private final PersistenceMoviePort persistenceMoviePort;

    @Override
    public PageableMoviesResponse fetchFromClient(int page) {
        TmdbPageableMovies tmdbPageableMovies = tmdbMoviePort.fetchPageable(page);
        return new PageableMoviesResponse(
                tmdbPageableMovies.tmdbMovies().stream()
                        .map(movie -> new MovieResponse(
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
}
