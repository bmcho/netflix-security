package com.bmcho.netflix.batch;

import com.bmcho.netflix.movie.FetchMovieUseCase;
import com.bmcho.netflix.movie.response.MovieBatchResponse;
import com.bmcho.netflix.movie.response.PageableMoviesBatchResponse;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;

import java.util.LinkedList;
import java.util.List;

public class HttpPageItemReader extends AbstractItemCountingItemStreamItemReader<MovieBatchResponse> {

    private final List<MovieBatchResponse> contents = new LinkedList<>();

    private int page;
    private final FetchMovieUseCase fetchMovieUseCase;

    public HttpPageItemReader(int page, FetchMovieUseCase fetchMovieUseCase) {
        this.page = page;
        this.fetchMovieUseCase = fetchMovieUseCase;
    }

    @Override
    protected MovieBatchResponse doRead() throws Exception {
        if (this.contents.isEmpty()) {
            PageableMoviesBatchResponse moviePageableResponse = fetchMovieUseCase.fetchFromClient(page);
            contents.addAll(moviePageableResponse.getMovieBatchResponses());
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
