package com.example.movie.service;

import com.example.movie.DTO.ActorResponseDto;
import com.example.movie.DTO.MovieRequestDto;
import com.example.movie.DTO.MovieResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {
    MovieResponseDto createMovie(MovieRequestDto movieRequestDto);
    MovieResponseDto getMovieById(Long id);
    Page<MovieResponseDto> getAllMovies(Pageable pageable); 
    MovieResponseDto updateMovie(Long id, MovieRequestDto movieDetails);
    void deleteMovie(Long id, boolean cascade);
    Page<MovieResponseDto> searchMoviesByTitle(String title, Pageable pageable);
    Page<MovieResponseDto> getMoviesByReleaseYear(int releaseYear, Pageable pageable);
    List<MovieResponseDto> getMoviesByActorId(Long actorId);
    List<ActorResponseDto> getAllActorsInMovie(Long movieId);
    Page<MovieResponseDto> getMoviesByGenreId(Long genreId, Pageable pageable);
}
