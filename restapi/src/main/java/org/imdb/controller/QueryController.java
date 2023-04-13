package org.imdb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(description = "Returns the movies in a range")
    @GetMapping("/_search/range")
    public ResponseEntity<List<Movie>> getRangedMovies(@Parameter(description =
            "From which number of movie to search", required = true)@RequestParam int from
            ,@Parameter(description = "Size of the query result", required = true)
             @RequestParam int size){
        return ResponseEntity.ok(imdbService.getRangedMovies(from, size));
    }

    @Operation(description = "Returns the movies found that match part of the" +
            " title and correspond to the applied filter")
    @GetMapping("/_search/title")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@Parameter(description =
            "Text to search for the movie", required = true)@RequestParam String title,
                                                        @RequestParam String type){

        return ResponseEntity.ok(imdbService.getMoviesByTitle(title, type));
    }

    @Operation(description = "Returns the recommeded movies in a specific year")
    @GetMapping("/_search/recommended")
    public ResponseEntity<List<Movie>> getRecommended(@Parameter(description
            = "Year of the film", required = true) int year, @Parameter(description
            = "Size of the " +"query result",required = true) int size){
        return ResponseEntity.ok(imdbService.getRecommended(year, size));
    }


    @GetMapping("/_search")
    public ResponseEntity<List<Movie>> getMoviesFiltered(@RequestParam int minYear,
                                                         @RequestParam int maxYear,
                                                         @RequestParam int maxRuntimeMin,
                                                         @RequestParam int minRuntimeMin,
                                                         @RequestParam double minAvgRating,
                                                         @RequestParam double maxAvgRating,
                                                         @RequestParam String type,
                                                         @RequestParam String[] genres
                                                         )
    {
        return ResponseEntity.ok(imdbService.getMoviesFiltered(minYear,
                maxYear, maxRuntimeMin, minRuntimeMin, minAvgRating,
                maxAvgRating, type, genres));
    }

    @GetMapping("/_search/not-to-watch")
    public ResponseEntity<List<Movie>> getNotToWatchMovies(){
        return ResponseEntity.ok(imdbService.getNotToWatchMovies());
    }

    @GetMapping("_search/recommended-all-times")
    public ResponseEntity<List<Movie>> getAllTimesRecommended(){
        return ResponseEntity.ok(imdbService.getAllTimesRecommended());
    }

}
