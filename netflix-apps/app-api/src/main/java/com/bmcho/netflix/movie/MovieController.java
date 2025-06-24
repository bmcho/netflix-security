package com.bmcho.netflix.movie;

import com.bmcho.netflix.movie.response.PageableMoviesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
public class MovieController {

    private final FetchMovieUseCase fetchMovieUseCase;

    @GetMapping("/client/{page}")
    public String fetchMoviePageable(@PathVariable("page") int page) {
        PageableMoviesResponse pageableMoviesResponse = fetchMovieUseCase.fetchFromClient(page);
        return "success";
    }
}
