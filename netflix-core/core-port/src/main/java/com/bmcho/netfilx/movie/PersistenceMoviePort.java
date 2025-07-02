package com.bmcho.netfilx.movie;

import com.bmcho.netflix.movie.NetflixMovie;

import java.util.List;

public interface PersistenceMoviePort {
    List<NetflixMovie> fetchByPageAndSize(int page, int size);

    NetflixMovie findById(String movieName);

    void insert(NetflixMovie netflixMovie);
}
