package org.imdb.controller;

import org.imdb.model.Movie;
import org.imdb.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/imdb")
public class QueryController {
    private final ImdbService imdbService;

    @Autowired
    public QueryController(ImdbService imdbService) {
        this.imdbService = imdbService;
    }

    @GetMapping("/_search/range")
    public ResponseEntity<List<Movie>> getRangedMovies(@RequestParam int from
            , @RequestParam int size){
        return ResponseEntity.ok(imdbService.getRangedMovies(from, size));
    }

    @GetMapping("/_search/title")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@RequestParam String title){
        return ResponseEntity.ok(imdbService.getMoviesByTitle(title));
    }

    @GetMapping("/_search/recommended")
    public ResponseEntity<List<Movie>> getRecommended(int year, int size){
        return ResponseEntity.ok(imdbService.getRecommended(year, size));
    }

}
