package org.imdb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.imdb.model.Movie;
import org.imdb.service.ImdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/index")
public class IndexController {
    private final ImdbService imdbService;

    @Autowired
    public IndexController(ImdbService imdbService) {
        this.imdbService = imdbService;
    }

    @Operation(description = "Given the files they are upload, processed and " +
            "indexed",
            responses = {
            @ApiResponse(responseCode = "202", description = "Files has been " +
                    "uploaded"),
            @ApiResponse(responseCode = "404", description = "File not found")})
    @PostMapping("/uploadMovies")
    public ResponseEntity uploadFiles(@RequestParam("basics") MultipartFile basics,
                                      @RequestParam("akas") MultipartFile akas,
                                      @RequestParam("ratings") MultipartFile ratings,
                                      @RequestParam("crew") MultipartFile crew,
                                      @RequestParam("principals") MultipartFile principals){
        try {
            imdbService.uploadFiles(basics, akas, ratings,crew, principals);
            return ResponseEntity.accepted().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }

    /*@Operation(description = "Creates the index with the specific mapping",
            responses = { @ApiResponse(responseCode = "200", description =
                    "Index created " +
                    "successfully"),
            @ApiResponse(responseCode = "400", description = "Index already " +
                    "exists")})
    @PutMapping("")
    public ResponseEntity createIndex(@Parameter(description = "Mapping file " +
            "to create the index", required = true) @RequestBody MultipartFile
                                                  file
                                      ){
        try {
            InputStream input = file.getInputStream();
            imdbService.createIndex(input);
            return ResponseEntity.ok("indexed");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }*/

    @Operation(description = "Given a movie it is indexed", responses = {
            @ApiResponse(responseCode = "200", description = "Movie indexed " +
                    "succesfully")
    })
    @PostMapping("/_doc")
    public ResponseEntity indexDocument(@Parameter(description = "Movie to be" +
            " indexed", required = true)@RequestBody Movie movie){
        imdbService.indexDocument(movie);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Deletes the index",
            responses = {
            @ApiResponse(responseCode = "200", description = "Index deleted " +
                    "successfully"),
            @ApiResponse(responseCode = "404", description = "Index not " +
                    "found")})
    @DeleteMapping("/delete")
    public ResponseEntity deleteIndex(@Parameter(description = "index to be " +
            "deleted", required = true) @RequestParam String indexName){
        imdbService.deleteIndex(indexName);
        return ResponseEntity.ok("deleted");
    }

    @Operation(description = "Returns information about the index")
    @GetMapping("/_cat/indices")
    public String getIndices(){
        return imdbService.getIndixes().toString();
    }



}
