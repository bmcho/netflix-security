package com.bmcho.netflix.movie;

import com.bmcho.netfilx.movie.TmdbMoviePort;
import com.bmcho.netfilx.movie.TmdbPageableMovies;
import com.bmcho.netflix.movie.response.MovieResponse;
import com.bmcho.netflix.movie.response.PageableMoviesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService implements FetchMovieUseCase {

    private final TmdbMoviePort tmdbMoviePort;

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
}
