package com.bmcho.netflix.batch;

import com.bmcho.netflix.movie.FetchMovieUseCase;
import com.bmcho.netflix.movie.response.MovieResponse;
import com.bmcho.netflix.movie.response.PageableMoviesResponse;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;

public class HttpPageItemReader extends AbstractItemCountingItemStreamItemReader<MovieResponse> {

    private final List<MovieResponse> contents = new LinkedList<>();

    private int page;
    private final FetchMovieUseCase fetchMovieUseCase;

    public HttpPageItemReader(int page, FetchMovieUseCase fetchMovieUseCase) {
        this.page = page;
        this.fetchMovieUseCase = fetchMovieUseCase;
    }

    @Nullable
    protected MovieResponse doRead() throws Exception {
        if (this.contents.isEmpty()) {
            PageableMoviesResponse moviePageableResponse = fetchMovieUseCase.fetchFromClient(page);
            contents.addAll(moviePageableResponse.movieResponses());
            page++;
        }

        int size = contents.size();
        int index = size - 1;

        if (index < 0) {
            return null;
        }

        return contents.remove(contents.size() - 1);
    }

    @Override
    protected void doOpen() throws Exception {
        setName(HttpPageItemReader.class.getName());
    }

    @Override
    protected void doClose() throws Exception {}
}
