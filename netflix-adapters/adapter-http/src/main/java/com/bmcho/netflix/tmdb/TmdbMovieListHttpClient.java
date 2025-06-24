package com.bmcho.netflix.tmdb;

import com.bmcho.netfilx.movie.TmdbMovie;
import com.bmcho.netfilx.movie.TmdbMoviePort;
import com.bmcho.netfilx.movie.TmdbPageableMovies;
import com.bmcho.netflix.client.TmdbHttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class TmdbMovieListHttpClient implements TmdbMoviePort {

    @Value("${tmdb.api.movie-lists.now-playing}")
    private String nowPlayingUrl;

    public final TmdbHttpClient tmdbHttpClient;

    @Override
    public TmdbPageableMovies fetchPageable(int page) {
        String url = nowPlayingUrl + "?language=ko-KR&page=" + page;
        String request = tmdbHttpClient.request(url, HttpMethod.GET, CollectionUtils.toMultiValueMap(Map.of()), Map.of());

        TmdbMovieNowPlayingResponse tmdbMovieNowPlayingResponse = null;
        try {
            tmdbMovieNowPlayingResponse = new ObjectMapper().readValue(request, TmdbMovieNowPlayingResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return new TmdbPageableMovies(
            tmdbMovieNowPlayingResponse.getResults().stream()
                .map(movie -> new TmdbMovie(
                    movie.getTitle(),
                    movie.getAdult(),
                    movie.getGenreIds(),
                    movie.getOverview(),
                    movie.getReleaseDate()
                ))
                .toList(),
            page,
            tmdbMovieNowPlayingResponse.getTotalPages() - page != 0
        );

    }
}
