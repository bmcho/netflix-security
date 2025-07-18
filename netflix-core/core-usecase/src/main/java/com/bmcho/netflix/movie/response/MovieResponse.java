package com.bmcho.netflix.movie.response;

import java.util.List;

public record MovieResponse(
    String movieId,
    String movieName,
    boolean isAdult,
    List<String> genre,
    String overview,
    String releaseAt
) {
}
