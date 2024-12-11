package com.example.movie.service;

import com.example.movie.DTO.GenreRequestDto;
import com.example.movie.DTO.GenreResponseDto;

import java.util.List;

public interface GenreService {

    // Creates a new genre using data from GenreRequestDto
    // Takes genreRequestDto as input and returns GenreResponseDto with the created genre details
    GenreResponseDto createGenre(GenreRequestDto genreRequestDto);

    // Retrieves a genre by its ID
    // Takes a genre ID as input and returns GenreResponseDto with the found genre details
    GenreResponseDto getGenreById(Long id);

    // Retrieves all genres, optionally with pagination
    // Accepts page number and page size as optional input parameters
    // Returns a list of GenreResponseDto containing details of all found genres
    List<GenreResponseDto> getAllGenres(Integer page, Integer size);

    // Updates an existing genre by ID using data from GenreRequestDto
    // Takes a genre ID and genreRequestDto as input, and returns GenreResponseDto with the updated genre details
    GenreResponseDto updateGenre(Long id, GenreRequestDto genreRequestDto);

    // Deletes a genre by its ID, with an option for cascading deletion of associated movies
    // Takes a genre ID and a boolean 'cascade' parameter as input
    // If cascade is true, deletes associated movies as well
    void deleteGenre(Long id, boolean cascade);
}
