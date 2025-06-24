package com.bmcho.netfilx.movie;

public interface TmdbMoviePort {
    TmdbPageableMovies fetchPageable(int page);
}
