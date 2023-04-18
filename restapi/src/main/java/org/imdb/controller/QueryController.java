package org.imdb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.imdb.model.Movie;
import org.imdb.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imdb")
public class QueryController {

    private final QueryService queryService;

    @Autowired
    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @Operation(description = "Returns the movies in a range", responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
    @GetMapping("/_search/range")
    public ResponseEntity<List<Movie>> getRangedMovies(@Parameter(description =
            "From which number of movie to search", required = true)@RequestParam int from
            ,@Parameter(description = "Size of the query result", required = true)
             @RequestParam int size){
        try {
            return ResponseEntity.ok(queryService.getRangedMovies(from, size));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the movies found that match part of the" +
            " title and correspond to the applied filter", responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
    @GetMapping("/_search/title")
    public ResponseEntity<List<Movie>> getMoviesByTitle(@Parameter(description =
            "Text to search for the movie", required = true)@RequestParam String title, @Parameter(description = "Type to be searched MOVIE, EPISODE, ALL", required = true)
                                                        @RequestParam String type){

        try {
            return ResponseEntity.ok(queryService.getMoviesByTitle(title, type));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the recommeded movies in a specific year", responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
    @GetMapping("/_search/recommended")
    public ResponseEntity<List<Movie>> getRecommended(@Parameter(description
            = "Year of the film", required = true) int year, @Parameter(description
            = "Size of the " +"query result",required = true) int size){
        try {
            return ResponseEntity.ok(queryService.getRecommended(year, size));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the movies filtered by year, " +
            "runtimeMin, avgRating, type and genre", responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
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
        try {
            return ResponseEntity.ok(queryService.getMoviesFiltered(minYear,
                    maxYear, maxRuntimeMin, minRuntimeMin, minAvgRating,
                    maxAvgRating, type, genres));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the movies that you must NOT watch",
            responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
    @GetMapping("/_search/not-to-watch")
    public ResponseEntity<List<Movie>> getNotToWatchMovies(){
        try {
            return ResponseEntity.ok(queryService.getNotToWatchMovies());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the all times recommended movies",
            responses = {
            @ApiResponse(responseCode = "202", description = "Movies have " +
                    "been found"),
            @ApiResponse(responseCode = "404", description = "Movies were not" +
                    " found")})
    @GetMapping("_search/recommended-all-times")
    public ResponseEntity<List<Movie>> getAllTimesRecommended(){
        try {
            return ResponseEntity.ok(queryService.getAllTimesRecommended());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(description = "Returns the movies that contains the genres " +
            "specified and do not contains the specified",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Movies have " +
                            "been found"),
                    @ApiResponse(responseCode = "404", description = "Movies were not" +
                            " found")})
    @GetMapping("_search/genres")
    public ResponseEntity<List<Movie>> getFilmsByGenres(@Parameter(description = "Genres that must appear in the movies", required = true)
                                                            @RequestParam String[] mustGenres,
                                                        @Parameter(description = "Genres that must not appear in the movies", required = true)
                                                        @RequestParam String[] mustNotGenres,
                                                        @Parameter(description = "Excluded ids of movies", required = true)
                                                        @RequestParam String[] excludedIds){
        try{
            return ResponseEntity.ok(queryService.getFilmsByGenres(mustGenres
                    , mustNotGenres, excludedIds));
        }catch(IOException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




}
