package com.bmcho.netflix.respository.movie.Download;


public interface UserMovieDownloadCustomRepository {
    long countDownloadToday(String userId);
}
