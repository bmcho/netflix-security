package com.bmcho.netfilx.movie;


import lombok.Getter;

import java.util.List;

public record TmdbPageableMovies(
    List<TmdbMovie> tmdbMovies,
    int page,
    boolean hasNext
) {
}
