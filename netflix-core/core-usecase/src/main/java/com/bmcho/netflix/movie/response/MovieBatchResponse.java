package com.bmcho.netflix.movie.response;

import java.util.List;

public record MovieBatchResponse(
    String movieName,
    boolean isAdult,
    List<String> genre,
    String overview,
    String releaseAt
) {
}
