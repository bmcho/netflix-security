package com.bmcho.netflix.movie.response;

import java.util.List;

public record PageableMoviesResponse(
    List<MovieResponse> movieResponses,
    int page,
    boolean hssNext
) {
}
