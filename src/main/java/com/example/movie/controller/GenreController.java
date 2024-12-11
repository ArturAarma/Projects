package com.example.movie.controller;

import com.example.movie.DTO.GenreRequestDto;
import com.example.movie.DTO.GenreResponseDto;
import com.example.movie.service.GenreService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    // Endpoint to create a new Genre
    @PostMapping
    public ResponseEntity<GenreResponseDto> createGenre(@Valid @RequestBody GenreRequestDto genre) {
        GenreResponseDto createdGenre = genreService.createGenre(genre);
        return new ResponseEntity<>(createdGenre, HttpStatus.CREATED); // Return 201 Created
    }

    // Endpoint to get a Genre by its ID
    @GetMapping("/{id}")
    public ResponseEntity<GenreResponseDto> getGenreById(@PathVariable Long id) {
        GenreResponseDto genre = genreService.getGenreById(id);
        return ResponseEntity.ok(genre); // Return 200 OK with the genre data
    }

    // Endpoint to get all Genres, with optional pagination
    @GetMapping
    public ResponseEntity<List<GenreResponseDto>> getAllGenre(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer page_size
    ) {
        List<GenreResponseDto> genres = genreService.getAllGenres(page, page_size);
        return ResponseEntity.ok(genres); // Return 200 OK with list of genres
    }

    // Endpoint to update an existing Genre
    @PatchMapping("/{id}")
    public ResponseEntity<GenreResponseDto> updateGenre(@PathVariable Long id, @RequestBody GenreRequestDto genreDetails) {
        GenreResponseDto updatedGenre = genreService.updateGenre(id, genreDetails);
        return ResponseEntity.ok(updatedGenre); // Return 200 OK with updated genre data
    }

    // Endpoint to delete a Genre by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        genreService.deleteGenre(id, cascade);
        if (cascade) {
            return new ResponseEntity<>("Genre and its associated movies have been deleted.", HttpStatus.NO_CONTENT); // Return 204 No Content
        } else {
            return new ResponseEntity<>("Genre has been deleted.", HttpStatus.NO_CONTENT); // Return 204 No Content
        }
    }
}