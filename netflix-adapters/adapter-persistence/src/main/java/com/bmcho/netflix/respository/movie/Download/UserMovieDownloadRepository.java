package com.bmcho.netflix.respository.movie.Download;

import com.bmcho.netfilx.movie.DownloadMoviePort;
import com.bmcho.netflix.entity.movie.UserMovieDownloadEntity;
import com.bmcho.netflix.movie.UserMovieDownload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserMovieDownloadRepository implements DownloadMoviePort {
    private final UserMovieDownloadJpaRepository userMovieDownloadJpaRepository;

    @Override
    @Transactional
    public void save(UserMovieDownload userMovieDownload) {
        userMovieDownloadJpaRepository.save(
            UserMovieDownloadEntity.toEntity(userMovieDownload));
    }

    @Override
    @Transactional
    public long downloadCntToday(String userId) {
        return userMovieDownloadJpaRepository.countDownloadToday(userId);
    }
}
