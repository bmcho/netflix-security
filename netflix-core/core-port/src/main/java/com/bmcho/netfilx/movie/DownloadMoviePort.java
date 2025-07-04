package com.bmcho.netfilx.movie;

import com.bmcho.netflix.movie.UserMovieDownload;

public interface DownloadMoviePort {
    void save(UserMovieDownload domain);

    long downloadCntToday(String userId);
}
