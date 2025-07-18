package com.bmcho.netflix.movie;

import lombok.Builder;

@Builder
public record NetflixMovie(
    String movieId,
    String movieName,
    Boolean isAdult,
    String genre,
    String overview,
    String releasedAt
) {
}
