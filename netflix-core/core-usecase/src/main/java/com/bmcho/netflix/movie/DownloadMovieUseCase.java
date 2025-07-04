package com.bmcho.netflix.movie;

public interface DownloadMovieUseCase {
    String downloadMovie(String userId, String role, String movieId);
}
