package com.bmcho.netfilx.movie;

import com.bmcho.netflix.movie.NetflixMovie;

import java.util.List;

public interface PersistenceMoviePort {
    List<NetflixMovie> fetchByPageAndSize(int page, int size);

    NetflixMovie findByMovieName(String movieName);

    NetflixMovie findByMovieId(String movieName);

    void insert(NetflixMovie netflixMovie);
}
