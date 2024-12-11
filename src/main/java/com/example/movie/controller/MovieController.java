package com.example.movie.controller;

import com.example.movie.DTO.ActorResponseDto;
import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseDto;
import com.example.movie.service.ActorService;
import com.example.movie.service.MovieService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Autowired
    private ActorService actorService;

    @PostMapping
    public ResponseEntity<MovieResponseDto> createMovie(@Valid @RequestBody MovieRequestDto movieRequestDto) {
        MovieResponseDto newMovie = movieService.createMovie(movieRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMovie);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponseDto> getMovieById(@PathVariable Long id) {
        MovieResponseDto movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @GetMapping
    public ResponseEntity<Page<MovieResponseDto>> getAllMovies(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Max(50) int size // Enforce max size limit
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> movies = movieService.getAllMovies(pageable);
        return ResponseEntity.ok(movies); // Return 200 OK
    }

    @GetMapping("/search/by-title")
    public ResponseEntity<List<MovieResponseDto>> findByTitleContainingIgnoreCase(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> moviePage = movieService.searchMoviesByTitle(title, pageable);
        return ResponseEntity.ok(moviePage.getContent());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MovieResponseDto> updateMovie(@PathVariable Long id, @RequestBody MovieRequestDto movieDetails) {
        MovieResponseDto updatedMovie = movieService.updateMovie(id, movieDetails);
        return ResponseEntity.ok(updatedMovie);
    }

    @GetMapping("/search/by-year")
    public ResponseEntity<?> getMoviesByYear(
            @RequestParam(required = false) Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("Invalid pagination parameters");
        }
        if (year == null) {
            return ResponseEntity.badRequest().body("Year parameter is required");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> movies = movieService.getMoviesByReleaseYear(year, pageable);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}/actors")
    public ResponseEntity<List<ActorResponseDto>> getAllActorsInMovie(@PathVariable Long movieId) {
        List<ActorResponseDto> actors = movieService.getAllActorsInMovie(movieId);
        return ResponseEntity.ok(actors);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id, @RequestParam(defaultValue = "false") boolean cascade) {
        movieService.deleteMovie(id, cascade);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-genre")
    public ResponseEntity<Page<MovieResponseDto>> getMoviesByGenreId(
            @RequestParam Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MovieResponseDto> movies = movieService.getMoviesByGenreId(genreId, pageable);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/by-actor")
    public ResponseEntity<List<MovieResponseDto>> getMoviesByActorId(@RequestParam Long actorId) {
        List<MovieResponseDto> movies = movieService.getMoviesByActorId(actorId);
        return ResponseEntity.ok(movies);
    }
}
